package com.longway.framework.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.stetho.common.LogUtil;
import com.longway.framework.AndroidApplication;
import com.longway.framework.core.network.base.netstate.NetStatusManager;
import com.longway.framework.service.SystemServiceManager;


/**
 * 手机信息工具类，提供获取手机信息的方法
 * Created by Administrator on 2015/11/4 0004.
 */
public class PhoneInfoUtils {

    private static String mImei = "";

    private static final String INVALID_IMEI = "000000000000000";//无效IMEI

    private static final String EXCEPTION_IMEI = "000000000000001";//异常IMEI

    private static final String TAG_WARN = "PhoneInfoUtils";

    private static int mNetworkStatus;//网络状态

//    private static NetworkStatusReceiver sNetworkStatusReceiver;//网络状态改变广播接收者

    private PhoneInfoUtils(){}

    /**
     * 通过api获取IMSI，需要添加权限<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @return
     */
    public static String getIMSI() {
        String imsi = null;
        try {
//            TelephonyManager telephonyManager = (TelephonyManager) QkApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            TelephonyManager telephonyManager = (TelephonyManager) SystemServiceManager.getInstance().getService(Context.TELEPHONY_SERVICE);
            imsi = telephonyManager.getSubscriberId();
        } catch (Throwable t) {
            Log.w(TAG_WARN, t.getMessage());
        }
        if (TextUtils.isEmpty(imsi)) {
            imsi = INVALID_IMEI;
        }
        return imsi;
    }

    /**
     * 获取mac地址,需要添加权限<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     *
     * @return
     */
    public static String getMAC() {
//        WifiManager wifiManager = (WifiManager) QkApp.getInstance().getSystemService(Context.WIFI_SERVICE);
        WifiManager wifiManager = (WifiManager) SystemServiceManager.getInstance().getService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String mac = null;
        if (info != null) {
            mac = info.getMacAddress();
        }
        return mac;
    }

    /**
     * 获取ICCID码，即SIM卡序列号，需要添加权限<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @return ICCID码
     */
    public static String getICCID() {
        String number = null;
        try {
//            TelephonyManager telephonyManager = (TelephonyManager) QkApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            TelephonyManager telephonyManager = (TelephonyManager) SystemServiceManager.getInstance().getService(Context.TELEPHONY_SERVICE);
            number = telephonyManager.getSimSerialNumber();
        } catch (Exception e) {
            Log.w(TAG_WARN, e.getMessage());
        }
        return number;
    }

    /**
     * 获取AndroidId
     *
     * @return AndroidId
     */
    public static String getAndroidId() {
        return Settings.Secure.getString(AndroidApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取android api版本
     *
     * @return
     */
    public static String getApiLevel() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机的品牌
     *
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取当前网络状态
     *
     * @return
     */
    public static void getNetworkState() {

        //主动获取网络状态
        mNetworkStatus = NetStatusManager.getInstance().getStatus();

    }

    /**
     * 注册广播
     *
     * @param receiver
     */
    private static void registerBoradcastReceiver(BroadcastReceiver receiver) {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        AndroidApplication.getInstance().registerReceiver(receiver, intentFilter);
    }

    /**
     * 注销广播
     *
     * @param receiver
     */
    private static void unregisterBoradcastReceiver(BroadcastReceiver receiver) {
        if (receiver != null) {
            AndroidApplication.getInstance().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public static String getAppVersion() {
        String appVersion = null;
        PackageManager packageManager = AndroidApplication.getInstance().getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(AndroidApplication.getInstance().getPackageName(), 0);
            appVersion = String.valueOf(info.versionCode);
            return appVersion;
        } catch (Throwable e) {
            LogUtil.d("PhoneInfoUtils.getAppVersion()", e.toString());
        }
        return appVersion;
    }

    /**
     * 获取IMEI，需要添加权限<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @return
     */
    public static String getIMEI() {
        if (!TextUtils.isEmpty(mImei)) {
            return mImei;
        }

        synchronized (mImei) {
            if (!TextUtils.isEmpty(mImei)) {
                return mImei;
            }

            String imei4Api = getImeiWithApi();
            if(!TextUtils.isEmpty(imei4Api)){
                mImei = imei4Api;
            }

            if(!TextUtils.isEmpty(mImei) && isValidIMEI(mImei)){
                return mImei;
            }

            return INVALID_IMEI;

        }
    }

    /**
     * 系统api获取imei
     * @return
     */
    private static String getImeiWithApi() {
        String imei = INVALID_IMEI;
        try {
//            TelephonyManager telephonyManager = (TelephonyManager) QkApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            TelephonyManager telephonyManager = (TelephonyManager) SystemServiceManager.getInstance().getService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            imei = EXCEPTION_IMEI;
            Log.w(TAG_WARN, e.getMessage());
        }
        return imei;
    }


    /**
     * 是否是有效的IMEI
     *
     * @return
     */
    private static boolean isValidIMEI(String imei) {
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        // IMEI
        if (imei.length() < 14) {
            return false;
        }

        if (imei.startsWith("0000")) {
            return false;
        }

        return true;
    }
}
