package com.longway.framework.core.network;

import android.content.Context;

import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.Headers;
import com.koushikdutta.async.http.Multimap;
import com.koushikdutta.async.http.cache.ResponseCacheMiddleware;
import com.koushikdutta.async.http.spdy.SpdyMiddleware;
import com.koushikdutta.async.util.FileCache;
import com.longway.framework.core.network.base.BasicAuthMiddleware;
import com.longway.framework.core.network.base.CacheControl;
import com.longway.framework.core.network.base.HttpApi;
import com.longway.framework.core.network.base.Request;
import com.longway.framework.core.network.base.Response;
import com.longway.framework.core.network.base.callback.ResponseCallback;
import com.longway.framework.util.IOUtils;
import com.longway.framework.util.JsonUtil;
import com.longway.framework.util.Utils;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by longway on 16/6/11.
 * Email:longway1991117@sina.com
 */

public class NetworkDelegate implements INetwork {
    private Map<String, Future<?>> mTask = new ConcurrentHashMap<>();
    private Map<String, SSLContext> mSSL = new ConcurrentHashMap<>();
    private ResponseCacheMiddleware mResponseCacheMiddleware;
    private BasicAuthMiddleware mBasicAuthMiddleware;
    private Context mContext;

    public NetworkDelegate(Context context) {
        this.mContext = context;
    }


    private Response convert(Request request, AsyncHttpResponse response, String s) {
        Response res = new Response();
        res.setCode(response.code());
        res.setMessage(response.message());
        res.setProtocol(response.protocol());
        res.setBody(s);
        res.setSourceBody(s.getBytes());
        Type type = request.getBusinessType();
        if (type != null) {
            res.setBussinessResponse(JsonUtil.fromJson(s, type));
        }
        Headers headers = response.headers();
        Multimap multimap = headers.getMultiMap();
        Set<String> keySet = multimap.keySet();
        for (String key : keySet) {
            res.addHeader(key, multimap.get(key));
        }
        res.setRequest(request);
        return res;
    }

    private void checkNull(Request request, NetworkListener networkListener) {
        if (request == null || networkListener == null) {
            throw new NullPointerException("request must be not null and networkListener must be not null");
        }
    }

