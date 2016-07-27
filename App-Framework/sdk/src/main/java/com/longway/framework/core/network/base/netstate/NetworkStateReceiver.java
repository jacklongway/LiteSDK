/*
 * Copyright (C) 2013  WhiteCat 白猫 (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.longway.framework.core.network.base.netstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.longway.framework.util.NetWorkUtil;

import java.util.ArrayList;


/**
 * 网络状态监视器
 * 观察者模式
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    private static boolean networkAvailable = false;
    private static NetWorkUtil.netType netType;
    private static ArrayList<NetChangeObserver> netChangeObserverArrayList = new ArrayList<NetChangeObserver>();
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public final static String ALIAS_ANDROID_NET_CHANGE_ACTION = "alias.net.conn.CONNECTIVITY_CHANGE";
    private volatile static BroadcastReceiver receiver;

    private static BroadcastReceiver getReceiver() {
        if (receiver == null) {
            synchronized (BroadcastReceiver.class) {
                if (receiver == null) {
                    receiver = new NetworkStateReceiver();
                }
            }
        }
        return receiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        receiver = NetworkStateReceiver.this;
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)
                || intent.getAction().equalsIgnoreCase(
                ALIAS_ANDROID_NET_CHANGE_ACTION)) {
            if (!NetWorkUtil.isNetworkAvailable(context)) {
                networkAvailable = false;
                netType = NetWorkUtil.netType.noneNet;
            } else {
                netType = NetWorkUtil.getAPNType(context);
                networkAvailable = true;
            }
            notifyObserver();
        }
    }

    /**
     * 注册网络状态广播
     *
     * @param mContext
     */

    public static void registerNetworkStateReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ALIAS_ANDROID_NET_CHANGE_ACTION);
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        mContext.getApplicationContext()
                .registerReceiver(getReceiver(), filter);
    }

    /**
     * 检查网络状态
     *
     * @param mContext
     */
    public static void checkNetworkState(Context mContext) {
        Intent intent = new Intent();
        intent.setAction(ALIAS_ANDROID_NET_CHANGE_ACTION);
        mContext.sendBroadcast(intent);
    }

    /**
     * 注销网络状态广播
     *
     * @param mContext
     */
    public static void unRegisterNetworkStateReceiver(Context mContext) {
        if (receiver != null) {
            try {
                mContext.unregisterReceiver(receiver);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    /**
     * 获取当前网络状态，true为网络连接成功，否则网络连接失败
     *
     * @return
     */
    public static Boolean isNetworkAvailable() {
        return networkAvailable;
    }

    public static NetWorkUtil.netType getAPNType() {
        return netType;
    }

    private void notifyObserver() {

        for (int i = 0; i < netChangeObserverArrayList.size(); i++) {
            NetChangeObserver observer = netChangeObserverArrayList.get(i);
            if (observer != null) {
                if (isNetworkAvailable()) {
                    observer.onConnect(netType);
                } else {
                    observer.onDisConnect();
                }
            }
        }

    }

    /**
     * 注册网络连接观察者
     * <p/>
     * observerKey
     */
    public static void registerObserver(NetChangeObserver observer) {
        if (netChangeObserverArrayList == null) {
            netChangeObserverArrayList = new ArrayList<NetChangeObserver>();
        }
        if (!netChangeObserverArrayList.contains(observer)) {
            netChangeObserverArrayList.add(observer);
        }
    }

    /**
     * 注销网络连接观察者
     * <p/>
     * observerKey
     */
    public static void removeRegisterObserver(NetChangeObserver observer) {
        if (netChangeObserverArrayList != null && observer != null && netChangeObserverArrayList.contains(observer)) {
            netChangeObserverArrayList.remove(observer);
        }
    }

}