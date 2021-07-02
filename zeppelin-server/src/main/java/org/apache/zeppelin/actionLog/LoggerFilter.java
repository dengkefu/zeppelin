package org.apache.zeppelin.actionLog;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.zeppelin.entity.log.OperationLog;
import org.apache.zeppelin.entity.log.OperationType;
import org.apache.zeppelin.service.OperationLogService;
import org.apache.zeppelin.threadUtils.CustomThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.annotation.Resource;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @program: zeppelin
 * @description:
 * @author: duncan.fu@foxmail.com
 * @create: 2021-05-19 17:16
 */
@Provider
@UserLogger
@Component
@Priority(Priorities.USER)
public class LoggerFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerFilter.class);

    @Resource
    private OperationLogService operationLogService;

    /**
     *  线程执行器，开启用以记录用户行为日志
     */
    CustomThreadPoolExecutor exec = new CustomThreadPoolExecutor();

    ExecutorService pool = null;

    @Context
    private ResourceInfo resourceInfo;

    LoggerDetail loggerDetail;

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        LOG.info("用户正在操作需要被记录的动作，保存需要被记录的内容");
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        LOG.info("用户操作完成，开始记录用户行为");
        // 初始化线程池
        if(pool == null || pool.isTerminated() || pool.isShutdown()){
            exec.init();
            pool = exec.getCustomThreadPoolExecutor();
        }
        Subject currentUser = SecurityUtils.getSubject();
        Method resourceMethod = resourceInfo.getResourceMethod();
        loggerDetail = resourceMethod.getAnnotation(LoggerDetail.class);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                // 注解内容
                String detail = loggerDetail.detail();
                String methodType = requestContext.getMethod();

                int level = loggerDetail.level();
                OperationType operationType = loggerDetail.operationType();
                String obj = loggerDetail.obj();
                // 访问信息
                MultivaluedMap<String, String> headers = requestContext.getHeaders();
                List<String> host = headers.getOrDefault("Host", Arrays.asList("127.0.0.1:8080"));
                // 用户信息
                String principal = currentUser.getPrincipal().toString();
                // 构建需要保存的日志对象
                OperationLog actionLog = new OperationLog(principal, host, detail, level, operationType, obj, methodType);
                operationLogService.insertLog(actionLog);
            }
        });
    }
}