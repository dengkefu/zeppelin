package org.apache.zeppelin.actionLog;

import ch.qos.logback.core.joran.action.ActionUtil;

import java.lang.annotation.*;

/**
 * @program: zeppelin
 * @description: 日志注解
 * @author: duncan.fu@foxmail.com
 * @create: 2021-05-14 14:21
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogDetail {

    /**
     * 方法描述,可使用占位符获取参数:{{tel}}
     */
    String detail() default "";

    /**
     * 日志等级:自己定，此处分为1-9
     */
    int level() default 0;

    /**
     * 操作类型(enum):主要是select,insert,update,delete
     */
    ActionType operationType() default ActionType.UNKNOWN;

    /**
     * 被操作的对象(此处使用enum):可以是任何对象，如表名(user)，或者是工具(redis)
     */
    ActionUnit operationUnit() default ActionUnit.UNKNOWN;
}