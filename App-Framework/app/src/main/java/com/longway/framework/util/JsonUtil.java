package com.longway.framework.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/*********************************
 * Created by longway on 16/5/27 下午2:30.
 * packageName:com.longway.framework.util
 * projectName:trunk
 * Email:longway1991117@sina.com
 ********************************/
public class JsonUtil {
    private static Gson sGson = new Gson();

    private JsonUtil() {
        throw new AssertionError("JsonUtil not instance");
    }

    public static String toJson(Object o) {
        return sGson.toJson(o);
    }

    public static <T> T fromJson(String json, Type type) {
        return sGson.fromJson(json, type);
    }

    public static <T> T fromJson(String json, Class<T> clz) {
        return sGson.fromJson(json, clz);
    }
}

