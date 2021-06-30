package org.apache.zeppelin.entity.log;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * operation_log
 * @author 
 */
public class OperationLog implements Serializable {
    private Integer id;

    private String userId;

    private String level;

    private String operationUnit;

    private String method;

    private String describe;

    private String host;

    private String operationType;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public OperationLog(String principal, List<String> host, String detail, int level, OperationType operationType, String obj, String method) {
        this.userId = principal;
        this.host = host.get(0);
        this.describe = detail;
        this.level = String.valueOf(level);
        this.operationType = operationType.getValue();
        this.operationUnit = obj;
        this.method = method;
        this.createTime = new Date();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}