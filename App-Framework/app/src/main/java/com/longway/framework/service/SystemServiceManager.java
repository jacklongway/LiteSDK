package com.longway.framework.service;

import android.app.Application;
import android.content.Context;

import com.longway.elabels.BuildConfig;
import com.longway.framework.util.LogUtils;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by longway on 16/1/9.
 * 系统服务管理
 */
public class SystemServiceManager {
    private static volatile SystemServiceManager sInstance = null;
    private static final String CONTEXT = "context";
    private Map<String, SoftReference<Object>> mSystemService;

    private SystemServiceManager() {
        mSystemService = new HashMap<>();
    }

    private Context getInnerCacheContext() {
        Context context = null;
        SoftReference<Object> object = mSystemService.get(CONTEXT);
        if (object != null) {
            context = (Context) object.get();
        }
        return context;
    }

    public static Context getAppContext() {
        return getInstance().getContext();
    }

    public Context getContext() {
        Context context = getInnerCacheContext();
        if (null != context) {
            return context;
        } else {
            synchronized (SystemServiceManager.class) {
                context = getInnerCacheContext();
                if (null != context) {
                    return context;
                }
                try {
                    Class clz = Class.forName("android.app.ActivityThread");
                    Method method = clz.getDeclaredMethod("currentApplication");
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    Application application = (Application) method.invoke(null);
                    if (null != application) {
                        context = application.getBaseContext();
                        if (context == null) {
                            context = application;
                        }
                        mSystemService.put(CONTEXT, new SoftReference<Object>(context));
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return context;
    }

    public static SystemServiceManager getInstance() {
        if (sInstance == null) {
            synchronized (SystemServiceManager.class) {
                if (sInstance == null) {
                    sInstance = new SystemServiceManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取服务
     *
     * @param name 最好使用系统Context 中定义的常量
     * @return
     */
    public synchronized Object getService(String name) {
        Object service = null;
        if (mSystemService.containsKey(name)) {
            SoftReference sr = mSystemService.get(name);
            if (sr != null) {
                service = sr.get();
            }
        }
        if (service != null) {
            return service;
        }
        try {
            service = getContext().getSystemService(name);
            if (service != null) {
                SoftReference softReference = new SoftReference(service);
                mSystemService.put(name, softReference);
                if (BuildConfig.DEBUG) {
                    LogUtils.d("put service[name,value]->[" + name + "," + service.hashCode() + "]");
                }
            }
            if (BuildConfig.DEBUG) {
                printCurrentServiceSize();
            }
        } catch (Throwable throwable) {
            // no should occur
        }
        return service; //may null
    }

    /**
     * 打印当前缓存的服务个数
     */
    public void printCurrentServiceSize() {
        LogUtils.d("SystemServiceManager" + ".getCurrentServiceSize->" + mSystemService.size());
    }
}
