package com.longway.framework.core.network;

import android.app.Application;
import android.content.Context;

import com.longway.framework.core.common.AbsManager;
import com.longway.framework.core.network.base.Request;
import com.longway.framework.core.network.base.Response;

import java.io.File;
import java.io.InputStream;

import javax.net.ssl.SSLContext;

/**
 * Created by longway on 16/6/11.
 * Email:longway1991117@sina.com
 */

public class NetworkManager extends AbsManager<INetwork> implements INetwork {
    private static volatile NetworkManager sNetworkManager;
    private Context mContext;
    private final INetwork mDelegate;

    private NetworkManager(Context context) {
        if (!(context instanceof Application)) {
            mContext = context.getApplicationContext();
        } else {
            mContext = context;
        }
        mDelegate = new NetworkDelegate(mContext);
    }

    public static NetworkManager getInstance(Context context) {
        if (sNetworkManager == null) {
            synchronized (NetworkManager.class) {
                if (sNetworkManager == null) {
                    sNetworkManager = new NetworkManager(context);
                }
            }
        }
        return sNetworkManager;
    }


    @Override
    public INetwork getCurrentUse() {
        INetwork network = super.getCurrentUse();
        if (network == null) {
            network = mDelegate;
        }
        return network;
    }

    @Override
    public void executeGetAsync(Request request, NetworkListener networkListener) {
        getCurrentUse().executeGetAsync(request, networkListener);
    }

    @Override
    public Response executeGetSync(Request request) {
        return getCurrentUse().executeGetSync(request);
    }

    @Override
    public void executePostAsync(Request request, NetworkListener networkListener) {
        getCurrentUse().executePostAsync(request, networkListener);
    }

    @Override
    public Response executePostSync(Request request) {
        return getCurrentUse().executePostSync(request);
    }

    @Override
    public void executePostJsonObjectAsync(Request request, NetworkListener networkListener) {
        getCurrentUse().executePostJsonObjectAsync(request, networkListener);
    }

    @Override
    public Response executePostJsonObjectSync(Request request) {
        return getCurrentUse().executePostJsonObjectSync(request);
    }

    @Override
    public void executePostJsonArrayAsync(Request request, NetworkListener networkListener) {
        getCurrentUse().executePostJsonArrayAsync(request, networkListener);
    }

    @Override
    public Response executePostJsonArraySync(Request request) {
        return getCurrentUse().executePostJsonArraySync(request);
    }

    @Override
    public void executePutAsync(Request request, NetworkListener networkListener) {
        getCurrentUse().executePutAsync(request, networkListener);
    }

    @Override
    public Response executePutSync(Request request) {
        return getCurrentUse().executePutSync(request);
    }

    @Override
    public void uploadFile(Request request, NetworkListener networkListener) {
        getCurrentUse().uploadFile(request, networkListener);
    }

    @Override
    public void uploadMultiFormData(Request request, NetworkListener networkListener) {
        getCurrentUse().uploadMultiFormData(request, networkListener);
    }

    @Override
    public void downloadFile(Request request, DownloadListener networkListener) {
        getCurrentUse().downloadFile(request, networkListener);
    }

    @Override
    public boolean cancel(String... tag) {
        return getCurrentUse().cancel(tag);
    }

    @Override
    public boolean cancelAll() {
        return getCurrentUse().cancelAll();
    }

    @Override
    public boolean contain(String tag) {
        return getCurrentUse().contain(tag);
    }

    @Override
    public void addCache(File dir, int maxSize) {
        getCurrentUse().addCache(dir, maxSize);
    }

    @Override
    public void setCaching(boolean caching) {
        getCurrentUse().setCaching(caching);
    }

    @Override
    public void setCacheControlForHost(String host, String cacheControl) {
        getCurrentUse().setCacheControlForHost(host, cacheControl);
    }

    @Override
    public void setAuthorizationForHost(String host, String username, String password) {
        getCurrentUse().setAuthorizationForHost(host,username,password);
    }

    @Override
    public void clearHttpCache() {
        getCurrentUse().clearHttpCache();
    }

    @Override
    public void addAssetsCert(String uri, String cert) {
        getCurrentUse().addAssetsCert(uri, cert);
    }

    @Override
    public void addRawCert(String uri, int certId) {
        getCurrentUse().addRawCert(uri, certId);
    }

    @Override
    public void addCert(String uri, File file) {
        getCurrentUse().addCert(uri, file);
    }

    @Override
    public void addCert(String uri, InputStream inputStream) {
        getCurrentUse().addCert(uri, inputStream);
    }

    @Override
    public SSLContext getSSLContext(String uri) {
        return getCurrentUse().getSSLContext(uri);
    }
}
