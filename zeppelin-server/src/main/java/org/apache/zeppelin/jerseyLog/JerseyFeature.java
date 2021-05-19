package org.apache.zeppelin.jerseyLog;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * @program: zeppelin
 * @description:
 * @author: duncan.fu@foxmail.com
 * @create: 2021-05-19 09:24
 */
public class JerseyFeature implements Feature {
    @Override
    public boolean configure(FeatureContext context) {
        context.register(new JerseyBinding());
        return true;
    }
}