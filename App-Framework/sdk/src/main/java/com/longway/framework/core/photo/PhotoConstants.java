package com.longway.framework.core.photo;

import android.util.SparseArray;

/**
 * Created by longway on 16/7/29.
 * Email:longway1991117@sina.com
 */

public class PhotoConstants {
    public static final int NO_CAMERA = 0;
    public static final int NO_CAMERA_APP = 1;
    public static final int LAUNCH_CAMERA_APP_FAIL = 2;
    public static final int TAKE_PHOTO_FAIL = 3;
    public static final int NO_ALBUM_APP = 4;
    public static final int LAUNCH_ALBUM_APP_FAIL = 5;
    public static final int PICK_PHOTO_FAIL = 6;
    public static final int NO_CROP_APP = 7;
    public static final int LAUNCH_CROP_APP_FAIL = 8;
    public static final int CROP_FAIL = 9;
    public static final SparseArray<String> MESSAGE_TABLE;

    static {
        MESSAGE_TABLE = new SparseArray<>();
        MESSAGE_TABLE.put(NO_CAMERA, "not camera");
        MESSAGE_TABLE.put(NO_CAMERA_APP, "not camera application");
        MESSAGE_TABLE.put(LAUNCH_CAMERA_APP_FAIL, "launch camera fail");
        MESSAGE_TABLE.put(TAKE_PHOTO_FAIL, "take photo fail");
        MESSAGE_TABLE.put(NO_ALBUM_APP, "not album application");
        MESSAGE_TABLE.put(LAUNCH_ALBUM_APP_FAIL, "launch album fail");
        MESSAGE_TABLE.put(PICK_PHOTO_FAIL, "pick photo fail");
        MESSAGE_TABLE.put(NO_CROP_APP, "no crop application");
        MESSAGE_TABLE.put(LAUNCH_CROP_APP_FAIL, "launch crop fail");
        MESSAGE_TABLE.put(CROP_FAIL, "crop fail");
    }

    public static String getMessage(int code) {
        return MESSAGE_TABLE.get(code);
    }
}
