package com.longway.framework.version;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.longway.framework.executor.ThreadPoolManager;
import com.longway.framework.executor.ThreadPoolTask;
import com.longway.framework.handler.H;
import com.longway.framework.handler.HandlerCallback;
import com.longway.framework.util.AppUtils;
import com.longway.framework.util.LogUtils;
import com.longway.framework.util.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.longway.framework.handler.H.getMsg;

public class VersionChecker implements HandlerCallback {
    private static final String TAG = VersionChecker.class.getSimpleName();
    private static final int START = 1;
    private static final int SUCCESS = 2;
    private static final int ERROR = 3;
    private H mH;
    private Context mCtx;
    private IVersionChecker mIVersionChecker;

    public static VersionChecker get(Context context, IVersionChecker versionChecker) {
        return new VersionChecker(context, versionChecker);
    }

    public VersionChecker(Context context, IVersionChecker versionChecker) {
        this.mCtx = context;
        this.mIVersionChecker = versionChecker;
        mH = new H(this);
    }

    public void checkLastestVersion(final String url, final HashMap<String, String> params, final HashMap<String, String> headers) {
        ThreadPoolManager.getInstance().postLongTask(new ThreadPoolTask<String>("checkLastestVersion") {
            @Override
            public void doTask(String parameter) {
                getLatestVersion(url, params, headers);
            }
        });
    }

    private String covertParams(HashMap<String, String> params) {
        Set<String> keys = params.keySet();
        StringBuilder builder = new StringBuilder();
        for (String key : keys) {
            builder.append(key).append("=").append(params.get(key)).append("&");
        }
        if (builder.toString().contains("&")) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }


    private void getLatestVersion(String uri, HashMap<String, String> params,
                                  HashMap<String, String> headers) {
        mH.sendEmptyMessage(START);
        HttpClient client = new DefaultHttpClient();
        if (params != null) {
            uri = uri.concat("?").concat(covertParams(params));
        }
        HttpGet request = new HttpGet(uri);
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> entry = headers.entrySet();
            for (Map.Entry<String, String> header : entry) {
                request.addHeader(new BasicHeader(header.getKey(), header.getValue()));
            }
        }
        String result;
        try {
            HttpResponse response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == HttpStatus.SC_OK
                    || statusCode == HttpStatus.SC_PARTIAL_CONTENT) {
                result = EntityUtils.toString(response.getEntity());
                mH.sendMessage(getMsg(SUCCESS, result, 0, 0));
                return;
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        mH.sendMessage(H.getMsg(ERROR, VersionConstants.getMessage(VersionConstants.CHECK_FAIL), 0, 0));
    }

    public void downloadFile(final String url,
                             final String dirName, String baseName) {
        DownloadService.startDownloadService(mCtx, url, dirName, baseName);
    }


    public boolean needUpdateApp(String lastestVersion) {
        if (TextUtils.isEmpty(lastestVersion)) {
            return false;
        }
        String currentVersion = AppUtils.getVersionName(mCtx);
        if (TextUtils.isEmpty(currentVersion)) {
            return true;
        }
        return lastestVersion.compareTo(currentVersion) > 0;
    }

    @Override
    public void dispatchMessage(Message msg) {
        if (!Utils.contextIsValidate(mCtx)) {
            return;
        }
        if (mIVersionChecker == null) {
            return;
        }
        LogUtils.d(TAG.concat(":").concat(msg.obj.toString()));
        switch (msg.what) {
            case START:
                mIVersionChecker.onCheckStart();
                break;
            case SUCCESS:
                mIVersionChecker.onCheckSuccess(msg.obj.toString());
                break;
            case ERROR:
                mIVersionChecker.onCheckError(msg.obj.toString());
                break;
            default:
                break;

        }
    }
}
