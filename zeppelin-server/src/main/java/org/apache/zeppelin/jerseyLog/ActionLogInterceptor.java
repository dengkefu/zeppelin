package org.apache.zeppelin.jerseyLog;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.zeppelin.jerseyLog.LogEntity.OperationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @program: zeppelin
 * @description:
 * @author: duncan.fu@foxmail.com
 * @create: 2021-05-19 09:22
 */
@ActionLog
public class ActionLogInterceptor implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ActionLogInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 在此处处理日志入库等操作
        Method method = methodInvocation.getMethod();
        addOperationLog(method);
        return methodInvocation.proceed();
    }

    private void addOperationLog(Method method) {
        Subject currentUser = SecurityUtils.getSubject();

        OperationLog operationLog = new OperationLog();
        operationLog.setCreateTime(new Date());
        // 获取类+方法名
        System.out.println("method.getName() = " + method.getDeclaringClass()+"."+method.getName());
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("annotation.toString() = " + annotation.toString());
        }

        System.out.println("currentUser.getSession().getHost() = " + currentUser.getSession().getHost());
        //operationLog.setUserId();

        //TODO 这里保存日志
        System.out.println("记录日志:" + operationLog.toString());
        LOG.info("记录日志:" + operationLog.toString());
        //operationLogService.insert(operationLog);
    }


}