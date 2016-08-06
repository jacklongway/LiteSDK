package com.longway.framework.version;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.longway.framework.util.AppUtils;
import com.longway.framework.util.IOUtils;
import com.longway.framework.util.IntentUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by longway on 16/8/6.
 * Email:longway1991117@sina.com
 */

public class DownloadService extends IntentService {
    private static final String TAG = DownloadService.class.getSimpleName();
    private static final String URL = "url";
    private static final String DIR_NAME = "dirName";
    private static final String BASE_NAME = "baseName";
    private static final AtomicInteger ID = new AtomicInteger(1);
    private static int ICON;
    private int mNotificationId;
    private RequestParams mRequestParams;

    private int generateNotificationId() {
        mNotificationId = ID.incrementAndGet();
        return mNotificationId;
    }

    static class RequestParams {
        String url;
        String dirName;
        String baseName;

    }

    public DownloadService() {
        super(TAG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ICON = AppUtils.getAppIconResId(base);
    }

    public static ComponentName startDownloadService(Context context, String url, String dirName, String baseName) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(URL, url);
        intent.putExtra(DIR_NAME, dirName);
        intent.putExtra(BASE_NAME, baseName);
        try {
            context.startService(intent);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private boolean checkParamsPass(Intent intent) {
        if (intent == null) {
            return false;
        }
        if (!intent.hasExtra(URL)) {
            return false;
        }
        String url;
        url = intent.getStringExtra(URL);
        // verify url
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (!intent.hasExtra(DIR_NAME)) {
            return false;
        }
        String dirName;
        dirName = intent.getStringExtra(DIR_NAME);
        if (TextUtils.isEmpty(dirName)) {
            return false;
        }
        if (!intent.hasExtra(BASE_NAME)) {
            return false;
        }
        String baseName;
        baseName = intent.getStringExtra(BASE_NAME);
        if (TextUtils.isEmpty(baseName)) {
            return false;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.url = url;
        requestParams.dirName = dirName;
        requestParams.baseName = baseName;
        this.mRequestParams = requestParams;
        cancelNotification(mNotificationId);
        generateNotificationId();
        return true;
    }

    private void cancelNotification(int id) {
        try {
            NotificationUtil
                    .cancelNotification(this, id);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelNotification(mNotificationId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!checkParamsPass(intent)) {
            return;
        }
        File file = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        RandomAccessFile f = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(mRequestParams.url);
            file = new File(new File(mRequestParams.dirName, mRequestParams.baseName).getAbsolutePath());
            f = new RandomAccessFile(file, "rw");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                get.addHeader("RANGE", "bytes=" + f.length() + "-");
                f.skipBytes((int) file.length());
            }
            HttpResponse response = client.execute(get);
            is = response.getEntity().getContent();
            bis = new BufferedInputStream(is);
            int t = (int) (file.length() + response.getEntity()
                    .getContentLength());
            byte[] buffer = new byte[8 * 1024];
            int len;
            int total = 0;

            while ((len = bis.read(buffer)) != -1) {
                f.write(buffer, 0, len);
                total += len;
                NotificationUtil.sendNotification(this,
                        file.getName(), (total * 1.0 / t) * 100
                                + "%", t, total, ICON,
                        mNotificationId);
            }
            IntentUtils.installApk(this, file);
            return;

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is, f, bis);
            NotificationUtil
                    .cancelNotification(this, mNotificationId);
        }
        NotificationUtil.sendNotification(this, file.getName(), VersionConstants.getMessage(VersionConstants.DOWNLOAD_FAIL),
                ICON, mNotificationId);

    }
}
