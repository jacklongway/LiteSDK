package com.longway.framework.core.common;

/**
 * Created by longway on 16/6/6.
 * Email:longway1991117@sina.com
 */

import android.content.Context;

/**
 * 组建管理调度
 */
public class FrameworkDispatcher {
    private static volatile FrameworkDispatcher sFrameworkDispatcher;

    private FrameworkDispatcher() {

    }

    public static FrameworkDispatcher getInstance() {
        if (sFrameworkDispatcher == null) {
            synchronized (FrameworkDispatcher.class) {
                if (sFrameworkDispatcher == null) {
                    sFrameworkDispatcher = new FrameworkDispatcher();
                }
            }
        }
        return sFrameworkDispatcher;
    }

    public void init(Context context) {
        BusinessComponentManager.getInstance(context).init();
    }
}
