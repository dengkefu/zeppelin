package org.apache.zeppelin.jerseyLog;

import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

/**
 * @program: zeppelin
 * @description:
 * @author: duncan.fu@foxmail.com
 * @create: 2021-05-19 09:24
 */
public class JerseyBinding extends AbstractBinder {
    @Override
    protected void configure() {
        this.bind(JerseyInterceptor.class).to(InterceptionService.class).in(Singleton.class);
    }
}