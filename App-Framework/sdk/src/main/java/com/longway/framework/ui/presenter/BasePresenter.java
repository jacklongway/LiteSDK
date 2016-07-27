package com.longway.framework.ui.presenter;

import com.longway.framework.AndroidApplication;
import com.longway.framework.core.network.NetworkListener;
import com.longway.framework.core.network.NetworkManager;
import com.longway.framework.core.network.base.Request;
import com.longway.framework.core.network.base.Response;
import com.longway.framework.reference.weakreference.BaseWeakReference;
import com.longway.framework.util.JsonUtil;
import com.longway.framework.util.NetWorkUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * Created by longway on 15/7/25.
 * 控制器基础类
 */
public abstract class BasePresenter<T extends IView> implements IBasePresenter {
    private int mCurrentPage = 0;
    private int mMaxPageNumber = 0;

    public void setCurrentPage(int currentPage) {
        this.mCurrentPage = currentPage;
    }

    public void setMaxPageNumber(int maxPageNumber) {
        this.mMaxPageNumber = maxPageNumber;
    }

    public boolean isLastPage() {
        return mCurrentPage == mMaxPageNumber;
    }

    public int getNextNumber() {
        return ++mCurrentPage;
    }

    public BasePresenter(BaseWeakReference<?> reference, T view) {
        this.mReference = reference;
        this.mView = view;
        view.setPresenter(this);
    }

    /**
     * Called when the presenter is initialized, this method represents the start of the presenter
     * lifecycle.
     */
    public void initialize() {

    }

    /**
     * Called when the presenter is resumed. After the initialization and when the presenter comes
     * from a pause state.
     */
    public void resume() {

    }

    /**
     * Called when the presenter is paused.
     */
    public void pause() {

    }

    /**
     * Called when the presenter is destroy
     */

    public void destroy() {
        cancelAll();
    }

    protected T mView; // 视图接口
    protected BaseWeakReference<?> mReference; // 操作的上下文
    protected ConcurrentHashMap<String, Future<?>> futures = new ConcurrentHashMap<>(); // 任务操作映射,为了避免任务重复执行，以及取消任务，销毁所有任务

    public void resetView() {
        this.mView = null;
    }

    public String parse(Object obj) {
        return JsonUtil.toJson(obj);
    }


    protected void onStart(Request request) {
        // sub override
    }

    protected void onError(Request request, Exception ex) {
        // sub override
    }

    protected void onComplete(Response resultResponse) {
        // sub override
    }

    protected void cancel(String... tag) {
        NetworkManager.getInstance(AndroidApplication.getInstance()).cancel(tag);
    }

    protected void cancelAll() {
        NetworkManager.getInstance(AndroidApplication.getInstance()).cancelAll();
    }

    protected void httpPostRequest(Request request) {
        if (NetworkManager.getInstance(AndroidApplication.getInstance()).contain(request.getRealTag())) {
            return;
        }
        if (!NetWorkUtil.isNetworkAvailable(AndroidApplication.getInstance())) {
            onError(request, new IllegalAccessException("network unavailable"));
            return;
        }
        NetworkManager.getInstance(AndroidApplication.getInstance()).executePostAsync(request, new NetworkListener() {
            @Override
            public void onStart(Request request) {
                onStart(request);
            }

            @Override
            public void onError(Request request, Exception ex) {
                onError(request, ex);
            }

            @Override
            public void onComplete(Response response) {
                complete(response);
            }
        });
    }

    protected void httpGetRequest(Request request) {
        if (NetworkManager.getInstance(AndroidApplication.getInstance()).contain(request.getRealTag())) {
            return;
        }
        if (!NetWorkUtil.isNetworkAvailable(AndroidApplication.getInstance())) {
            onError(request, new IllegalAccessException("network unavailable"));
            return;
        }
        NetworkManager.getInstance(AndroidApplication.getInstance()).executeGetAsync(request, new NetworkListener() {
            @Override
            public void onStart(Request request) {
                onStart(request);
            }

            @Override
            public void onError(Request request, Exception ex) {
                onError(request, ex);
            }

            @Override
            public void onComplete(Response response) {
                complete(response);
            }
        });
    }

    private void complete(Response response) {
        if (mReference.referenceActive()) {
            onComplete(response);
        }
    }
}
