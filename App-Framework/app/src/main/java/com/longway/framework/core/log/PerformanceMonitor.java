package com.longway.framework.core.log;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.util.Log;

import com.longway.framework.util.DateUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by longway on 16/6/29.
 * Email:longway1991117@sina.com
 */

public class PerformanceMonitor {
    private static final String TAG = PerformanceMonitor.class.getSimpleName();

    private PerformanceMonitor() {

    }

    private static String importance(int importance) {
        switch (importance) {
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND:
                return "background";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND:
                return "foreground";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE:
                return "gone";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE:
                return "perceptible";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY:
                return "empty";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND_SERVICE:
                return "foreground_service";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE:
                return "service";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_TOP_SLEEPING:
                return "top_sleeping";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE:
                return "visible";
            case ActivityManager.RunningAppProcessInfo.REASON_PROVIDER_IN_USE:
                return "provider_in_use";
            case ActivityManager.RunningAppProcessInfo.REASON_SERVICE_IN_USE:
                return "service_in_use";
        }
        return "unknown";
    }

    private static final DecimalFormat sDF = new DecimalFormat("0.0");
    private static final int K = 1024; // 1K
    private static final int M = (int) Math.pow(K, 2); // 1M
    private static final int G = (int) Math.pow(K, 3); // 1G
    private static final long T = (long) Math.pow(K, 4); // 1T

    private static String getSize(long b) {
        if (b < K) {
            return b + "B";
        }
        if (b < M) {
            double d = b * 1.0 / K;
            return sDF.format(d).concat("K");
        }
        if (b < G) {
            double d = b * 1.0 / M;
            return sDF.format(d).concat("M");
        }
        if (b < T) {
            double d = b * 1.0 / G;
            return sDF.format(d).concat("G");
        }
        Log.e(TAG, String.valueOf(b));
        return b + "B";
    }


    public static String dumpProcessMemoryInfo(Context context, int pid, String event, int trimLevel) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return "ActivityManagerService died.";
        }
        List<ActivityManager.RunningAppProcessInfo> runningAppProcess = activityManager.getRunningAppProcesses();
        StringBuilder sb = new StringBuilder(128);
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcess) {
            if (runningAppProcessInfo.pid == pid) {
                sb.append("dump start time:" + DateUtils.format(System.currentTimeMillis(),DateUtils.DUMP_YMD_HMS)).append("\n");
                sb.append("dump event:" + event).append("\n");
                sb.append("pid:").append(pid).append("\n");
                sb.append("processName:").append(runningAppProcessInfo.processName).append("\n");
                sb.append("uid:").append(runningAppProcessInfo.uid).append("\n");
                sb.append("lastTrimLevel:").append(level(runningAppProcessInfo.lastTrimLevel == 0 ? trimLevel : runningAppProcessInfo.lastTrimLevel)).append("\n");
                sb.append("lru:").append(runningAppProcessInfo.lru).append("\n");
                sb.append("importance:").append(importance(runningAppProcessInfo.importance)).append("\n");
                ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                activityManager.getMemoryInfo(mi);
                sb.append("availMem:").append(getSize(mi.availMem)).append("\n");
                sb.append("lowMemory:").append(mi.lowMemory).append("\n");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sb.append("totalMem:").append(getSize(mi.totalMem)).append("\n");
                } else {
                    sb.append("maxHeadMemory:").append(getSize(Runtime.getRuntime().maxMemory())).append("\n");
                }
                sb.append("threshold:").append(getSize(mi.threshold)).append("\n");
                int appMemory = activityManager.getMemoryClass();
                sb.append("applicationMemory:").append(appMemory).append("M").append("\n");
                sb.append("applicationMaxMemory:").append(activityManager.getLargeMemoryClass()).append("M").append("\n");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    sb.append("isLowRamDevice:").append(activityManager.isLowRamDevice()).append("\n");
                }
                Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{pid});
                int totalPss = memoryInfo[0].getTotalPss();
                sb.append("TotalPss:").append(getSize(totalPss * 1024)).append("\n");
                sb.append("appMemoryUsePercent:").append(totalPss * 100 / 1024 / appMemory).append("%").append("\n");
                sb.append("dump end time:" + DateUtils.format(System.currentTimeMillis(),DateUtils.DUMP_YMD_HMS)).append("\n");
                sb.append("-------------------------------------------------------------").append("\n");
                break;
            }
        }
        if (sb.length() == 0) {
            sb.append("pid=" + pid + " have died.");
        }
        return sb.toString();
    }

    private static String level(int level) {
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                return "trim_memory_background";
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                return "trim_memory_complete";
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                return "trim_memory_moderate";
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                return "trim_memory_running_critical";
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                return "trim_memory_running_low";
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                return "trim_memory_running_moderate";
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                return "trim_memory_ui_hidden";
        }
        return "unknown";
    }
}
