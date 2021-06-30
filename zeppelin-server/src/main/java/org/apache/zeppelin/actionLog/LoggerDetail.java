package org.apache.zeppelin.actionLog;

import org.apache.zeppelin.entity.log.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: zeppelin
 * @description:
 * @author: duncan.fu@foxmail.com
 * @create: 2021-05-19 17:16
 */
@Target({ElementType.TYPE, ElementType.METHOD}) //表示该注解可以使用在类和方法上。
@Retention(value = RetentionPolicy.RUNTIME)
public @interface LoggerDetail {
    /**
     * 方法描述
     */
    String detail() default "";

    /**
     * 动态参数
     */
    String params() default  "";

    /**
     * 日志等级:自己定，此处分为1-9
     */
    int level() default 0;

    /**
     * 被操作对象
     */
    String obj() default "";

    /**
     * 操作类型(enum):主要是select,insert,update,delete
     */
    OperationType operationType() default OperationType.UNKNOWN;
}