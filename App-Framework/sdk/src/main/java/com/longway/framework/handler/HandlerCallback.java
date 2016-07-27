package com.longway.framework.handler;

import android.os.Message;

/**
 * Created by longway on 15/8/8.
 * 消息处理回调
 */
public interface HandlerCallback {
     void dispatchMessage(Message msg);
}
