package com.longway.framework.reference.weakreference;

import java.lang.ref.WeakReference;

/**
 * Email:longway1991117@sina.com
 */
public abstract class BaseWeakReference<T> {
    protected WeakReference<T> mWeakReference;

    public BaseWeakReference(T reference) {
        mWeakReference = new WeakReference<>(reference);
    }

    public abstract boolean referenceActive();

    public T getReference() {
        return mWeakReference.get();
    }
}
