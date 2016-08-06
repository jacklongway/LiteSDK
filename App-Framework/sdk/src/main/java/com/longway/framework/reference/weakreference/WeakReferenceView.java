package com.longway.framework.reference.weakreference;

import android.view.View;

import com.longway.framework.util.Utils;

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
        return Utils.viewIsValidate(getReference());
    }
}
