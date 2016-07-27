package com.longway.framework.handler;

import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by longway on 16/6/19.
 * Email:longway1991117@sina.com
 */

public class AsyncLoop implements LoopFactory {
    private static final String TAG = AsyncLoop.class.getSimpleName();

    public AsyncLoop() {
    }

    @Override
    public Looper getLooper() {
        HandlerThread asyncLoopFactory = new HandlerThread(TAG);
        asyncLoopFactory.start();
        Looper looper = asyncLoopFactory.getLooper();
        return looper;
    }
}
