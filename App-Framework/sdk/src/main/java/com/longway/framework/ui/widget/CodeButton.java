package com.longway.framework.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.longway.framework.handler.H;
import com.longway.framework.handler.HandlerCallback;
import com.longway.framework.util.ToastUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by longway on 15/12/27.
 */
public class CodeButton extends Button implements HandlerCallback, View.OnClickListener {
    private static final int WAIT = 60;
    private static final int DELAY = 1000;
    private static final int CODE = 0x1;
    private static final String mDefault_text = "获取短信验证码";
    private static final String mWait_text = "%d秒";
    private static final String mHint = "请%d秒后重试";
    private int mLen = WAIT;
    private H mH;
    private AtomicBoolean mFlag = new AtomicBoolean(false);
    private AtomicBoolean mDestroy = new AtomicBoolean(false);
    private OnClickListener mOnclickListener;
    private ICode mICode;

    public boolean isRunning() {
        return mLen < WAIT;
    }

    public int waitTime() {
        return mLen;
    }

    public interface ICode {
        void remain(int len);
    }

    public void setICode(ICode iCode) {
        this.mICode = iCode;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.mOnclickListener = l;
    }

    public CodeButton(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    private void init(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        mH = new H(this);
        super.setOnClickListener(this);
    }

    public CodeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public CodeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CodeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void onStart() {
        reset();
        mH.sendEmptyMessageDelayed(CODE, DELAY);
    }

    private void onDestroy() {
        mH.clear();
        mFlag.set(false);
        mDestroy.set(true);

    }

    public void reset() {
        setText(mDefault_text);
        mLen = WAIT;
        mFlag.set(true);
        mDestroy.set(false);
    }

    @Override
    public void dispatchMessage(Message msg) {
        switch (msg.what) {
            case CODE:
                mLen--;
                if (mLen <= 0) {
                    setText(mDefault_text);
                    mFlag.set(false);
                } else {
                    setText(String.format(mWait_text, mLen));
                    mH.sendEmptyMessageDelayed(CODE, DELAY);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (null == v) {
            return;
        }
        if (mOnclickListener != null && !mFlag.get() && !mDestroy.get()) {
            mOnclickListener.onClick(v);
        } else {
            if (mICode != null) {
                mICode.remain(mLen);
            } else {
                ToastUtils.showToast(getContext(), String.format(mHint, mLen), true);
            }
        }
    }
}
