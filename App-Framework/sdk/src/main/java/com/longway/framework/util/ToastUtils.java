package com.longway.framework.util;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.longway.framework.executor.ThreadPoolTask;
import com.longway.framework.executor.UiThreadManager;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by longway on 16/6/19.
 * Email:longway1991117@sina.com
 */

public class ToastUtils {
    private static final String RAW = "raw";
    private static final String CUSTOM = "custom";
    private static final ConcurrentHashMap<String, Toast> TOAST_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    @Override
    protected void finalize() throws Throwable {
        try {
            TOAST_CONCURRENT_HASH_MAP.clear();
        } finally {
            super.finalize();
        }
    }

    private static Toast createToast(Context context, String msg, int duration) {
        if (msg == null) {
            throw new NullPointerException("msg==null");
        }
        final ConcurrentHashMap<String, Toast> map = TOAST_CONCURRENT_HASH_MAP;
        Toast toast;
        if (!map.containsKey(RAW)) {
            toast = Toast.makeText(Utils.convertContext(context), msg, duration);
            map.put(RAW, toast);
        } else {
            toast = map.get(RAW);
            toast.setText(msg);
        }
        return toast;
    }

    private static Toast createToast(Context context, View contentView, int duration) {
        if (contentView == null) {
            throw new NullPointerException("contentView==null");
        }
        final ConcurrentHashMap<String, Toast> map = TOAST_CONCURRENT_HASH_MAP;
        Toast toast;
        if (!map.containsKey(CUSTOM)) {
            toast = new Toast(Utils.convertContext(context));
            map.put(CUSTOM, toast);
        } else {
            toast = map.get(CUSTOM);
        }
        toast.setDuration(duration);
        toast.setView(contentView);
        return toast;
    }

    public static void showToast(final Context context, final String msg, final boolean longDuration) {
        if (Utils.isMainThread()) {
            createToast(context, msg, longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        } else {
            UiThreadManager.postRunnableToFront(new ThreadPoolTask<String>(msg) {
                @Override
                public void doTask(String parameter) {
                    createToast(context, parameter, longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                }
            }.setParameter(msg));
        }
    }


    public static void showToast(final Context context, final View contentView, final boolean longDuration) {
        if (Utils.isMainThread()) {
            createToast(context, contentView, longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        } else {
            UiThreadManager.postRunnableToFront(new ThreadPoolTask<View>(contentView.toString()) {
                @Override
                public void doTask(View parameter) {
                    createToast(context, contentView, longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                }
            }.setParameter(contentView));
        }
    }
}
