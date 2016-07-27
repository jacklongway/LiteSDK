package com.longway.framework.reference.weakreference;

import android.support.v4.app.Fragment;

/**
 * Created by longway on 16/4/10.
 * Email:longway1991117@sina.com
 */
public class WeakReferenceFragment extends BaseWeakReference<Fragment> {
    public WeakReferenceFragment(Fragment fragment) {
        super(fragment);
    }

    @Override
    public boolean referenceActive() {
        Fragment fragment = mWeakReference.get();
        if (fragment == null || fragment.isDetached() || fragment.isRemoving()) {
            return false;
        }
        return true;
    }

}
