package org.apache.zeppelin.actionLog;

import javax.ws.rs.NameBinding;
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
@NameBinding //标识名称绑定的注解
@Target({ElementType.TYPE, ElementType.METHOD}) //表示该注解可以使用在类和方法上。
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UserLogger {

}