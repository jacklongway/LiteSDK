package com.longway.framework.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.longway.elabels.R;

/**
 * Created by longway on 16/4/26.
 * Email:longway1991117@sina.com
 */
public class EmptyView extends LinearLayout {
    private ImageView mEmptyIcon;
    private Button mRetryBtn;

    public Button getRetryBtn() {
        return mRetryBtn;
    }

    public ImageView getEmptyIcon() {
        return mEmptyIcon;
    }

    public EmptyView(Context context) {
        super(context);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
    }


    public void setEmptyIcon(int resId) {
        mEmptyIcon.setImageResource(resId);
    }

    public void setEmptyIcon(Drawable drawable) {
        mEmptyIcon.setImageDrawable(drawable);
    }

    public void setButtonText(String msg) {
        mRetryBtn.setText(msg);
    }

    public void setButtonText(int resId) {
        mRetryBtn.setText(resId);
    }

    public void setButtonEvent(OnClickListener clickListener) {
        mRetryBtn.setOnClickListener(clickListener);
    }

    public void setEmptyViewBackground(int color) {
        setBackgroundColor(color);
    }

    public void setEmptyViewBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    public void setEmptyViewBackgroundRes(int resId) {
        setBackgroundResource(resId);
    }

    public void setEmptyViewBackground(Bitmap bitmap) {
        setEmptyViewBackground(new BitmapDrawable(getResources(), bitmap));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEmptyIcon = (ImageView) findViewById(R.id.framework_empty_iv);
        mRetryBtn = (Button) findViewById(R.id.framework_empty_retry_load);
    }
}
