package com.longway.framework.util;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 打开或关闭软键盘
 */
public class KeyBoardUtils {
    public static boolean openKeyBoard(View view) {
        return openOrCloseKeyBoard(view, true);
    }

    public static boolean closeKeyBoard(View view) {
        return openOrCloseKeyBoard(view, false);
    }

    public static boolean openOrCloseKeyBoard(View view, boolean open) {
        if (view == null) {
            return false;
        }
        IBinder iBinder = view.getWindowToken();
        if (iBinder == null || !iBinder.isBinderAlive()) {
            return false;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return false;
        }
        try {
            if (open) {
                return inputMethodManager.showSoftInput(view, 0);
            } else {
                return inputMethodManager.hideSoftInputFromWindow(iBinder, 0);
            }
        } catch (Exception ex) {

        }
        return false;
    }
}
