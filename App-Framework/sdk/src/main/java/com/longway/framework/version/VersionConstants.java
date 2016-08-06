package com.longway.framework.version;

import android.util.SparseArray;

/**
 * Created by longway on 16/8/6.
 * Email:longway1991117@sina.com
 */

public class VersionConstants {
    public static final int CHECK_FAIL = 1;
    public static final int DOWNLOAD_FAIL = 2;
    private static final SparseArray<String> MSG_TABLE;

    static {
        MSG_TABLE = new SparseArray<>();
        MSG_TABLE.put(CHECK_FAIL, "check lastest version fail");
        MSG_TABLE.put(DOWNLOAD_FAIL, "download application fail");
    }

    public static String getMessage(int code) {
        return MSG_TABLE.get(code);
    }
}
