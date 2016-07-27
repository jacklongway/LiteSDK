package com.longway.framework.report;

import android.content.Context;

import com.longway.framework.AndroidApplication;
import com.longway.framework.core.network.base.netstate.NetStatusManager;
import com.longway.framework.util.FileUtils;
import com.longway.framework.util.PhoneInfoUtils;
import com.longway.framework.util.SPUtils;


/**
 * ANR Reporter
 */
public class ReportManager {

    private final static String SP_NAME = "photoInfo";
    private final static String REPORT_DIR = AndroidApplication.getInstance().getFilesDir().getAbsolutePath();
    private final static String REPORT_FILENAME = "ReportCache";

    /**
     * 获得用户信息和版本信息
     *
     * @return PhoneInfo
     */
    public static PhoneInfo getPhoneInfo() {
        PhoneInfo userInfo = new PhoneInfo();
        userInfo.setImei(PhoneInfoUtils.getIMEI());
        userInfo.setImsi(PhoneInfoUtils.getIMSI());
        userInfo.setMac(PhoneInfoUtils.getMAC());
        userInfo.setIccid(PhoneInfoUtils.getICCID());
        userInfo.setAndroidId(PhoneInfoUtils.getAndroidId());
        userInfo.setApiLevel(PhoneInfoUtils.getApiLevel());
        userInfo.setModel(PhoneInfoUtils.getModel());
        userInfo.setBrand(PhoneInfoUtils.getBrand());
//        userInfo.setNetworkState();
//        userInfo.setProductID();产品id
        userInfo.setAppVersion(PhoneInfoUtils.getAppVersion());

        save2SharedPreferences(userInfo);
        save2File(userInfo);
        return userInfo;
    }

    /**
     * 保存信息到SharedPreferences中
     *
     * @param info
     */
    private static void save2SharedPreferences(PhoneInfo info) {
        Context context = AndroidApplication.getInstance();
        SPUtils.put(SP_NAME, context, "imei", info.getImei());
        SPUtils.put(SP_NAME, context, "imsi", info.getImsi());
        SPUtils.put(SP_NAME, context, "mac", info.getMac());
        SPUtils.put(SP_NAME, context, "iccid", info.getIccid());
        SPUtils.put(SP_NAME, context, "android id", info.getAndroidId());
        SPUtils.put(SP_NAME, context, "api Level", info.getApiLevel());
        SPUtils.put(SP_NAME, context, "model", info.getModel());
        SPUtils.put(SP_NAME, context, "brand", info.getBrand());
        SPUtils.put(SP_NAME, context, "networkState", String.valueOf(NetStatusManager.getInstance().getStatus()));
        SPUtils.put(SP_NAME, context, "productID", info.getProductID());
        SPUtils.put(SP_NAME, context, "appVersion", info.getAppVersion());
    }

    // 保存手机信息到缓存文件中
    private static void save2File(final PhoneInfo info) {
        FileUtils.asyncSave2File(REPORT_DIR, REPORT_FILENAME, info.toString());
    }

}
