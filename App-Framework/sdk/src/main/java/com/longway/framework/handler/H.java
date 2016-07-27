package com.longway.framework.handler;


import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by longway on 15/8/8.
 * 通用消息处理
 */
public class H extends Handler {

    private HandlerCallback mHandlerCallback;

    public H(Looper looper, HandlerCallback handlerCallback) {
        super(looper);
        this.mHandlerCallback = handlerCallback;
    }

    public H(Looper looper) {
        super(looper);
    }

    public H(Looper looper, Callback callback) {
        super(looper, callback);
    }

    public H(Looper looper, Callback callback, HandlerCallback handlerCallback) {
        super(looper, callback);
        this.mHandlerCallback = handlerCallback;
    }

    public H(Callback callback) {
        super(Looper.getMainLooper(), callback);
    }

    public H(Callback callback, HandlerCallback handlerCallback) {
        super(Looper.getMainLooper(), callback);
        this.mHandlerCallback = handlerCallback;
    }

    public H(HandlerCallback handlerCallback) {
        super(Looper.getMainLooper());
        this.mHandlerCallback = handlerCallback;
    }

    public H() {
        super(Looper.getMainLooper());
    }

    /**
     * @param loopFactory
     * @param handlerCallback
     * @see AsyncLoop
     */
    public H(LoopFactory loopFactory, HandlerCallback handlerCallback) {
        super(loopFactory.getLooper());
        this.mHandlerCallback = handlerCallback;
    }


    @Override
    public void handleMessage(Message msg) {
        if (mHandlerCallback != null) {
            mHandlerCallback.dispatchMessage(msg);
        } else {
            super.handleMessage(msg);
        }
    }

    public static Message getMsg(int what, Object object, int arg1, int arg2) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = object;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        return msg;
    }

    /**
     * 销毁handler
     */
    public void clear() {
        removeCallbacksAndMessages(null);
        Looper looper = getLooper();
        // 非主线程退出队列
        if (looper != null && looper != Looper.getMainLooper()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                looper.quitSafely();
            } else {
                looper.quit();
            }
            looper = null;
        }
        mHandlerCallback = null;
    }
}
