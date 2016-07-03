package com.longway.framework.ui.dialog;

import android.content.Context;

/**
 * Created by longway on 15/12/11.
 * 列表dialog
 */
public abstract class BaseListDialog<T> extends BaseDialog {

    protected T mAdapter;

    public BaseListDialog(Context context, T adapter) {
        super(context);
        mAdapter = adapter;
    }

    public BaseListDialog(Context context, int theme, T adapter) {
        super(context, theme);
        mAdapter = adapter;
    }

    public BaseListDialog(Context context, boolean cancelable, OnCancelListener cancelListener, T adapter) {
        super(context, cancelable, cancelListener);
        mAdapter = adapter;
    }


    public BaseListDialog(Context context) {
        super(context);
    }

    public BaseListDialog(Context context, int theme) {
        super(context, theme);
    }

    protected BaseListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
