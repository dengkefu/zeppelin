package org.apache.zeppelin.actionLog;

/**
 * @author duncan.fu@foxmail.com
 * @date 2021/5/14 14:22
 * @desc TODO
 */
public enum ActionUnit {
    /**
     * 被操作的单元
     */
    UNKNOWN("unknown"),
    USER("user"),
    EMPLOYEE("employee"),
    Redis("redis");

    private String value;

    ActionUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
