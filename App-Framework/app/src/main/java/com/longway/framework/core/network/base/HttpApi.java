package com.longway.framework.core.network.base;

import android.text.TextUtils;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.body.FileBody;
import com.koushikdutta.async.http.body.JSONArrayBody;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.body.StringBody;
import com.longway.elabels.BuildConfig;
import com.longway.framework.core.network.base.callback.ResponseCallback;
import com.longway.framework.util.HttpUtils;
import com.longway.framework.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Created by longway on 15/7/26.
 */
public class HttpApi {
    public static final AsyncHttpClient asyncHttpClient = AsyncHttpClient.getDefaultInstance();

    public static Future<?> executePostJsonObject(Request request, final ResponseCallback responseCallback) throws JSONException {
        StringBuilder stringBuilder = new StringBuilder(64);
        String params = HttpUtils.getRequestParams(request.getURLParams());
        if (!TextUtils.isEmpty(params)) {
            stringBuilder.append("?").append(params);
        }
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(stringBuilder.toString());
        JSONObject jsonObject;
        if (request.getBodyParams() != null) {
            jsonObject = new JSONObject(request.getBodyParams());
        } else {
            jsonObject = new JSONObject("{}");
        }
        AsyncHttpRequestBody<JSONObject> asyncHttpRequestBody = new JSONObjectBody(jsonObject);
        asyncHttpPost.setBody(asyncHttpRequestBody);
        addHead(request.getHeader(), asyncHttpPost);
        return asyncHttpClient.executeString(asyncHttpPost, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse asyncHttpResponse, String s) {
                if (responseCallback != null) {
                    responseCallback.onCompleted(e, asyncHttpResponse, s);
                }
            }
        });
    }

    public static Future<?> executePostJsonArray(Request request, final ResponseCallback responseCallback) throws JSONException {
        StringBuilder builder = new StringBuilder(request.getURL());
        String params = HttpUtils.getRequestParams(request.getURLParams());
        if (!TextUtils.isEmpty(params)) {
            builder.append("?").append(params);
        }
        if (BuildConfig.DEBUG) {
            LogUtils.d(builder.toString());
        }
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(builder.toString());
        JSONArray jsonArray;
        if (request.getArray() != null) {
            jsonArray = new JSONArray(request.getArray());
        } else {
            jsonArray = new JSONArray("[]");
        }
        AsyncHttpRequestBody<JSONArray> asyncHttpRequestBody = new JSONArrayBody(jsonArray);
        asyncHttpPost.setBody(asyncHttpRequestBody);
        addHead(request.getHeader(), asyncHttpPost);

        return asyncHttpClient.executeString(asyncHttpPost, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse asyncHttpResponse, String s) {
                if (responseCallback != null) {
                    responseCallback.onCompleted(e, asyncHttpResponse, s);
                }
            }
        });
    }

    public static Future<?> executePost(Request request, final ResponseCallback responseCallback) {
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(request.getURL());
        String body;
        if (request.getBodyParams() != null) {
            if (BuildConfig.DEBUG) {
                LogUtils.d(request.getBodyParams().toString());
            }
            body = HttpUtils.getRequestParams(request.getBodyParams());
        } else {
            body = "";
        }
        AsyncHttpRequestBody<String> asyncHttpRequestBody = new StringBody(body);
        asyncHttpPost.setBody(asyncHttpRequestBody);
        addHead(request.getHeader(), asyncHttpPost);
        return asyncHttpClient.executeString(asyncHttpPost, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse asyncHttpResponse, String s) {
                if (responseCallback != null) {
                    responseCallback.onCompleted(e, asyncHttpResponse, s);
                }
            }
        });
    }

    public static Future<?> executeGet(Request request, final ResponseCallback responseCallback) {
        StringBuilder stringBuffer = new StringBuilder(request.getURL());
        if (request.getURLParams() != null) {
            String params = HttpUtils.getRequestParams(request.getURLParams());
            if (!TextUtils.isEmpty(params)) {
                stringBuffer.append("?").append(params);
            }
        }
        if (BuildConfig.DEBUG) {
            LogUtils.d(stringBuffer.toString());
        }
        AsyncHttpGet asyncHttpGet = new AsyncHttpGet(stringBuffer.toString());
        CacheControl cacheControl = request.getCacheControl();
        if (cacheControl != null) {
            asyncHttpGet.setHeader("Cache-Control", cacheControl.getCacheHeaderProperty());
        }
        addHead(request.getHeader(), asyncHttpGet);
        return asyncHttpClient.executeString(asyncHttpGet, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse asyncHttpResponse, String s) {
                if (responseCallback != null) {
                    responseCallback.onCompleted(e, asyncHttpResponse, s);
                }
            }
        });
    }

    public static Future<?> uploadFile(Request request, final ResponseCallback responseCallback) {
        StringBuilder stringBuffer = new StringBuilder(request.getURL());
        if (request.getURLParams() != null) {
            String params = HttpUtils.getRequestParams(request.getURLParams());
            if (!TextUtils.isEmpty(params)) {
                stringBuffer.append("?").append(params);
            }
        }
        if (BuildConfig.DEBUG) {
            LogUtils.d(stringBuffer.toString());
        }
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(stringBuffer.toString());
        HashMap<String, File> file = request.getFileBody();
        if (file != null && !file.isEmpty()) {
            asyncHttpPost.setBody(new FileBody(file.entrySet().iterator().next().getValue()));
        }
        addHead(request.getHeader(), asyncHttpPost);
        return asyncHttpClient.executeString(asyncHttpPost, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse asyncHttpResponse, String s) {
                if (responseCallback != null) {
                    responseCallback.onCompleted(e, asyncHttpResponse, s);
                }
            }
        });
    }

    public static Future<?> uploadMultiFormData(Request request, final ResponseCallback responseCallback) {
        StringBuilder stringBuffer = new StringBuilder(request.getURL());
        if (request.getURLParams() != null) {
            String params = HttpUtils.getRequestParams(request.getURLParams());
            if (!TextUtils.isEmpty(params)) {
                stringBuffer.append("?").append(params);
            }
        }
        if (BuildConfig.DEBUG) {
            LogUtils.d(stringBuffer.toString());
        }
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(stringBuffer.toString());
        MultipartFormDataBody multipartFormDataBody = new MultipartFormDataBody();
        HashMap<String, String> paramsBody = request.getBodyParams();
        if (paramsBody != null) {
            Set<Map.Entry<String, String>> paramsSet = paramsBody.entrySet();
            Iterator<Map.Entry<String, String>> paramsIterator = paramsSet.iterator();
            while (paramsIterator.hasNext()) {
                Map.Entry<String, String> entry = paramsIterator.next();
                multipartFormDataBody.addStringPart(entry.getKey(), entry.getValue());
            }
        }
        HashMap<String, File> fileBody = request.getFileBody();
        if (fileBody != null) {
            Set<Map.Entry<String, File>> entries = fileBody.entrySet();
            Iterator<Map.Entry<String, File>> iterable = entries.iterator();
            while (iterable.hasNext()) {
                Map.Entry<String, File> entry = iterable.next();
                multipartFormDataBody.addFilePart(entry.getKey(), entry.getValue());
            }
        }
        asyncHttpPost.setBody(multipartFormDataBody);
        addHead(request.getHeader(), asyncHttpPost);
        return asyncHttpClient.executeString(asyncHttpPost, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse asyncHttpResponse, String s) {
                if (responseCallback != null) {
                    responseCallback.onCompleted(e, asyncHttpResponse, s);
                }
            }
        });
    }

    public static Future<?> downloadFile(final Request request, final ResponseCallback responseCallback) {
        StringBuilder stringBuffer = new StringBuilder(request.getURL());
        if (request.getURLParams() != null) {
            String params = HttpUtils.getRequestParams(request.getURLParams());
            if (!TextUtils.isEmpty(params)) {
                stringBuffer.append("?").append(params);
            }
        }
        if (BuildConfig.DEBUG) {
            LogUtils.d(stringBuffer.toString());
        }
        AsyncHttpGet asyncHttpGet = new AsyncHttpGet(stringBuffer.toString());
        CacheControl cacheControl = request.getCacheControl();
        if (cacheControl != null) {
            asyncHttpGet.setHeader("Cache-Control", cacheControl.getCacheHeaderProperty());
        }
        addHead(request.getHeader(), asyncHttpGet);
        return asyncHttpClient.executeFile(asyncHttpGet, request.getLocalRepositoryPath(), new AsyncHttpClient.FileCallback() {

            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, File result) {
                if (responseCallback != null) {
                    responseCallback.onCompleted(e, source, result);
                }
            }

            @Override
            public void onProgress(AsyncHttpResponse response, long downloaded, long total) {
                super.onProgress(response, downloaded, total);
                if (responseCallback != null) {
                    responseCallback.onProgress(response, downloaded, total);
                }
            }

            @Override
            public void onConnect(AsyncHttpResponse response) {
                super.onConnect(response);
                if (responseCallback != null) {
                    responseCallback.onConnect(response);
                }
            }
        });
    }

    private static void addHead(HashMap<String, String> header, AsyncHttpRequest asyncHttp) {
        if (header != null && !header.isEmpty()) {
            if (BuildConfig.DEBUG) {
                LogUtils.d(header.toString());
            }
            Set<String> keys = header.keySet();
            for (String key : keys) {
                asyncHttp.setHeader(key, header.get(key));
            }
        }
    }
}
