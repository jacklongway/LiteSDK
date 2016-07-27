package com.longway.framework.reference.weakreference;

import android.view.View;

/**
 * View可用性检测器
 *
 * @author longway
 */
public class WeakReferenceView extends BaseWeakReference<View> {
    public WeakReferenceView(View view) {
        super(view);
    }

    @Override
    public boolean referenceActive() {
        View view = getReference();
        if (view == null) {
            return false;
        }
        return true;
    }
}
