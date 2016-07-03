package com.longway.framework.exception.crash;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.util.PrintWriterPrinter;

import com.longway.elabels.BuildConfig;
import com.longway.framework.util.FileUtils;
import com.longway.framework.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/*********************************
 * Created by longway on 16/5/17 下午3:22.
 * packageName:com.longway.framework.exception.crash
 * projectName:MPTPAPP
 * Email:longway1991117@sina.com
 ********************************/
public class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "AppCrashHandler";
    private static final String CRASH_LOG_DIR = "%/crash";
    private static final String CRASH_LOG_FILE_NAME = "%.txt";
    private static final String UNKNOWN = "unknown";
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
    private Context mContext;
    private ICrashConfiguration mICrashConfiguration;

    public AppCrashHandler(Context context, ICrashConfiguration crashConfiguration) {
        mContext = context;
        this.mICrashConfiguration = crashConfiguration;
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void setICrashConfiguration(ICrashConfiguration iCrashConfiguration) {
        this.mICrashConfiguration = iCrashConfiguration;
    }

    private void dump(CrashInfo crashInfo) {
        try {
            File dir = FileUtils.getExternalCacheDir(mContext, String.format(CRASH_LOG_DIR, mContext.getPackageName()));
            File file = new File(dir, String.format(CRASH_LOG_FILE_NAME, System.currentTimeMillis()));
            OutputStream outputStream = new FileOutputStream(file);
            crashInfo.dump(new PrintWriterPrinter(new PrintWriter(outputStream)), "");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        final CrashInfo crashInfo = buildCrashInfo(thread, ex);
        dump(crashInfo);
        ICrashConfiguration crashConfiguration = mICrashConfiguration;
        if (crashConfiguration != null) {
            crashConfiguration.uploadCrashInfoToServer(crashInfo);
            if (crashConfiguration.onSelfCompleteHandlerCrash()) {
                return;
            }

            if (crashConfiguration.restartApp()) {
                scheduleRestartApp(crashConfiguration.restartDelayTime());
            } else {
                if (!executeDefaultHandler(thread, ex)) {
                    killProcess();
                }
            }
        } else {
            if (!executeDefaultHandler(thread, ex)) {
                killProcess();
            }
        }
    }

    private CrashInfo buildCrashInfo(Thread thread, Throwable ex) {
        CrashInfo crashInfo = new CrashInfo();
        crashInfo.setmThreadName(thread != null ? thread.getName() : UNKNOWN);
        crashInfo.setmErrorMessage(ex != null ? ex.getLocalizedMessage() : UNKNOWN);
        ApplicationInfo applicationInfo = mContext.getApplicationInfo();
        crashInfo.setmAppName(applicationInfo.name);
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(applicationInfo.packageName, 0);
            crashInfo.setmAppVersionCode(packageInfo.versionName);
            crashInfo.setmAppVersionCode(String.valueOf(packageInfo.versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        crashInfo.setmCpuCoreCount(String.valueOf(Runtime.getRuntime().availableProcessors()));
        crashInfo.setmProduct(Build.PRODUCT);
        crashInfo.setmDeviceId(Build.ID);
        crashInfo.setmDeviceName(Build.DEVICE);
        crashInfo.setmOsName(System.getProperty("os.name", UNKNOWN));
        crashInfo.setmOsVersion(System.getProperty("os.version", UNKNOWN));
        crashInfo.setmArch(System.getProperty("os.arch", UNKNOWN));
        crashInfo.setmSDKVersion(String.valueOf(Build.VERSION.SDK_INT));
        crashInfo.setMemorySize(String.valueOf(Runtime.getRuntime().maxMemory()));
        if (BuildConfig.DEBUG) {
            LogUtils.d(TAG + ":" + crashInfo.toString());
        }
        return crashInfo;
    }

    public void scheduleRestartApp(int delay) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, pendingIntent);
    }

    public void killProcess() {
        Process.killProcess(Process.myPid());
    }

    private boolean executeDefaultHandler(Thread thread, Throwable throwable) {
        if (mDefaultUncaughtExceptionHandler != null) {
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
            return true;
        }
        return false;
    }
}
