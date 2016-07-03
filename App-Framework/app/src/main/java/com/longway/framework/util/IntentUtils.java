package com.longway.framework.util;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.longway.framework.ui.activities.BaseActivity;

import java.io.File;

public class IntentUtils {
    private static final String INTENT_INSTANCE_EXCEPTION = "IntentUtil not instance";
    public static final int PICK_REQUEST_CODE = 0x0;
    public static final int TAKE_REQUEST_CODE = 0x1;
    public static final int PICK_CONTACTS_PHONE_NUMBER_REQUEST_CODE = 0x2;
    public static final int PICK_CONTACTS_NAME_REQUEST_CODE = 0x3;
    public static final int CROP = 0x4;

    private IntentUtils() {
        throw new UnsupportedOperationException(INTENT_INSTANCE_EXCEPTION);
    }

    /**
     * 从相册选择图片
     *
     * @param activity
     * @param requestCode
     */
    public static void pickImage(BaseActivity activity, int requestCode) {
        if (activity == null) {
            throw new NullPointerException("activity not null");
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 拍照
     *
     * @param activity
     * @param requestCode
     * @param outPath
     */
    public static void takePicture(BaseActivity activity, int requestCode,
                                   File outPath) {
        if (activity == null) {
            throw new NullPointerException("activity not null");
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPath));
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取联系人
     *
     * @param activity
     * @param requestCode
     */
    public static void pickContact(BaseActivity activity, int requestCode) {
        if (activity == null) {
            throw new NullPointerException("activity not null");
        }
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 裁剪图片
     *
     * @param activity
     * @param uri
     * @param tempFile
     */
    public void cropPhoto(BaseActivity activity, Uri uri, File tempFile) {
        if (activity == null) {
            throw new NullPointerException("activity not null");
        }
        if (uri == null) {
            throw new NullPointerException("uri not null");
        }
        if (tempFile == null) {
            throw new NullPointerException("tempFile not null");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");//动作-裁剪
        intent.setDataAndType(uri, "image/*");//类型
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));//
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);// 裁剪比例
        intent.putExtra("aspectY", 1);// 裁剪比例
        intent.putExtra("outputX", 150);// 输出大小
        intent.putExtra("outputY", 150);// 裁剪比例后输出比例
        intent.putExtra("scale", true);// 缩放
        intent.putExtra("scaleUpIfNeeded", true);// 如果小于要求输出大小，就放大
        intent.putExtra("return-data", false);// 不返回缩略图
        intent.putExtra("noFaceDetection", true);// 关闭人脸识别
        activity.startActivityForResult(intent, CROP);
    }

    /**
     * 打开网络设置界面
     */
    public static void openNetworkSetting(BaseActivity activity) {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        activity.startActivity(intent);
    }
}
