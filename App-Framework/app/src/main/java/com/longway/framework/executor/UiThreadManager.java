package com.longway.framework.executor;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.PopupWindow;

import com.longway.framework.handler.H;


/**
 * Created by longway on 15/12/19.
 * ui线程管理器
 */
public class UiThreadManager {
    private static volatile UiThreadManager sUiThreadManager;
    private transient H mH;

    private UiThreadManager() {
        // 创建主线程loop
        mH = new H();
    }

    public static UiThreadManager getInstance() {
        if (sUiThreadManager == null) {
            synchronized (UiThreadManager.class) {
                if (sUiThreadManager == null) {
                    sUiThreadManager = new UiThreadManager();
                }
            }
        }
        return sUiThreadManager;
    }

    public static void postRunnable(ThreadPoolTask runnable) {
        getInstance().mH.post(runnable);
    }

    public static void postRunnable(ThreadPoolTask runnable, long delayTime) {
        getInstance().mH.postDelayed(runnable, delayTime);
    }

    public static void postRunnableToFront(ThreadPoolTask runnable) {
        getInstance().mH.postAtFrontOfQueue(runnable);
    }

    public static void postRunnableAtTime(ThreadPoolTask runnable, long time) {
        getInstance().mH.postAtTime(runnable, time);
    }

    public static void postRunnable(Activity activity, ThreadPoolTask runnable) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.runOnUiThread(runnable);
    }

    public static void postRunnable(View view, ThreadPoolTask runnable) {
        if (view == null || !view.isActivated()) {
            return;
        }
        Handler handler = view.getHandler();
        if (handler != null) {
            handler.post(runnable);
        } else {
            postRunnable(runnable);
        }
    }

    public static void postRunnable(Fragment fragment, ThreadPoolTask runnable) {
        if (fragment == null || fragment.isRemoving() || fragment.isDetached()) {
            return;
        }
        postRunnable(fragment.getActivity(), runnable);
    }

    public static void postRunnable(Dialog dialog, ThreadPoolTask runnable) {
        if (dialog == null) {
            return;
        }
        postRunnable(dialog.getOwnerActivity(), runnable);
    }

    public static void postRunnable(PopupWindow popupWindow, ThreadPoolTask runnbale) {
        if (popupWindow == null) {
            return;
        }
        postRunnable((Activity) popupWindow.getContentView().getContext(), runnbale);
    }

    public void destroy() {
        mH.clear();
        sUiThreadManager = null;
    }
}