    @Override
    public void executeGetAsync(final Request request, final NetworkListener networkListener) {
        checkNull(request, networkListener);
        networkListener.onStart(request);
        Future<?> future = HttpApi.executeGet(request, new ResponseCallback<String>() {

            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String s) {
                if (e != null) {
                    if (request.getCacheControl() != null) {
                        if (request.getCacheControl().getCacheType() == CacheControl.NETWORK_CACHE) {
                            request.setCacheControl(CacheControl.getCacheControl(CacheControl.CACHE));
                            executeGetAsync(request, networkListener);
                            return;
                        }
                    }
                }
                handleResponse(e, response, s, request, networkListener);

            }

            @Override
            public void onProgress(AsyncHttpResponse response, long downloaded, long total) {

            }

            @Override
            public void onConnect(AsyncHttpResponse response) {

            }
        });
        mTask.put(request.getRealTag(), future);
    }

    private void handleResponse(Exception e, AsyncHttpResponse response, String s, Request request, NetworkListener networkListener) {
        Future future = mTask.remove(request.getRealTag());
        if (future.isCancelled()) {
            return;
        }
        if (e != null) {
            networkListener.onError(request, e);
        } else {
            networkListener.onComplete(convert(request, response, s));
        }
    }

    @Override
    public Response executeGetSync(Request request) {
        throw new UnsupportedOperationException("executeGetSync not support");
    }

    @Override
    public void executePostAsync(final Request request, final NetworkListener networkListener) {
        checkNull(request, networkListener);
        networkListener.onStart(request);
        Future<?> future = HttpApi.executePost(request, new ResponseCallback<String>() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String s) {
                handleResponse(e, response, s, request, networkListener);
            }

            @Override
            public void onProgress(AsyncHttpResponse response, long downloaded, long total) {

            }

            @Override
            public void onConnect(AsyncHttpResponse response) {

            }
        });
        mTask.put(request.getRealTag(), future);
    }

    @Override
    public Response executePostSync(Request request) {
        throw new UnsupportedOperationException("executePostSync not support");
    }

    @Override
    public void executePostJsonObjectAsync(final Request request, final NetworkListener networkListener) {
        checkNull(request, networkListener);
        networkListener.onStart(request);
        try {
            Future future = HttpApi.executePostJsonObject(request, new ResponseCallback<String>() {
                @Override
                public void onCompleted(Exception e, AsyncHttpResponse response, String s) {
                    handleResponse(e, response, s, request, networkListener);
                }

                @Override
                public void onProgress(AsyncHttpResponse response, long downloaded, long total) {

                }

                @Override
                public void onConnect(AsyncHttpResponse response) {

                }
            });
            mTask.put(request.getRealTag(), future);
        } catch (JSONException e) {
            e.printStackTrace();
            mTask.remove(request.getRealTag());
            networkListener.onError(request, e);
        }
    }

    @Override
    public Response executePostJsonObjectSync(Request request) {
        throw new UnsupportedOperationException("executePostJsonObjectSync not support");
    }

    @Override
    public void executePostJsonArrayAsync(final Request request, final NetworkListener networkListener) {
        checkNull(request, networkListener);
        networkListener.onStart(request);
        try {
            Future future = HttpApi.executePostJsonArray(request, new ResponseCallback<String>() {
                @Override
                public void onCompleted(Exception e, AsyncHttpResponse response, String s) {
                    handleResponse(e, response, s, request, networkListener);
                }

                @Override
                public void onProgress(AsyncHttpResponse response, long downloaded, long total) {

                }

                @Override
                public void onConnect(AsyncHttpResponse response) {

                }
            });
            mTask.put(request.getRealTag(), future);
        } catch (JSONException e) {
            e.printStackTrace();
            mTask.remove(request.getRealTag());
            networkListener.onError(request, e);
        }
    }

    @Override
    public Response executePostJsonArraySync(Request request) {
        return null;
    }

    @Override
    public void executePutAsync(Request request, NetworkListener networkListener) {

    }

    @Override
    public Response executePutSync(Request request) {
        return null;
    }

    @Override
    public void uploadFile(final Request request, final NetworkListener networkListener) {
        checkNull(request, networkListener);
        networkListener.onStart(request);
        Future future = HttpApi.uploadFile(request, new ResponseCallback<String>() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String s) {
                handleResponse(e, response, s, request, networkListener);
            }

            @Override
            public void onProgress(AsyncHttpResponse response, long downloaded, long total) {

            }

            @Override
            public void onConnect(AsyncHttpResponse response) {

            }
        });
        mTask.put(request.getRealTag(), future);
    }

    @Override
    public void uploadMultiFormData(final Request request, final NetworkListener networkListener) {
        checkNull(request, networkListener);
        networkListener.onStart(request);
        Future future = HttpApi.uploadMultiFormData(request, new ResponseCallback<String>() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String s) {
                handleResponse(e, response, s, request, networkListener);
            }

            @Override
            public void onProgress(AsyncHttpResponse response, long downloaded, long total) {

            }

            @Override
            public void onConnect(AsyncHttpResponse response) {

            }
        });
        mTask.put(request.getRealTag(), future);
    }

    @Override
    public void downloadFile(final Request request, final DownloadListener networkListener) {
        checkNull(request, networkListener);
        networkListener.onStart(request);
        Future future = HttpApi.downloadFile(request, new ResponseCallback<File>() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse asyncHttpResponse, File result) {
                handleResponse(e, asyncHttpResponse, result.getAbsolutePath(), request, networkListener);
            }

            @Override
            public void onProgress(AsyncHttpResponse response, long downloaded, long total) {
                networkListener.onProgress(downloaded, total);
            }

            @Override
            public void onConnect(AsyncHttpResponse response) {

            }
        });
        mTask.put(request.getRealTag(), future);
    }

    @Override
    public boolean cancel(String... tags) {
        if (tags == null || tags.length == 0) {
            return false;
        }
        for (String tag : tags) {
            Future<?> future = mTask.remove(tag);
            Utils.cancelFuture(future, true);
        }
        return true;
    }

    @Override
    public boolean cancelAll() {
        Set<String> tags = mTask.keySet();
        for (String tag : tags) {
            Utils.cancelFuture(mTask.remove(tag), true);
        }
        return true;
    }

    @Override
    public boolean contain(String tag) {
        return mTask.containsKey(tag);
    }

    @Override
    public void addCache(File dir, int maxSize) {
        if (mResponseCacheMiddleware == null) {
            try {
                mResponseCacheMiddleware = ResponseCacheMiddleware.addCache(HttpApi.asyncHttpClient, dir, maxSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setCaching(boolean caching) {
        if (mResponseCacheMiddleware == null) {
            throw new NullPointerException("must be call addCache(File dir, int maxSize) before setCaching(boolean caching)");
        }
        mResponseCacheMiddleware.setCaching(caching);
    }

    @Override
    public void setCacheControlForHost(String url, String cacheControl) {
        if (mResponseCacheMiddleware == null) {
            throw new NullPointerException("must be call addCache(File dir, int maxSize) before setCacheControlForHost(String host, String cacheControl)");
        }
        mResponseCacheMiddleware.setCacheControlForHost(url, cacheControl);
    }

    @Override
    public void setAuthorizationForHost(String host, String username, String password) {
        if (mBasicAuthMiddleware == null) {
            mBasicAuthMiddleware = BasicAuthMiddleware.add(HttpApi.asyncHttpClient);
        }
        mBasicAuthMiddleware.setAuthorizationForHost(host, username, password);
    }

    @Override
    public void clearHttpCache() {
        if (mResponseCacheMiddleware == null) {
            throw new NullPointerException("must be call addCache(File dir, int maxSize) before clearHttpCache() ");
        }
        FileCache fileCache = mResponseCacheMiddleware.getFileCache();
        fileCache.clear();
    }

    @Override
    public void addAssetsCert(String uri, String cert) {
        try {
            addCert(uri, mContext.getAssets().open(cert));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addRawCert(String uri, int certId) {
        addCert(uri, mContext.getResources().openRawResource(certId));
    }

    @Override
    public void addCert(String uri, File file) {
        try {
            addCert(uri, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCert(String uri, InputStream inputStream) {
        if (!mSSL.containsKey(uri)) {
            //创建X .509 格式的CertificateFactory
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                //从asserts中获取证书的流
                //ca是java.security.cert.Certificate，不是java.security.Certificate，
                //也不是javax.security.cert.Certificate
                Certificate ca = cf.generateCertificate(inputStream);
                IOUtils.closeQuietly(inputStream);

                // 创建一个默认类型的KeyStore，存储我们信任的证书
                String keyStoreType = KeyStore.getDefaultType();

                KeyStore keyStore = KeyStore.getInstance(keyStoreType);

                keyStore.load(null, null);

                //将证书ca作为信任的证书放入到keyStore中
                keyStore.setCertificateEntry("ca", ca);

                //TrustManagerFactory是用于生成TrustManager的，我们创建一个默认类型的TrustManagerFactory

                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();

                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);

                //用我们之前的keyStore实例初始化TrustManagerFactory，这样tmf就会信任keyStore中的证书

                tmf.init(keyStore);

                //通过tmf获取TrustManager数组，TrustManager也会信任keyStore中的证书

                TrustManager[] trustManagers = tmf.getTrustManagers();

                //创建TLS类型的SSLContext对象， that uses our TrustManager

                SSLContext sslContext = SSLContext.getInstance("TLS");
                //用上面得到的trustManagers初始化SSLContext，这样sslContext就会信任keyStore中的证书
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, null);
                sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, new SecureRandom());
                mSSL.put(uri, sslContext);
                bindSSLContext(sslContext);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            bindSSLContext(getSSLContext(uri));
        }
    }

    private void bindSSLContext(SSLContext sslContext) {
        SpdyMiddleware spdyMiddleware = HttpApi.asyncHttpClient.getSSLSocketMiddleware();
        spdyMiddleware.setSSLContext(sslContext);
        if (!spdyMiddleware.getSpdyEnabled()) {
            spdyMiddleware.setSpdyEnabled(true);
        }
    }

    @Override
    public SSLContext getSSLContext(String uri) {
        return mSSL.get(uri);
    }
}
