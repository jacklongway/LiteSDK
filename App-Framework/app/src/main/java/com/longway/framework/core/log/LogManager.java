package com.longway.framework.core.log;

import android.annotation.TargetApi;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Process;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by longway on 16/6/28.
 * Email:longway1991117@sina.com
 */

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class LogManager implements ComponentCallbacks2 {
    private volatile static LogManager sInstance;
    private Context mContext;
    private WeakReference<GCWatcher> mGCWatcher;
    private int mLastTrimLevel = 0;
    private Runnable mGCRunnable = new Runnable() {
        @Override
        public void run() {
            dumpPerformanceInfo(Process.myPid(), LogService.GC, sInstance.mLastTrimLevel);
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            onTrimMemory(ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW);
        } else {
            onTrimMemory(100);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        mLastTrimLevel = level;
        dumpPerformanceInfo(Process.myPid(), LogService.TRIM_MEMORY, level);
    }


    private static final class GCWatcher {
        @Override
        protected void finalize() throws Throwable {
            try {
                sInstance.dumpPerformanceInfo(Process.myPid(), LogService.GC, sInstance.mLastTrimLevel);
                sInstance.mGCWatcher = new WeakReference<GCWatcher>(new GCWatcher());
            } finally {
                super.finalize();
            }
        }
    }

    private LogManager(Context context) {
        this.mContext = context;
        init();
    }

    public void init() {
        mContext.getApplicationContext().registerComponentCallbacks(this);
        try {
            Class<?> clz = Class.forName("com.android.internal.os.BinderInternal");
            Method method = clz.getMethod("addGcWatcher", Runnable.class);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            method.invoke(null, mGCRunnable);
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mGCWatcher = new WeakReference<GCWatcher>(new GCWatcher());
    }


    public static LogManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LogManager.class) {
                sInstance = new LogManager(context);
            }
        }
        return sInstance;
    }

    private void dumpService(int pid, int type, String event, int trimLevel) {
        final Context context = mContext;
        Intent service = new Intent(context, LogService.class);
        service.putExtra(LogService.PROCESS_ID, pid);
        service.putExtra(LogService.DUMP_TYPE, type);
        service.putExtra(LogService.EVENT, event);
        service.putExtra(LogService.TRIM_MEMORY_LEVEL, trimLevel);
        context.startService(service);
    }

    public void dumpPerformanceInfo(int pid, String event, int trimLevel) {
        dumpService(pid, LogService.PERFORMANCE_MEMORY, event, trimLevel);
    }
}
