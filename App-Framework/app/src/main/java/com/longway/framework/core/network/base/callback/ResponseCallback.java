package com.longway.framework.core.network.base.callback;

import com.koushikdutta.async.http.AsyncHttpResponse;

/**
 * Created by longway on 15/7/26.
 * 响应回调接口
 */
public interface ResponseCallback<T> {
    void onCompleted(Exception e, AsyncHttpResponse response, T s);
    void onProgress(AsyncHttpResponse response, long downloaded, long total);
    void onConnect(AsyncHttpResponse response);
}
