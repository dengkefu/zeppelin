package org.apache.zeppelin.service;

import org.apache.zeppelin.dao.OperationLogDao;
import org.apache.zeppelin.entity.log.OperationLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: zeppelin
 * @description:
 * @author: duncan.fu@foxmail.com
 * @create: 2021-05-21 17:04
 */
@Service
public class OperationLogService {

    @Resource
    private OperationLogDao operationLogDao;

    public void insertLog(OperationLog operationLog){
        operationLogDao.insertSelective(operationLog);
    }
}