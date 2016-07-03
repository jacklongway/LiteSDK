package com.longway.framework.util;

/**
 * Created by longway on 16/6/23.
 * Email:longway1991117@sina.com
 */

public class OptionsUtils<T> {
    private T mReference;

    private OptionsUtils(T t) {
        this.mReference = t;
    }

    public static <T> OptionsUtils<T> option(T o) {
        return new OptionsUtils(o);
    }

    public boolean isPresent() {
        return mReference != null;
    }

    public T orElse(T t) {
        return mReference != null ? mReference : t;
    }

    public T get() {
        if (!isPresent()) {
            throw new IllegalStateException("reference not present");
        }
        return mReference;
    }
}
