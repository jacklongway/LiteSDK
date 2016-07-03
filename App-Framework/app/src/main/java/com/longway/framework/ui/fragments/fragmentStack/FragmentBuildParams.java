package com.longway.framework.ui.fragments.fragmentStack;

import android.os.Bundle;

import com.longway.framework.ui.fragments.BaseFragment;

/**
 * Created by longway on 16/4/23.
 * Email:longway1991117@sina.com
 */
public class FragmentBuildParams {
    public final Class<? extends BaseFragment> mClz;
    public final Bundle mParams;

    public FragmentBuildParams(Class<? extends BaseFragment> clz, Bundle params) {
        this.mClz = clz;
        this.mParams = params;
    }

}
