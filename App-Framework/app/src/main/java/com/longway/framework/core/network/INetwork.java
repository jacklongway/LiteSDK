package com.longway.framework.core.network;

import com.longway.framework.core.network.base.Request;
import com.longway.framework.core.network.base.Response;

import java.io.File;
import java.io.InputStream;

import javax.net.ssl.SSLContext;


/**
 * Created by longway on 16/6/11.
 * Email:longway1991117@sina.com
 */

public interface INetwork {
    void executeGetAsync(Request request, NetworkListener networkListener);

    Response executeGetSync(Request request);

    void executePostAsync(Request request, NetworkListener networkListener);

    Response executePostSync(Request request);

    void executePostJsonObjectAsync(Request request, NetworkListener networkListener);

    Response executePostJsonObjectSync(Request request);

    void executePostJsonArrayAsync(Request request, NetworkListener networkListener);

    Response executePostJsonArraySync(Request request);

    void executePutAsync(Request request, NetworkListener networkListener);

    Response executePutSync(Request request);

    void uploadFile(Request request, NetworkListener networkListener);

    void uploadMultiFormData(Request request, NetworkListener networkListener);

    void downloadFile(Request request, DownloadListener networkListener);

    boolean cancel(String... tag);

    boolean cancelAll();

    boolean contain(String tag);

    void addCache(File dir, int maxSize);

    void setCaching(boolean caching);

    void setCacheControlForHost(String host, String cacheControl);

    void setAuthorizationForHost(String host, String username, String password);

    void clearHttpCache();

    void addAssetsCert(String uri, String cert);

    void addRawCert(String uri, int certId);

    void addCert(String uri, File file);

    void addCert(String uri, InputStream inputStream);

    SSLContext getSSLContext(String uri);
}
