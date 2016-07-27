package com.longway.framework.util;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.longway.framework.ui.activities.BaseActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.longway.framework.util.FileUtils.getFileExtension;
import static com.longway.framework.util.FileUtils.sendBroadcastToFileScanner;

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

    public static boolean hasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
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
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
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
     * 处理拍照
     *
     * @param context
     * @param file
     * @return
     */
    public static File handleCameraFile(Context context, File file) {
        long start = System.currentTimeMillis();
        BufferedOutputStream outputStream = null;
        if (!FileUtils.isValidateFile(file.getAbsolutePath())) {
            FileUtils.deleteFile(file, false);
            return null;
        }
        try {
            final int degress = BitmapHelper.readPictureDegree(file
                    .getAbsolutePath());
            if (degress != 0) {
                int[] imageSize = BitmapDecoder.getWidthAndheight(file
                        .getAbsolutePath());
                Bitmap bitmap = BitmapDecoder.getBitmap(imageSize[0],
                        imageSize[1], file.getAbsolutePath());
                Bitmap bmp = BitmapDecoder.rotaingImageView(degress, bitmap);
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
                File f = FileUtils.getExternalCacheDir(context, context.getApplicationInfo().className + "/tmp");
                if (!f.exists()) {
                    f.mkdirs();
                }
                File dest = new File(f, System.currentTimeMillis() + "." + getFileExtension(file.getAbsolutePath()));
                outputStream = new BufferedOutputStream(new FileOutputStream(
                        dest), 8192);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                file.delete();
                file = null;
                file = f;
                if (bmp != null && !bmp.isRecycled()) {
                    bmp.recycle();
                    bmp = null;
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (OutOfMemoryError error) {

        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                outputStream = null;
            }
        }
        long end = System.currentTimeMillis();
        Log.i("time", (end - start) + "");
        // 发送扫描指令
        sendBroadcastToFileScanner(context, file);
        return file;
    }

    public static final String CONTENT = "content";
    public static final String FILE = "file";

    /**
     * 从contentprovider 获取文件名
     *
     * @param ctx
     * @param uri
     * @return
     */
    public static String getFilePath(Context ctx, Uri uri) {
        if (ctx == null || uri == null) {
            return null;
        }
        ContentResolver resolver = ctx.getContentResolver();
        if (resolver == null) {
            return null;
        }

        String scheme = uri.getScheme();
        // from provider
        if (CONTENT.equalsIgnoreCase(scheme)) {
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = resolver.query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                try {
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.MediaColumns.DATA));
                    return path;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                        cursor = null;
                    }
                }
            }
            // from file://
        } else if (FILE.equalsIgnoreCase(scheme)) {
            return uri.getPath();
        }
        return null;
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
