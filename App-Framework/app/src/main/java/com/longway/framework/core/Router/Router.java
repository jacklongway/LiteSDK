package com.longway.framework.core.Router;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.longway.framework.ui.activities.BaseActivity;
import com.longway.framework.ui.fragments.BaseFragment;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by longway on 16/7/21.
 * Email:longway1991117@sina.com
 */

public class Router {
    private static final String TAG = Router.class.getSimpleName();
    private static volatile Router sInstance;
    private ConcurrentHashMap<String, RouterParams> mActivities = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, RouterParams> mServices = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Class<? extends BaseFragment>> mFragments = new ConcurrentHashMap<>();

    private Router() {
    }

    public static Router getInstance() {
        if (sInstance == null) {
            synchronized (Router.class) {
                if (sInstance == null) {
                    sInstance = new Router();
                }
            }
        }
        return sInstance;
    }


    public void registerActivity(String key, RouterParams routerParams) {
        mActivities.put(key, routerParams);
    }

    public void unregisterActivity(String key) {
        mActivities.remove(key);
    }

    public void registerService(String key, RouterParams routerParams) {
        mActivities.put(key, routerParams);
    }

    public void unregisterService(String key) {
        mActivities.remove(key);
    }

    public void registerFragment(String key, Class<? extends BaseFragment> klass) {
        mFragments.put(key, klass);
    }


    public void unregisterFragment(String key) {
        mFragments.remove(key);
    }

    public Fragment routerFragment(BaseActivity container, Class<? extends BaseFragment> klass) {
        return routerFragment(container, klass, null);
    }

    public Fragment routerFragment(BaseActivity container, Class<? extends BaseFragment> klass, Bundle params) {
        try {
            return container.addFragment(klass, params);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public Fragment routerFragment(BaseActivity container, String key, Bundle params) {
        Class<? extends BaseFragment> klass = mFragments.get(key);
        try {
            return container.addFragment(klass, params);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public void routerFragment(BaseActivity container, String key) {
        routerFragment(container, key, null);
    }

    public boolean routerActivityExist(String key) {
        return mActivities.containsKey(key);
    }

    public boolean routerServiceExist(String key) {
        return mServices.containsKey(key);
    }

    public boolean routerFragmentExist(String key) {
        return mFragments.containsKey(key);
    }

    public void updateRouterParams(String key, Bundle params) {
        RouterParams routerParams = mActivities.get(key);
        if (routerParams != null) {
            routerParams.mParams = params;
        }
    }

    public void updateRouterActivityFlags(String key, int flag) {
        RouterParams routerParams = mActivities.get(key);
        if (routerParams != null) {
            routerParams.mFlag = flag;
        }
    }

    public void updateRouterActivityData(String key, Uri uri) {
        RouterParams routerParams = mActivities.get(key);
        if (routerParams != null) {
            routerParams.mData = uri;
        }
    }

    public void updateRouterActivityType(String key, String type) {
        RouterParams routerParams = mActivities.get(key);
        if (routerParams != null) {
            routerParams.mType = type;
        }
    }

    public void updateRouterActivityRequestCode(String key, int requestCode) {
        RouterParams routerParams = mActivities.get(key);
        if (routerParams != null) {
            routerParams.mRequestCode = requestCode;
        }
    }

    public ComponentName routerStartService(Context context, RouterParams routerParams) {
        return routerStartService(context, null, routerParams);
    }

    public ComponentName routerStartService(Context context, String key, RouterParams routerParams) {
        if (context == null) {
            return null;
        }
        if (routerParams == null) {
            return null;
        }
        return context.startService(getIntent(context, key, routerParams));
    }

    public ComponentName routerStartService(Context context, String key) {
        return routerStartService(context, mServices.get(key));
    }

    public boolean routerBinderService(Context context, RouterParams routerParams, ServiceConnection serviceConnection) {
        return routerBinderService(context, null, routerParams, serviceConnection);
    }

    public boolean routerBinderService(Context context, String key, ServiceConnection serviceConnection) {
        return routerBinderService(context, key, mServices.get(key), serviceConnection);
    }

    public boolean routerBinderService(Context context, String key, RouterParams routerParams, ServiceConnection serviceConnection) {
        if (context == null) {
            return false;
        }
        if (routerParams == null) {
            return false;
        }
        return context.bindService(getIntent(context, key, routerParams), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public boolean routerActivity(Context context, RouterParams routerParams) {
        return routerActivity(context, routerParams.mKey, routerParams, false);
    }

    public boolean routerActivityForResult(Activity context, RouterParams routerParams) {
        return routerActivity(context, routerParams.mKey, routerParams, true);
    }

    public boolean routerActivityForResult(Activity context, String key) {
        return routerActivity(context, key, mActivities.get(key), true);
    }

    public boolean routerActivity(Context context, String key) {
        return routerActivity(context, key, mActivities.get(key), false);
    }

    private boolean routerActivity(Context context, String key, RouterParams routerParams, boolean needResult) {
        if (context == null) {
            return false;
        }
        if (key == null) {
            return false;
        }
        if (routerParams == null) {
            return false;
        }
        Intent intent = getIntent(context, key, routerParams);
        try {
            if (needResult) {
                ((Activity) context).startActivityForResult(intent, routerParams.mRequestCode);
            } else {
                context.startActivity(intent);
            }
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return false;
    }


    private Intent getIntent(Context context, String key, RouterParams routerParams) {
        Intent intent = new Intent();
        if (routerParams.mAction != null) {
            intent.setAction(routerParams.mAction);
        }
        if (routerParams.mCategory != null) {
            List<String> categories = routerParams.mCategory;
            for (String category : categories) {
                intent.addCategory(category);
            }
        }
        if (routerParams.mClassName != null) {
            intent.setClassName(context, routerParams.mClassName);
        }
        if (routerParams.mClz != null) {
            intent.setClass(context, routerParams.mClz);
        }
        if (routerParams.mData != null) {
            intent.setData(routerParams.mData);
        }
        if (routerParams.mType != null) {
            intent.setType(routerParams.mType);
        }
        if (routerParams.mFlag != -1) {
            intent.setFlags(routerParams.mFlag);
        }
        if (routerParams.mParams != null) {
            String k = routerParams.mKey;
            intent.putExtra(k == null ? key : k, routerParams.mParams);
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    public void printRouterInfo() {
        Iterator<String> iterator = mActivities.keySet().iterator();
        String key;
        while (iterator.hasNext()) {
            key = iterator.next();
            RouterParams routerParams = mActivities.get(key);
            Log.d(TAG, key + "=" + routerParams.toString("activity"));
        }

        iterator = mServices.keySet().iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            RouterParams routerParams = mServices.get(key);
            Log.d(TAG, key + "=" + routerParams.toString("service"));
        }

        iterator = mFragments.keySet().iterator();
        StringBuffer sb = new StringBuffer();
        sb.append("RouterParams{");
        while (iterator.hasNext()) {
            key = iterator.next();
            Class<?> clz = mFragments.get(key);
            sb.append("key=").append(key).append(",className='").append(clz.getName()).append('\'');
        }
        sb.append('}');
        Log.d(TAG, sb.toString());
    }

}
