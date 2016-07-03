package com.longway.framework.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.longway.framework.executor.ThreadPoolManager;
import com.longway.framework.executor.ThreadPoolTask;
import com.longway.framework.report.RecordAnrTask;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by longway on 2015/11/9 0009.
 * 广播接收者类的基类
 */
class BroadcastObject extends BroadcastReceiver {
    private static final String TAG = "BroadcastObject";

    private List<SoftReference<BroadcastCallback>> mList;

    /**
     * 传入参数，是否运行在主线程中
     */
    BroadcastObject() {
        mList = new ArrayList<SoftReference<BroadcastCallback>>();
    }

    List<SoftReference<BroadcastCallback>> getBroadcastCallbackList() {
        return mList;
    }

    /**
     * 添加广播回调
     *
     * @param cb 广播回调
     */
    void addBroadcast(SoftReference<BroadcastCallback> cb) {
        mList.add(cb);
    }


    @Override
    public void onReceive(final Context context, final Intent intent) {

        Iterator<SoftReference<BroadcastCallback>> ite = mList.iterator();
        while (ite.hasNext()) {
            // 去掉那些已经被回收的广播对象
            final BroadcastCallback callback = ite.next().get();
            if (callback == null) {
                ite.remove();
                continue;
            }
            if (callback.getRunOnMainThread()) {
                // 运行在主线程上
                long startTime = System.currentTimeMillis();
                callback.onReceive(context,intent);
                RecordAnrTask.asyncRecordBroadcast(callback.getName(), System.currentTimeMillis() - startTime);
            } else {
                // 运行在非主线程
                ThreadPoolTask<Object> task = new ThreadPoolTask<Object>(intent.getAction()) {
                    @Override
                    public void doTask(Object parameter) {
                        callback.onReceive(context, intent);
                    }
                };
                ThreadPoolManager.getInstance().postShortTask(task);
            }

        }
    }


}
