package com.longway.framework.reference.softreference;

/*********************************
 * Created by longway on 16/5/26 上午10:36.
 * packageName:com.longway.framework.reference.softreference
 * projectName:trunk
 * Email:longway1991117@sina.com
 ********************************/
public class SoftReferenceObject extends BaseSoftReference<Object> {
    public SoftReferenceObject(Object o) {
        super(o);
    }

    @Override
    public boolean referenceActive() {
        return getReference() != null;
    }
}
