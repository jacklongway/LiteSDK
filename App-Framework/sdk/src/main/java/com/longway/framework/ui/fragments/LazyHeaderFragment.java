package com.longway.framework.ui.fragments;

import com.longway.framework.util.LogUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by longway on 15/11/12.
 * 延迟加载fragment
 */
public abstract class LazyHeaderFragment extends BaseHeaderFragment {
    private static final String TAG = "LazyFragment";
    private AtomicBoolean isFirstLoad = new AtomicBoolean();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstLoad.compareAndSet(false, true)) {
                lazyData(isVisibleToUser);
            }
        }
        onVisibleChange(isVisibleToUser);
    }

    protected abstract void lazyData(boolean isVisibleToUser);

    protected void onVisibleChange(boolean isVisibleToUser) {
        LogUtils.d(TAG + ":" + String.valueOf(isVisibleToUser));
    }
}
