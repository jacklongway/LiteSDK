package com.longway.framework.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by longway on 16/4/11.
 * Email:longway1991117@sina.com
 * 本地广播最好用这个类实现
 * 1.应用内广播
 * 2.性能提升
 * 3.避免其他进程发送恶意intent
 */
public class LocalBroadCast {
    private static Object[] mLock = new Object[0];

    private static volatile LocalBroadCast sLocalBroadCast;

    private LocalBroadcastManager mLocalBroadcastManager;

    private LocalBroadCast(Context context) {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public static LocalBroadCast getInstance(Context appContext) {
        if (sLocalBroadCast == null) {
            synchronized (mLock) {
                if (sLocalBroadCast == null) {
                    sLocalBroadCast = new LocalBroadCast(appContext);
                }
            }
        }
        return sLocalBroadCast;
    }

    /**
     * 注册广播
     *
     * @param receiver
     * @param filter
     */
    public void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        mLocalBroadcastManager.registerReceiver(receiver, filter);
    }

    /**
     * 注销广播
     *
     * @param receiver
     */
    public void unregisterReceiver(BroadcastReceiver receiver) {
        mLocalBroadcastManager.unregisterReceiver(receiver);
    }

    /**
     * 异步发送广播 handler
     *
     * @param intent
     * @return
     */
    public boolean sendBroadcast(Intent intent) {
        return mLocalBroadcastManager.sendBroadcast(intent);
    }

    /**
     * 同步发送广播 执行广播队列
     *
     * @param intent
     */
    public void sendBroadcastSync(Intent intent) {
        mLocalBroadcastManager.sendBroadcastSync(intent);
    }
}
