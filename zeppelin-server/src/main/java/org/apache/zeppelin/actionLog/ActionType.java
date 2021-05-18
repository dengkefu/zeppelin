package org.apache.zeppelin.actionLog;

/**
 * @author duncan.fu@foxmail.com
 * @date 2021/5/14 14:21
 * @desc TODO
 */
public enum ActionType {
    /**
     * 操作类型
     */
    UNKNOWN("unknown"),
    DELETE("delete"),
    SELECT("select"),
    UPDATE("update"),
    INSERT("insert");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    ActionType(String s) {
        this.value = s;
    }
}
