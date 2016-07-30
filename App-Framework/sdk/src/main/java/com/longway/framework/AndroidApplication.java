package com.longway.framework;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.longway.elabels.BuildConfig;
import com.longway.framework.core.imageLoader.GlideManager;
import com.longway.framework.exception.crash.AppCrashHandler;
import com.longway.framework.exception.crash.CrashInfo;
import com.longway.framework.exception.crash.ICrashConfiguration;
import com.longway.framework.util.AppUtils;
import com.longway.framework.util.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longway on 15/7/26.
 */
public class AndroidApplication extends Application implements ICrashConfiguration, Application.ActivityLifecycleCallbacks {

    private List<IMemoryInfo> sMemInfoList = new ArrayList<>();
    public static String sMainProcessName;
    private static AndroidApplication sInstance;
    private GlideManager mGlide;
    private AppCrashHandler mAppCrashHandler;
    private RefWatcher mRefWatcher;
    private List<ActivityLifecycleCallbacks> mCallbacks = new ArrayList<>();

    public void addActivityLifecycleCallback(ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        mCallbacks.add(activityLifecycleCallbacks);
    }

    public void removeActivityLifecycleCallback(ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        mCallbacks.remove(activityLifecycleCallbacks);
    }

    public static AndroidApplication getInstance() {
        return sInstance;
    }

    public AppCrashHandler getAppCrashHandler() {
        return mAppCrashHandler;
    }

    public static RefWatcher getRefWatcher(Context context) {
        AndroidApplication application = (AndroidApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    public GlideManager getGlideInstance() {
        return mGlide;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 获取包名，主进程为包名
        sMainProcessName = base.getPackageName();
        LogUtils.d(sMainProcessName);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        sInstance = this;
    }

    @Override
    public boolean restartApp() {
        return true;
    }

    @Override
    public void uploadCrashInfoToServer(CrashInfo crashInfo) {

    }

    @Override
    public int restartDelayTime() {
        return 2000;
    }

    @Override
    public boolean onSelfCompleteHandlerCrash() {
        return false;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityCreated(activity,
                        savedInstanceState);
            }
        }
    }
    private Object[] collectActivityLifecycleCallbacks() {
        Object[] callbacks = null;
        synchronized (mCallbacks) {
            if (mCallbacks.size() > 0) {
                callbacks = mCallbacks.toArray();
            }
        }
        return callbacks;
    }
    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleCallbacks)callbacks[i]).onActivitySaveInstanceState(activity,
                        outState);
            }
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleCallbacks)callbacks[i]).onActivityDestroyed(activity);
            }
        }
    }

    /**
     * ComponentCallbacks2
     */
    public interface IMemoryInfo {
        void goodTimeToReleaseMemory(int level);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
//don't compare with == as intermediate stages also can be reported, always better to check >= or <=
        if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
            try {
                // Activity at the front will get earliest than activity at the
                // back
                for (int i = sMemInfoList.size() - 1; i >= 0; i--) {
                    try {
                        sMemInfoList.get(i).goodTimeToReleaseMemory(level);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param implementor interested listening in memory events
     */
    public void registerMemoryListener(IMemoryInfo implementor) {
        if (implementor == null) {
            return; // ? throw
        }
        synchronized (sMemInfoList) {
            if (sMemInfoList.contains(implementor)) {
                return; // ? throw
            }
            sMemInfoList.add(implementor);
        }
    }

    public void unregisterMemoryListener(IMemoryInfo implementor) {
        if (implementor == null) {
            return; // ? throw
        }
        synchronized (sMemInfoList) {
            int index = sMemInfoList.indexOf(implementor);
            if (index == -1) {
                return; // ? throw
            }
            sMemInfoList.remove(index);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 避免多进程初始化多次
        sInstance = this;
        String processName = AppUtils.getProcessName();
        LogUtils.d("processName:" + processName);
        if (TextUtils.equals(processName, sMainProcessName)) {
            LogUtils.e(sMainProcessName.concat(" process init..."));
            registerActivityLifecycleCallbacks(this);
            // 开始调试侦查器
            if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()
                        .detectDiskWrites()
                        .detectNetwork()
                        .penaltyLog()
                        .build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects()
                        .penaltyLog()
                        .penaltyDeath()
                        .build());
            }
            if (BuildConfig.DEBUG) {
                mRefWatcher = LeakCanary.install(this);
            }
            if (BuildConfig.DEBUG) {
                BlockCanary.install(this, new AppBlockCanaryContext()).start();
            }
            mGlide = GlideManager.getInstance(getApplicationContext());
            // chrome://inspect 调试
            if (BuildConfig.DEBUG) {
                Stetho.initialize(Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
            }
            mAppCrashHandler = new AppCrashHandler(this, this);
            Thread.setDefaultUncaughtExceptionHandler(mAppCrashHandler);
        } else {
            LogUtils.d(processName.concat("process init..."));
        }
    }
}
