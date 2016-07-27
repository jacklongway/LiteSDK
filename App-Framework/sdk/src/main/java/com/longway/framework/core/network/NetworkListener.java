package com.longway.framework.core.network;

import com.longway.framework.core.network.base.Request;
import com.longway.framework.core.network.base.Response;

/**
 * Created by longway on 16/6/11.
 * Email:longway1991117@sina.com
 */

public interface NetworkListener {
    void onStart(Request request);

    void onError(Request request, Exception ex);

    void onComplete(Response response);
}
