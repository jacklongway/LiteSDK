package com.longway.framework.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Process;

import com.longway.framework.service.SystemServiceManager;

import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

/**
 * 跟App相关的辅助类
 *
 * @author longway
 */
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取当前进程名
     *
     * @return
     */
    public static String getProcessName() {
        try {
            SystemServiceManager systemServiceManager = SystemServiceManager.getInstance();
            if (systemServiceManager == null) {
                LogUtils.d("systemServiceManager null");
                return "";
            }
            android.app.ActivityManager activityManager = (ActivityManager) systemServiceManager.getService(Context.ACTIVITY_SERVICE);
            if (activityManager == null) {
                LogUtils.d("activityManager null");
                return "";
            }
            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();
            if (runningAppProcessInfos != null && !runningAppProcessInfos.isEmpty()) {
                int pid = Process.myPid();
                if (runningAppProcessInfos instanceof RandomAccess) {
                    int size = runningAppProcessInfos.size();
                    for (int i = 0; i < size; i++) {
                        android.app.ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcessInfos.get(i);
                        if (pid == runningAppProcessInfo.pid) {
                            return runningAppProcessInfo.processName;
                        }

                    }
                } else {
                    Iterator<ActivityManager.RunningAppProcessInfo> iterator = runningAppProcessInfos.iterator();
                    while (iterator.hasNext()) {
                        android.app.ActivityManager.RunningAppProcessInfo rap = iterator.next();
                        if (pid == rap.pid) {
                            return rap.processName;
                        }
                    }
                }
            }
        } catch (RuntimeException ex) {
            // ignore
            ex.printStackTrace();
        }
        return "";
    }


}
