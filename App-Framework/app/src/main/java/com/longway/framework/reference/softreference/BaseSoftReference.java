package com.longway.framework.reference.softreference;

import java.lang.ref.SoftReference;

/*********************************
 * Created by longway on 16/5/26 上午10:23.
 * packageName:com.longway.framework.reference.softreference
 * projectName:trunk
 * Email:longway1991117@sina.com
 ********************************/
public abstract class BaseSoftReference<T> {
    private SoftReference<T> mSoftReference;

    public BaseSoftReference(T o) {
        mSoftReference = new SoftReference<>(o);
    }

    public T getReference() {
        return mSoftReference.get();
    }

    public abstract boolean referenceActive();
}
