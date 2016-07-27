package com.longway.framework.core.network.base.netstate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.longway.framework.broadcast.BroadcastCallback;
import com.longway.framework.broadcast.BroadcastManager;
import com.longway.framework.service.SystemServiceManager;

/**
 * 网络状态类
 */
public class NetStatusManager {
    /** 初始化网络状态，还没获取网络状态 */
    public static final int NETWORK_TYPE_INITIALIZE = 0;
    /** wifi网络类型 */
    public static final int NETWORK_TYPE_WIFI = 1;
    /** 2g网络类型 */
    public static final int NETWORK_TYPE_2G = 2;
    /** 3g网络类型 */
    public static final int NETWORK_TYPE_3G = 3;
    /** 4g网络类型  */
    public static final int NETWORK_TYPE_4G = 4;
    /** 断网状态  */
    public static final int NETWORK_TYPE_DISCONNECT = 5;
    /** 未知网络状态 */
    public static final int NETWORK_TYPE_UNKNOWN = 6;

    // 记录网络状态
    private int mNetworkStatus;
    // 单例
    private volatile static NetStatusManager sInstance = null;

    private NetStatusManager() {
        mNetworkStatus = NETWORK_TYPE_INITIALIZE;
    }

    public static NetStatusManager getInstance() {
        if (sInstance == null) {
            synchronized (NetStatusManager.class) {
                if (sInstance == null) {
                    sInstance = new NetStatusManager();
                }
            }
        }

        return sInstance;
    }

    /**
     * 设置当前网络状态
     * @param type 当前网络状态
     */
    public void setStatus(int type) {
        mNetworkStatus = type;
    }

    /**
     * 获取当前网络状态
     * @return 当前网络状态
     */
    public int getStatus() {
        if(mNetworkStatus == NetStatusManager.NETWORK_TYPE_INITIALIZE){
            setStatus(getNetworkStatus());
        }
        BroadcastCallback callback = new BroadcastCallback("Net Connectivity Change Broadcast", false) {
            @Override
            public void onReceive(Context context, Intent intent) {
                int networkStatus = NetStatusManager.getInstance().getNetworkStatus();
                NetStatusManager.getInstance().setStatus(networkStatus);
            }
        };
        BroadcastManager.regBroadcast(callback, ConnectivityManager.CONNECTIVITY_ACTION);
        return mNetworkStatus;
    }

    /**
     * 判断网络是否连接
     * @return 网络是否连接
     */
    public boolean isConnected() {
        ConnectivityManager connectivityManager = getConnectivityManager();
        if(null != connectivityManager) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(null != networkInfo && networkInfo.isConnected()){
                if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断是否wifi连接
     * @return 是否wifi连接
     */
    public boolean isWifi() {
        ConnectivityManager cm = getConnectivityManager();
        if (null == cm) {
            return false;
        }
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * 获取当前网络状态
     * @return 当前网络状态
     */
    public int getNetworkStatus(){
        ConnectivityManager connectivityManager = getConnectivityManager();
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        int networkStatus = NetStatusManager.NETWORK_TYPE_DISCONNECT;
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                networkStatus = NetStatusManager.NETWORK_TYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                int networkType = networkInfo.getSubtype();
                String _strSubTypeName = networkInfo.getSubtypeName();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        networkStatus = NetStatusManager.NETWORK_TYPE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        networkStatus = NetStatusManager.NETWORK_TYPE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        networkStatus = NetStatusManager.NETWORK_TYPE_4G;
                        break;
                    default:
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            networkStatus = NetStatusManager.NETWORK_TYPE_2G;
                        } else {
                            networkStatus = NetStatusManager.NETWORK_TYPE_UNKNOWN;
                        }
                        break;
                }
            }
        }

        return networkStatus;
    }

    // 获取ConnectivityManager
    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) SystemServiceManager.getInstance().getService(Context.CONNECTIVITY_SERVICE);
    }
}
