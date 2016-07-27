package com.longway.framework.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.longway.framework.util.ScreenUtils;

import butterknife.ButterKnife;

/**
 * Created by longway on 15/12/11.
 * dialog 通用类
 */
public abstract class BaseDialog extends Dialog implements DialogInterface.OnCancelListener, DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    protected View mRootView;

    public BaseDialog(Context context) {
        super(context, 0);
        initContext(context);
    }

    protected void setBackground(int resId) {
        mRootView.setBackgroundResource(resId);
    }


    protected void setBackgroundColor(int color) {
        mRootView.setBackgroundColor(color);
    }

    protected void setBackgroundDrawable(Drawable drawable) {
        mRootView.setBackgroundDrawable(drawable);
    }

    protected void initContext(Context context) {
        View rootView = View.inflate(context, getLayoutId(), null);
        mRootView = rootView;
        ButterKnife.inject(this, rootView);
        initView(rootView);
        setContentView(rootView, getLayoutParams());
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = getWindowWidth();
        params.height = getWindowHeight();
        window.setAttributes(params);
        dataBind();
        registerListener();
        setCanceledOnTouchOutside(false);
        setOnShowListener(this);
        if (setCancelListenerAndDismissListener()) {
            setOnCancelListener(this);
            setOnDismissListener(this);
        }
    }

    protected boolean setCancelListenerAndDismissListener() {
        return true;
    }

    protected int getWindowWidth() {
        return ScreenUtils.getScreenWidth(getContext()) * 3 / 4;
    }

    protected int getWindowHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onShow(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void hide() {
        super.hide();
    }


    protected ViewGroup.LayoutParams getLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        initContext(context);

    }

    protected abstract int getLayoutId();

    protected void initView(View view) {

    }

    protected void dataBind() {
    }

    protected void registerListener() {

    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
