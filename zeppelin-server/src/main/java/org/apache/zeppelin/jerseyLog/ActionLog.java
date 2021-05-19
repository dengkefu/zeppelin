package org.apache.zeppelin.jerseyLog;

import java.lang.annotation.*;

/**
 * @program: zeppelin
 * @description:
 * @author: duncan.fu@foxmail.com
 * @create: 2021-05-19 09:22
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionLog {
}