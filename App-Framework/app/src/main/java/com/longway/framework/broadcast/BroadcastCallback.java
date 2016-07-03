package com.longway.framework.broadcast;

import android.content.Context;
import android.content.Intent;

/**
 * Created by longway on 2015/11/16.
 */
public abstract class BroadcastCallback {
    protected boolean mRunOnMainThread;
    // 名字，用于debug跟踪使用
    private String mName;

    /**
     * 如果isRunOnUiThread=true 运行在主线程，
     * 反之运行在非主线程,
     * 默认为false运行在非主线程中。
     * @param name 广播回类的名字，用于debug时候使用
     * @param runOnMainThread 是否在主线程上运行
     */
    public BroadcastCallback(String name, boolean runOnMainThread) {
        mName = name;
        mRunOnMainThread = runOnMainThread;
    }
    boolean getRunOnMainThread() {
        return mRunOnMainThread;
    }

    String getName() {
        return mName;
    }


    public abstract void onReceive(Context context, Intent intent);

}
