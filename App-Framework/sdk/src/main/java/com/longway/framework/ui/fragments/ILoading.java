package com.longway.framework.ui.fragments;

import android.view.View;

/**
 * Created by longway on 16/8/6.
 * Email:longway1991117@sina.com
 */

public interface ILoading {
    int getContentView();
    void initContentView(View contentView);
    void registerEvent(View contentView);
    void bindView(View contentView);
}
