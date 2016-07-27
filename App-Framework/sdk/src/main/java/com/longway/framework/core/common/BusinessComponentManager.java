package com.longway.framework.core.common;

import android.app.Application;
import android.content.Context;

import com.longway.framework.core.imageLoader.ImageLoaderManager;
import com.longway.framework.core.network.NetworkManager;
import com.longway.framework.util.LogUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by longway on 16/6/6.
 * Email:longway1991117@sina.com
 */

class BusinessComponentManager implements IBusinessComponent {
    private static final String TAG = BusinessComponentManager.class.getSimpleName();
    private Map<String, AbsManager> mBusinessComponent = new ConcurrentHashMap<>();
    private Context mContext;

    public void init() {
        addBusinessComponent(BusinessComponentTable.IMAGE_LOADER, ImageLoaderManager.getInstance());
        addBusinessComponent(BusinessComponentTable.NETWORK, NetworkManager.getInstance(mContext));
        printBusinessComponent();
    }

    private BusinessComponentManager() {

    }

    @Override
    public AbsManager addBusinessComponent(String key, AbsManager absManager) {
        return mBusinessComponent.put(key, absManager);
    }

    @Override
    public AbsManager removeBusinessComponent(String key) {
        return mBusinessComponent.remove(key);
    }

    @Override
    public AbsManager getBusinessComponent(String key) {
        return mBusinessComponent.get(key);
    }

    private static final class InnerBusinessComponentManager {
        private static final BusinessComponentManager INSTANCE = new BusinessComponentManager();
    }

    public static BusinessComponentManager getInstance(Context context) {
        BusinessComponentManager businessComponentManager = InnerBusinessComponentManager.INSTANCE;
        if (!(context instanceof Application)) {
            businessComponentManager.mContext = context.getApplicationContext();
        } else {
            businessComponentManager.mContext = context;
        }
        return businessComponentManager;
    }

    public void printBusinessComponent() {
        Set<Map.Entry<String, AbsManager>> entries = mBusinessComponent.entrySet();
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, AbsManager> entry : entries) {
            stringBuilder.append(entry.getKey()).append(":").append(entry.getValue().toString()).append("\n");
        }
        LogUtils.d(TAG + ">>" + stringBuilder.toString());
    }

}
