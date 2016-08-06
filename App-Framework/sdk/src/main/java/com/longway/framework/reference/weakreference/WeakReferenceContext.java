package com.longway.framework.reference.weakreference;

import android.content.Context;

import com.longway.framework.util.Utils;


/**
 * Context可用性检测器
 *
 * @author longway
 */
public class WeakReferenceContext extends BaseWeakReference<Context> {

    public WeakReferenceContext(Context context) {
        super(context);
    }

    @Override
    public boolean referenceActive() {
        return Utils.contextIsValidate(getReference());
    }
}
