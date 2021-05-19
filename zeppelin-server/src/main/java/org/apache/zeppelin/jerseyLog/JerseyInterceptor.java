package org.apache.zeppelin.jerseyLog;

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: zeppelin
 * @description:
 * @author: duncan.fu@foxmail.com
 * @create: 2021-05-19 09:22
 */
public class JerseyInterceptor implements InterceptionService {
    private static Map<Annotation, MethodInterceptor> map = new HashMap<>();

    static {
        Annotation[] annotations = ActionLogInterceptor.class.getAnnotations();
        for (Annotation annotation : annotations) {
            map.put(annotation, new ActionLogInterceptor());
        }
    }

    @Override
    public Filter getDescriptorFilter() {
        return new Filter() {
            @Override
            public boolean matches(Descriptor descriptor) {
                return true;
            }
        };
    }

    @Override
    public List<MethodInterceptor> getMethodInterceptors(Method method) {
        Annotation[] annotations = method.getAnnotations();
        List<MethodInterceptor> list = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (map.get(annotation) != null) {
                list.add(map.get(annotation));
            }
        }
        return list;
    }

    @Override
    public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> constructor) {
        return null;
    }
}