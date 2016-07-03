package com.longway.framework.util;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.facebook.stetho.common.LogUtil;
import com.longway.framework.AndroidApplication;
import com.longway.framework.executor.ThreadPoolManager;
import com.longway.framework.executor.ThreadPoolTask;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 文件操作类
 * io操作耗时的操作最好在异步任务中进行 在包executor下执行
 */
public class FileUtils {

    /**
     * 判断是否存在sd
     */
    public static boolean sdcardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 验证是否是文件
     *
     * @param path
     * @return
     */
    public static boolean isFileExist(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    /**
     * 验证文件是否是有效的
     *
     * @param path
     * @return
     */
    public static boolean isValidateFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return isFileExist(path) && file.length() > 0;
    }

    /**
     * 获取缓存目录
     *
     * @param context
     * @param dir
     * @return
     */
    public static File getCacheDir(Context context, String dir) {
        File file = getExternalCacheDir(context, dir);
        if (file != null) {
            return file;
        }
        file = getInternalCacheDir(context);
        return file;

    }

    /**
     * 获取外部缓存目录
     *
     * @param context
     * @param dir
     * @return
     */
    public static File getExternalCacheDir(Context context, String dir) {
        File file = null;
        if (context == null || !sdcardAvailable()) {
            return file;
        }
        String dirs = "";
        if (TextUtils.isEmpty(dir)) {
            dirs = context.getPackageName();
        } else {
            dirs = dir;
        }
        file = new File(Environment.getExternalStorageDirectory(), dirs);
        if (!file.exists() && file.mkdirs()) {
        }
        return file;
    }

    /**
     * 获取内部缓存目录
     *
     * @param context
     * @return
     */
    public static File getInternalCacheDir(Context context) {
        File file = null;
        if (context == null) {
            return file;
        }
        file = context.getCacheDir();
        if (file == null) {
            return file;
        }
        file = new File("/data/data/".concat(context.getPackageName().concat(
                "/cache")));
        return file;
    }

    /**
     * 获取文件最新修改时间
     */
    public static long getFileLastModifiedTime(String path) {
        if (isValidateFile(path)) {
            return 0;
        }
        return new File(path).lastModified();

    }


