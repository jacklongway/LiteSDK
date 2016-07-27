package com.longway.framework.util;

import android.os.Build;
import android.text.TextUtils;

import com.longway.elabels.BuildConfig;
import com.longway.framework.AndroidApplication;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Http请求的工具类
 */
public class HttpUtils {
    public static String getRequestParams(HashMap<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            int i = 0;
            int size = keys.size() - 1;
            for (String key : keys) {
                builder.append(key).append("=").append(params.get(key));
                if (i != size) {
                    builder.append("&");
                }
                i++;
            }
        }
        return builder.toString();
    }

    public static interface HttpCallback {
        public void onStart(String url);

        public void onError(String url, Exception ex);

        public void onProgressUpdate(String url, long currentSize, long total);

        public void onComplete(String url, byte[] data);

        public void onReadUpdate(String url, long currentSize, long total);
    }

    public static final void download(String url, HashMap<String, String> params, HashMap<String, String> header, HttpCallback callback) {
        InputStream inputStream = null;
        HttpURLConnection conn = null;
        ByteArrayOutputStream outputStream = null;
        if (callback != null) {
            callback.onStart(url);
        }
        try {
            StringBuilder stringBuilder = new StringBuilder(url);
            String body = getRequestParams(params);
            if (!TextUtils.isEmpty(body)) {
                stringBuilder.append(body);
            }
            URL uri = new URL(stringBuilder.toString());
            conn = (HttpURLConnection) uri.openConnection();
            addHeader(conn, header);
            // 设置通用的请求属性
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setReadTimeout(0);
            conn.setConnectTimeout(0);
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            LogUtils.d("responseCode:" + responseCode + ",responseMessage:" + conn.getResponseMessage());
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Map<String, List<String>> headers = conn.getHeaderFields();
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    List<String> values = headers.get(key);
                    if (values != null && !values.isEmpty()) {
                        for (String value : values) {
                            LogUtils.d("[" + key + "," + value + "]");
                        }
                        if ("Set-Cookie".equals(key)) {
                            SPUtils.put(AndroidApplication.sMainProcessName, AndroidApplication.getInstance(), "Cookie", values.get(0));
                        }
                    }
                }
                inputStream = conn.getInputStream();
                byte[] buffer = new byte[8192];
                int len;
                outputStream = new ByteArrayOutputStream();
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();
                if (callback != null) {
                    byte[] result = outputStream.toByteArray();
                    callback.onComplete(url, result);
                }
            } else {
                if (callback != null) {
                    callback.onError(url, new Exception(conn.getResponseMessage()));
                }
            }
        } catch (Exception ex) {
            if (callback != null) {
                callback.onError(url, new Exception("connect error..."));
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }

    }

    public static final boolean download(String url, String desk, HttpCallback downloadCallback, HashMap<String, String> header) {
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        HttpURLConnection conn = null;
        if (downloadCallback != null) {
            downloadCallback.onStart(url);
        }
        try {
            File file = new File(desk);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            long seek = file.length();
            URL uri = new URL(url);
            conn = (HttpURLConnection) uri.openConnection();
            // 设置请求token
            // 设置通用的请求属性
            addHeader(conn, header);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Range", "bytes=" + seek + "-");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(0);
            conn.setConnectTimeout(0);
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            LogUtils.d("responseMsg:" + conn.getResponseMessage() + ",Method:" + conn.getRequestMethod());
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL) {
                inputStream = conn.getInputStream();
                long totalLen = conn.getContentLength() + seek;
                long downloadLen = 0;
                byte[] buffer = new byte[8192];
                int len;
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(seek);
                while ((len = inputStream.read(buffer)) != -1) {
                    downloadLen += len;
                    if (downloadCallback != null) {
                        downloadCallback.onProgressUpdate(url, downloadLen, totalLen);
                    }
                    randomAccessFile.write(buffer, 0, len);
                }
                if (downloadCallback != null) {
                    downloadCallback.onComplete(url, new byte[]{});
                }
                return true;
            } else {
                if (downloadCallback != null) {
                    downloadCallback.onError(url, new Exception(conn.getResponseMessage()));
                }
            }
        } catch (Exception ex) {
            if (downloadCallback != null) {
                downloadCallback.onError(url, new Exception("connect error..."));
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(randomAccessFile);
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return false;
    }


    public static final boolean download(String url, String desk, HttpCallback downloadCallback) {
        return download(url, desk, downloadCallback, null);
    }

    private static void addHeader(HttpURLConnection connection, HashMap<String, String> header) {
        if (header == null || header.isEmpty()) {
            return;
        }
        Set<String> keys = header.keySet();
        for (String key : keys) {
            connection.setRequestProperty(key, header.get(key));
        }
    }

    public static final void upload(String uri, File file, HashMap<String, String> header, HttpCallback callBack) {
        HttpURLConnection conn = null;
        OutputStream out = null;
        BufferedInputStream bis = null;
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        if (callBack != null) {
            callBack.onStart(uri);
        }
        String BOUNDARY = "---------------------------" + System.currentTimeMillis(); //boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            addHeader(conn, header);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            out = new DataOutputStream(conn.getOutputStream());
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
            String filename = file.getName();
            strBuf.append("Content-Disposition: form-data; name=\"" + filename + "\"; filename=\"" + filename + "\"\r\n");
            strBuf.append("Content-Type:image/png" + "\r\n\r\n");
            out.write(strBuf.toString().getBytes());
            int len;
            byte[] buf = new byte[8192];
            long size = file.length();
            int currentSize = 0;
            bis = new BufferedInputStream(new FileInputStream(file), 8192);
            while ((len = bis.read(buf)) != -1) {
                out.write(buf, 0, len);
                currentSize += len;
                if (callBack != null) {
                    callBack.onProgressUpdate(uri, currentSize, size);
                }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
                size = conn.getContentLength();
                currentSize = 0;
                byteArrayOutputStream = new ByteArrayOutputStream();
                while ((len = inputStream.read(buf)) != -1) {
                    byteArrayOutputStream.write(buf, 0, len);
                    currentSize += len;
                    if (callBack != null) {
                        callBack.onReadUpdate(uri, currentSize, size);
                    }
                }
                byteArrayOutputStream.flush();
                if (callBack != null) {
                    callBack.onComplete(uri, byteArrayOutputStream.toByteArray());
                }
            } else {
                if (callBack != null) {
                    callBack.onError(uri, new Exception(conn.getResponseMessage()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (callBack != null) {
                callBack.onError(uri, ex);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(byteArrayOutputStream);
        }
    }

    static {
        disableConnectionReuseIfNecessary();
    }

    private static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    public static void httpDeleteRequest(String url, HashMap<String, String> params, HashMap<String, String> header, HttpCallback httpCallback) {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(url);
            String body = getRequestParams(params);
            if (!TextUtils.isEmpty(body)) {
                stringBuilder.append("?").append(body);
            }
            if (BuildConfig.DEBUG) {
                LogUtils.d(stringBuilder.toString());
            }
            URL uri = new URL(stringBuilder.toString());
            conn = (HttpURLConnection) uri.openConnection();
            // 设置通用的请求属性
            addHeader(conn, header);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setUseCaches(false);
            // http://bugs.java.com/view_bug.do?bug_id=7157360
            //conn.setDoOutput(true); jdk 8修复了
            conn.setDoInput(true);
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.connect();
            inputStream = conn.getInputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[8192];
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
            if (httpCallback != null) {
                httpCallback.onComplete(url, byteArrayOutputStream.toByteArray());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (httpCallback != null) {
                httpCallback.onError(url, ex);
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(byteArrayOutputStream);
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
    }
}
