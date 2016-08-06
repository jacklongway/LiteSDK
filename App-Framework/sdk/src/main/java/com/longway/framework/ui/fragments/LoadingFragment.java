package com.longway.framework.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.longway.elabels.R;
import com.longway.framework.ui.activities.BaseActivity;
import com.longway.framework.util.Utils;

/**
 * Created by longway on 16/8/5.
 * Email:longway1991117@sina.com
 */

public class LoadingFragment extends BaseFragment {
    private ILoading mILoading = new ILoading() {
        @Override
        public int getContentView() {
            return R.layout.loading;
        }

        @Override
        public void initContentView(View contentView) {

        }

        @Override
        public void registerEvent(View contentView) {

        }

        @Override
        public void bindView(View contentView) {

        }
    };

    public static Fragment loading(int containerId, BaseActivity baseActivity) {
        return loading(containerId, null, baseActivity);
    }

    public static Fragment loading(int containerId, ILoading loading, BaseActivity baseActivity) {
        if (!Utils.contextIsValidate(baseActivity)) {
            return null;
        }
        return baseActivity.showLoading(containerId, loading);
    }


    public void setILoading(ILoading loading) {
        if (loading != null && loading != mILoading) {
            this.mILoading = loading;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return mILoading.getContentView();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mILoading.initContentView(view);
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mILoading.bindView(view);
    }

    @Override
    protected void registerEvent(View view) {
        super.registerEvent(view);
        mILoading.registerEvent(view);
    }
}
