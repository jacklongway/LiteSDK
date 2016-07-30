package com.longway.framework.util;

import android.app.Activity;
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

import com.longway.framework.core.photo.CropOptions;
import com.longway.framework.ui.activities.BaseActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static com.longway.framework.util.FileUtils.getFileExtension;
import static com.longway.framework.util.FileUtils.sendBroadcastToFileScanner;

public class IntentUtils {
    private static final String INTENT_INSTANCE_EXCEPTION = "IntentUtil not instance";
    public static final int PICK_REQUEST_CODE = 0x0;
    public static final int TAKE_REQUEST_CODE = 0x1;
    public static final int PICK_CONTACTS_PHONE_NUMBER_REQUEST_CODE = 0x2;
    public static final int PICK_CONTACTS_NAME_REQUEST_CODE = 0x3;
    public static final int CROP_REQUEST_CODE = 0x4;

    private IntentUtils() {
        throw new UnsupportedOperationException(INTENT_INSTANCE_EXCEPTION);
    }

    /**
     * 从相册选择图片
     *
     * @param activity
     * @param requestCode
     */
    public static void pickImage(Activity activity, int requestCode) throws Exception {
        if (activity == null) {
            throw new NullPointerException("activity==null");
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    public static boolean hasCamera(Context context) {
        if (context == null) {
            throw new NullPointerException("context==null");
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return false;
    }

    public static boolean hasActivity(Context context, String action) {
        if (context == null) {
            throw new NullPointerException("context==null");
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                Intent intent = new Intent(action);
                return !packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty();
            }
        } catch (RuntimeException e) {

        } catch (Error error) {

        } catch (Throwable throwable) {

        }
        return false;
    }

    /**
     * 拍照
     *
     * @param activity
     * @param requestCode
     * @param outPath
     */
    public static void takePicture(Activity activity, int requestCode,
                                   File outPath) throws Exception {
        if (activity == null) {
            throw new NullPointerException("activity==null");
        }
        if (outPath == null) {
            throw new NullPointerException("outPath==null");
        }
        if (outPath.isDirectory()) {
            throw new IllegalArgumentException("outPath must be isFile");
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
    public static void pickContact(BaseActivity activity, int requestCode) throws Exception {
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
     * @param cropOptions
     */
    public static void cropPhoto(Activity activity, CropOptions cropOptions) throws Exception {
        if (activity == null) {
            throw new NullPointerException("activity==null");
        }
        if (cropOptions == null) {
            throw new NullPointerException("cropOptions==null");
        }
        if (cropOptions.input == null) {
            throw new NullPointerException("uri==null");
        }
        if (cropOptions.output == null) {
            throw new NullPointerException("tempFile==null");
        }
        if (cropOptions.output.isDirectory()) {
            throw new IllegalArgumentException("tempFile must be isFile");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");//动作-裁剪
        intent.setDataAndType(cropOptions.input, "image/*");//类型
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropOptions.output));//
        intent.putExtra("outputFormat", cropOptions.mCompressFormat.toString());//Bitmap.CompressFormat.JPEG.toString()
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", cropOptions.aspectX);// 裁剪比例 1
        intent.putExtra("aspectY", cropOptions.aspectY);// 裁剪比例 1
        intent.putExtra("outputX", cropOptions.outputX);// 输出大小 150
        intent.putExtra("outputY", cropOptions.outputY);// 裁剪比例后输出比例 150
        intent.putExtra("scale", true);// 缩放
        intent.putExtra("scaleUpIfNeeded", true);// 如果小于要求输出大小，就放大
        intent.putExtra("return-data", false);// 不返回缩略图
        intent.putExtra("noFaceDetection", true);// 关闭人脸识别
        activity.startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    /**
     * 处理拍照
     *
     * @param context
     * @param file
     * @return
     */
    public static File handleCameraFile(Context context, File file) {
        BufferedOutputStream outputStream = null;
        if (!FileUtils.isValidateFile(file)) {
            FileUtils.deleteFile(file, false);
            return null;
        }
        try {
            if (!FileUtils.sdcardAvailable()) {
                return file;
            }
            final int degress = BitmapHelper.readPictureDegree(file
                    .getAbsolutePath());
            if (degress != 0) {
                int[] imageSize = BitmapDecoder.getWidthAndheight(file
                        .getAbsolutePath());
                Bitmap bitmap = BitmapDecoder.getBitmap(imageSize[0],
                        imageSize[1], file.getAbsolutePath());
                if (bitmap == null) {
                    return null;
                }
                Bitmap bmp = BitmapDecoder.rotaingImageView(degress, bitmap);
                if (bitmap == null) {
                    return null;
                }
                BitmapHelper.recycleBitmap(bitmap);
                File f = FileUtils.getExternalCacheDir(context, context.getCacheDir() + "/tmp");
                File dest = new File(f, System.currentTimeMillis() + "." + getFileExtension(file.getAbsolutePath()));
                outputStream = new BufferedOutputStream(new FileOutputStream(
                        dest), 8192);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                file.delete();
                file = f;
                BitmapHelper.recycleBitmap(bmp);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (OutOfMemoryError error) {

        } finally {
            IOUtils.closeQuietly(outputStream);
        }
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
        String scheme = uri.getScheme();
        // from provider
        if (CONTENT.equalsIgnoreCase(scheme)) {
            ContentResolver resolver = ctx.getContentResolver();
            if (resolver == null) {
                return null;
            }
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
                    IOUtils.closeQuietly(cursor);
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
    public static void openNetworkSetting(BaseActivity activity) throws RuntimeException, Error {
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
