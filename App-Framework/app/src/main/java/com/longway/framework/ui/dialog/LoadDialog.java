package com.longway.framework.ui.dialog;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.longway.elabels.R;

public class LoadDialog extends BaseDialog {

    private ImageView mImageView;

    private TextView mHintText;

    private AnimationDrawable mAnimationDrawable;

    public LoadDialog(Context context, boolean cancelable,
                      OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public LoadDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected boolean setCancelListenerAndDismissListener() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.loading;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mImageView = (ImageView) view.findViewById(R.id.loading_iv);
        mHintText = (TextView) view.findViewById(R.id.loaing_hint_tv);
    }

    @Override
    protected void dataBind() {
        super.dataBind();
    }

    public LoadDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void setLoadingHintText(String text) {
        mHintText.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        super.show();
        if (mAnimationDrawable == null) {
            mAnimationDrawable = (AnimationDrawable) mImageView.getDrawable();
        }
        mAnimationDrawable.start();
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        super.hide();
        stop();
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        super.dismiss();
        stop();
    }

    private synchronized void stop() {
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
            mAnimationDrawable = null;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected ViewGroup.LayoutParams getLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
