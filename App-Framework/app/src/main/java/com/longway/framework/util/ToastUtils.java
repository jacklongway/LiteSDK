package com.longway.framework.util;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.longway.framework.executor.ThreadPoolTask;
import com.longway.framework.executor.UiThreadManager;

/**
 * Created by longway on 16/6/19.
 * Email:longway1991117@sina.com
 */

public class ToastUtils {
    private ToastUtils() {

    }

    private static Toast createToast(Context context, String msg, int duration) {
        if (msg == null) {
            throw new NullPointerException("msg must be not null");
        }
        Toast toast = Toast.makeText(Utils.convertContext(context), msg, duration);
        return toast;
    }

    private static Toast createToast(Context context, View contentView, int duration) {
        if (contentView == null) {
            throw new NullPointerException("contentView must be not null");
        }
        Toast toast = new Toast(Utils.convertContext(context));
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
