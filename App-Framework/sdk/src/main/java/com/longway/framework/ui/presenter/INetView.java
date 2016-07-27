package com.longway.framework.ui.presenter;

import com.longway.framework.core.network.base.Request;
import com.longway.framework.core.network.base.Response;

/**
 * Created by longway on 16/6/1.
 * Email:longway1991117@sina.com
 */

public interface INetView<T> extends IView<T> {
    void showLoading(Request request);

    void onError(Request request, Exception ex);

    void onComplete(Response response);
}
