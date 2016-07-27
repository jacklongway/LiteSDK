package com.longway.framework.reference.weakreference;

import android.app.Activity;
import android.content.Context;

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
        Context context = getReference();
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            return !((Activity) context).isFinishing();
        }
        return true;
    }
}
