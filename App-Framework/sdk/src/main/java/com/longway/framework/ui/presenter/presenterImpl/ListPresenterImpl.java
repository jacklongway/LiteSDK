package com.longway.framework.ui.presenter.presenterImpl;

import com.longway.framework.core.network.base.Request;
import com.longway.framework.core.network.base.Response;
import com.longway.framework.reference.weakreference.BaseWeakReference;
import com.longway.framework.ui.presenter.BasePresenter;
import com.longway.framework.ui.presenter.ipresenter.Contract;

/**
 * Created by longway on 16/4/26.
 * Email:longway1991117@sina.com
 */
public class ListPresenterImpl extends BasePresenter<Contract.IList> implements Contract.IListPresenter {
    public static final String REQUEST_LIST = "requestList";

    public ListPresenterImpl(BaseWeakReference<?> weakReference, Contract.IList list) {
        super(weakReference, list);
    }

    public void getList(Request request) {
        httpGetRequest(request);
    }

    @Override
    protected void onStart(Request request) {
        super.onStart(request);
        if (mView != null) {
            mView.showLoading(request);
        }
    }

    @Override
    protected void onError(Request request, Exception ex) {
        super.onError(request, ex);
        if (mView != null) {
            mView.onError(request, ex);
        }
    }

    @Override
    protected void onComplete(Response response) {
        super.onComplete(response);
        if (mView != null) {
            mView.onComplete(response);
        }
    }
}
