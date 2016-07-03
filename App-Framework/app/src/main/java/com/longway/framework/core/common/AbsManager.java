package com.longway.framework.core.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by longway on 16/6/6.
 * Email:longway1991117@sina.com
 */

public abstract class AbsManager<T> {
    private Map<String, T> mImpls = new ConcurrentHashMap<>();
    private String mCurrentImpl;

    public T add(String key, T value) {
        return mImpls.put(key, value);
    }

    public T remove(String key) {
        return mImpls.remove(key);
    }

    public void use(String key) {
        mCurrentImpl = key;
    }

    public T addAndUse(String key, T value) {
        T old = add(key, value);
        use(key);
        return old;
    }

    public T getCurrentUse() {
        return mImpls.get(mCurrentImpl);
    }
}
