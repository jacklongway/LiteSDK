package com.longway.framework.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by longway on 16/6/17.
 * Email:longway1991117@sina.com
 */

public class ReflectUtils {
    private ReflectUtils() {

    }

    @SuppressWarnings("unchecked")
    public static Class<?> genericType(Class<?> clazz) {
        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    return (Class<?>) args[0];
                }
            }
            clazz = clazz.getSuperclass();
        }
        return clazz;
    }
}
