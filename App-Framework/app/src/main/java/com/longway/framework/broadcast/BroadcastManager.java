package com.longway.framework.broadcast;

import android.content.IntentFilter;

import com.longway.framework.AndroidApplication;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by longway on 2015/11/6.
 * 广播中心
 */
public class BroadcastManager {

    private volatile static BroadcastManager sInstance = null;
    private Map<String, BroadcastObject> mReceivers;

    private BroadcastManager() {
        mReceivers = new HashMap<String, BroadcastObject>();
    }

    public static BroadcastManager getInstance() {
        if (sInstance == null) {
            synchronized (BroadcastManager.class) {
                if (sInstance == null) {
                    sInstance = new BroadcastManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 注册广播
     *
     * @param cb 注册广播的回调函数类
     * @param action 广播的action
     */
    public static synchronized void regBroadcast(BroadcastCallback cb, String action) {
        if (cb == null) {
            return;
        }
        BroadcastManager manager = getInstance();
        BroadcastObject receiver = manager.mReceivers.get(action);

        if (receiver == null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(action);
            intentFilter.setPriority(Integer.MAX_VALUE);

            receiver = new BroadcastObject();
            AndroidApplication.getInstance().registerReceiver(receiver, intentFilter);

            //加到内存中
            manager.mReceivers.put(action, receiver);
        }


        //遍历cb是否已经存在
        Iterator<SoftReference<BroadcastCallback>> ite = receiver.getBroadcastCallbackList().iterator();
        while (ite.hasNext()) {
            BroadcastCallback callback = ite.next().get();
            if (cb == callback) {
                return;
            }
            if (callback == null) {
                ite.remove();
            }
        }
        receiver.addBroadcast(new SoftReference<BroadcastCallback>(cb));
    }

    /**
     * 注销广播
     * @param callback 广播回调的函数
     */
    public static synchronized void unRegBroadcast(BroadcastCallback callback) {
        BroadcastManager manager = getInstance();
        Iterator<Map.Entry<String, BroadcastObject>> mapIte = manager.mReceivers.entrySet().iterator();
        while(mapIte.hasNext()) {
            BroadcastObject entryValue = mapIte.next().getValue();
            List<SoftReference<BroadcastCallback>> list = entryValue.getBroadcastCallbackList();

            // 遍历查找当前队列下该对象是否存在，存在则删除。并且顺便清理被回收的对象
            Iterator<SoftReference<BroadcastCallback>> listIte = list.iterator();
            while(listIte.hasNext()) {
                BroadcastCallback cb = listIte.next().get();
                if (cb == null || cb == callback) {
                    listIte.remove();         // 这里找到了，但不return，是因为还想去看看其他队列里是否含有为已经被回收的广播对象
                }
            }

            //注销到最后一个
            if (entryValue.getBroadcastCallbackList().size() <= 0) {
                AndroidApplication.getInstance().unregisterReceiver(entryValue);
                mapIte.remove();
            }
        }
    }

}