    /**
     * 删除文件 或者 目录
     *
     * @param file            删除的文件
     * @param ifDirDeleteSelf 是否删除目录本身
     * @return
     */
    public static boolean deleteFile(File file, boolean ifDirDeleteSelf) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        int deleteFailCount = 0; // 删除文件失败的次数
        if (file.isDirectory() && file.isAbsolute() && file.exists()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                if (ifDirDeleteSelf) {
                    if (!file.delete()) {
                        // no should occur
                        deleteFailCount++;
                    }
                }
            } else {
                for (File f : files) {
                    deleteFile(f, true);
                }
                if (ifDirDeleteSelf) {
                    if (!file.delete()) {
                        deleteFailCount++;
                    }
                }
            }
        }

        return deleteFailCount > 0;
    }

    /**
     * 判断存储空间是否足
     *
     * @param path
     * @param size
     * @return
     */
    public static boolean HasCapacity(String path, long size) {
        if (TextUtils.isEmpty(path)) {
            throw new NullPointerException("path null");
        }
        StatFs fs = new StatFs(path);
        long avaliableBlocks = fs.getAvailableBlocks();
        long blockSize = fs.getBlockSize();
        return avaliableBlocks * blockSize > size;
    }

    /**
     * 读取文件
     *
     * @param path
     * @return
     */
    public static String read(String path) {
        String result = "";
        if (TextUtils.isEmpty(path)) {
            return result;
        }
        File file = new File(path);
        if (!isValidateFile(path)) {
            return result;
        }
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            int len = -1;
            byte[] buffer = new byte[8192];
            outputStream = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            result = new String(buffer);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException exception) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                inputStream = null;
            }
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
        return result;

    }


    /**
     * 保存数据到指定路径
     *
     * @param path
     * @param data
     * @return
     */
    public static boolean save(String path, byte[] data) {
        if (data == null)
            return false;
        File file = new File(path);
        BufferedOutputStream out = null;
        try {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(data);
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                out = null;
            }
        }
        return false;
    }


    /**
     * 判断是否是图像
     *
     * @param file
     * @return
     */
    public static boolean isImage(String file) {
        String extension = getFileExtension(file);
        return "jpg".equalsIgnoreCase(extension)
                || "png".equalsIgnoreCase(extension)
                || "jpeg".equalsIgnoreCase(extension);
    }

    /**
     * 判断是否是视频
     *
     * @param file
     * @return
     */
    public static boolean isVideo(String file) {
        String extension = getFileExtension(file);
        return "mp4".equalsIgnoreCase(extension)
                || "3gp".equalsIgnoreCase(extension)
                || "avi".equalsIgnoreCase(extension)
                || "flv".equalsIgnoreCase(extension);
    }

    /**
     * 打开文件
     *
     * @param context
     * @param path
     */
    public static void openFile(Context context, String path) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            // 获取文件file的MIME类型

            // 设置intent的data和Type属性。
            File file = new File(path);
            intent.setDataAndType(/* uri */Uri.fromFile(file), getMimetype(path));
            // 跳转
            context.startActivity(intent);
            // Intent.createChooser(intent, "请选择对应的软件打开该附件！");

        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            Toast.makeText(context, "无法打开此文件", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取文件名
     *
     * @param url
     * @return
     */
    public static String getFileNameFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        int last = url.lastIndexOf("/");
        if (last != -1) {
            return url.substring(last + 1);
        }
        return "";
    }


    /**
     * 扫描服务开始扫描file
     *
     * @param context
     * @param file
     */
    public static void sendBroadcastToFileScanner(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

    /**
     * 获取文件的扩展名
     *
     * @param url
     * @return
     */
    public static String getFileExtension(String url) {
        return MimeTypeMap.getFileExtensionFromUrl(url);
    }

    /**
     * 获取文件的类型
     *
     * @param url
     * @return
     */
    public static String getMimetype(String url) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                getFileExtension(url));
    }

    /**
     * 通过类型获取扩展名
     *
     * @param mimetype
     * @return
     */
    public static String getExtensionfromMimetype(String mimetype) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimetype);
    }


    public static final long K = 1024; // 1K
    public static final long B = K * K; // 1M
    public static final long G = K * B; // 1G
    public static final long T = K * G; // 1T


    /**
     * 转换大小到String
     * B,K,M,G,T
     *
     * @param byteSize
     * @return
     */
    public static String calcauteSize(long byteSize) {
        if (byteSize < K) {
            return byteSize + "B";
        }
        DecimalFormat format = new DecimalFormat("0.0");
        if (byteSize >= 0 && byteSize < B) {
            return format.format(byteSize * 1.0f / K) + "K";
        }
        if (byteSize >= B && byteSize < G) {
            return format.format(byteSize * 1.0f / B) + "M";
        }
        if (byteSize >= G && byteSize < T) {
            return format.format(byteSize * 1.0f / G) + "G";
        }
        return format.format(byteSize * 1.0f / T) + "T";
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
                bmp.compress(CompressFormat.JPEG, 100, outputStream);
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

    /**
     * 把信息保存到文件的操作(异步操作）
     *
     * @param dir      文件的目录
     * @param fileName 文件名
     * @param info     写入的内容
     */
    public static void asyncSave2File(final String dir, final String fileName, final String info) {
        final StringBuffer sb = new StringBuffer();
        sb.append("FileUtil");
        sb.append("->");
        sb.append("asyncSave2File()");

        ThreadPoolTask<Object> task = new ThreadPoolTask<Object>(sb.toString()) {
            @Override
            public void doTask(Object parameter) {
                sb.append("|");
                sb.append(info);
                save2File(dir, fileName, info,false);
            }
        };
        ThreadPoolManager.getInstance().postShortTask(task);
    }

    /**
     * 把信息保存到缓存文件的操作
     *
     * @param fileName 文件名
     * @param info     写入的内容
     * @param encrypt  是否加密内容
     */
    public static void saveToCacheFile(final String fileName, final String info, final boolean encrypt) {
        String path = AndroidApplication.getInstance().getCacheDir().getAbsolutePath();
        save2File(path, fileName, info,false);
    }

    /**
     * 把信息保存到文件的操作(同步操作）
     *
     * @param dir      文件的目录
     * @param fileName 文件名
     * @param info     写入的内容
     */
    public static synchronized boolean save2File(final String dir, final String fileName, final String info,boolean append) {
        FileOutputStream fos = null;
        try {
            byte[] infoStream;
            infoStream = info.getBytes();
            //判断父文件夹是否存在，不存在则先创建文件夹
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdirs();
                if (!directory.exists()) {
                    LogUtil.w("FileUtil", "创建文件夹失败！！！");
                    return false;
                }
            }

            // 写入
            File file = new File(dir, fileName);
            fos = new FileOutputStream(file,append);
            fos.write(infoStream);
            fos.flush();
        } catch (Throwable e) {
            LogUtil.d("FileUtil.save2File()", e.toString());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Throwable e) {
                    LogUtil.w("FileUtil.save2File()", e);
                }
            }
        }
        return true;
    }
}
