package com.longway.framework.ui.fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.longway.elabels.BuildConfig;
import com.longway.elabels.R;
import com.longway.framework.AndroidApplication;
import com.longway.framework.adapter.BaseRenderAdapter;
import com.longway.framework.common.Constants;
import com.longway.framework.core.network.base.Request;
import com.longway.framework.core.network.base.Response;
import com.longway.framework.core.network.base.response.ApiResponse;
import com.longway.framework.executor.ThreadPoolTask;
import com.longway.framework.executor.UiThreadManager;
import com.longway.framework.reference.weakreference.WeakReferenceFragment;
import com.longway.framework.ui.presenter.ipresenter.Contract;
import com.longway.framework.ui.presenter.presenterImpl.ListPresenterImpl;
import com.longway.framework.ui.widget.EmptyView;
import com.longway.framework.util.LogUtils;
import com.longway.framework.util.NetWorkUtil;
import com.longway.framework.util.ReflectUtils;

import java.util.List;

import static android.R.attr.action;
import static android.R.attr.type;

/**
 * Email:longway1991117@sina.com
 */
public abstract class BaseListFragment<T> extends BaseFragment implements Contract.IList {
    private static final String TAG = "BaseListFragment";
    private ViewStub mViewStub;
    protected EmptyView mEmptyView;
    protected PtrClassicFrameLayout mPtrClassicFrameLayout;
    protected ListView mListView;
    protected BaseRenderAdapter mBaseRenderAdapter;
    protected ListPresenterImpl mListPresenter;
    private Request mCallArg;

    @Override
    protected void onConnect(NetWorkUtil.netType type) {
        super.onConnect(type);
        // 暂且在wifi网络并且没有数据的情况下请求网络获取数据,子类可以重写逻辑,调用refresh方法
        if (type == NetWorkUtil.netType.wifi) {
            if (mBaseRenderAdapter == null || mBaseRenderAdapter.isEmpty()) {
                refresh(Constants.REFRESH);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListPresenter.destroy();
    }

    @Override
    public void setPresenter(Contract.IListPresenter presenter) {
    }

    @Override
    protected void onDisconnect() {
        super.onDisconnect();
    }

    @Override
    protected void initView(View view) {
        mViewStub = (ViewStub) view.findViewById(R.id.framework_empty_vs);
        mPtrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.framework_list_view_frame);
        final PtrClassicFrameLayout pcf = mPtrClassicFrameLayout;
        pcf.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // refresh
                mListPresenter.setCurrentPage(0);
                refresh(Constants.REFRESH);
            }
        });

        pcf.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                // loadMore
                refresh(Constants.LOAD_MORE);
            }
        });
        mListView = (ListView) view.findViewById(R.id.framework_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int hvc = mListView.getHeaderViewsCount();
                BaseListFragment.this.onItemClick(parent, view, (T) mBaseRenderAdapter.getItem(position - hvc), position, id);
            }
        });
        mListPresenter = new ListPresenterImpl(new WeakReferenceFragment(this), this);
        UiThreadManager.postRunnable(new ThreadPoolTask(TAG) {
            @Override
            public void doTask(Object parameter) {
                pcf.autoRefresh();
            }
        }, 500);
    }

    protected final void refresh(int actionType) {
        Request callArg = mCallArg;
        if (callArg == null) {
            callArg = getCallArg();
            if (callArg == null) {
                throw new IllegalArgumentException("getCallArg must not null");
            }
            callArg.setTag(ListPresenterImpl.REQUEST_LIST);
            mCallArg = callArg;
        }
        callArg.setExtra(actionType);
        callArg.addURLParams(Constants.PAGE, String.valueOf(mListPresenter.getNextNumber()));
        callArg.addURLParams(Constants.PAGE_SIZE, String.valueOf(Constants.DEFAULT_COUNT));
        // 获取业务T的Class类型
        callArg.setBusinessType(ReflectUtils.genericType(getClass()));
        mListPresenter.getList(callArg);
    }

    /**
     * 子类覆盖该方法处理逻辑
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    protected void onItemClick(AdapterView<?> parent, View view, T obj, int position, long id) {

    }

    /**
     * 列表布局
     *
     * @return
     */
    @Override
    public final int getLayoutId() {
        return R.layout.listview_layout;
    }

    /**
     * 请求参数对象
     *
     * @return
     */
    public abstract Request getCallArg();

    /**
     * 开始加载
     *
     * @param request
     */
    @Override
    public void showLoading(Request request) {
    }

    /**
     * 加载出错
     *
     * @param request
     * @param ex
     */
    @Override
    public void onError(final Request request, final Exception ex) {
        UiThreadManager.postRunnable(new ThreadPoolTask<String>(request.getRealTag()) {
            @Override
            public void doTask(String parameter) {
                onRequestError(request, ex);
            }
        });
    }


    protected void onRequestError(Request request, Exception ex) {
        if (BuildConfig.DEBUG) {
            LogUtils.d("[" + action + "," + (ex != null ? ex.getMessage() : "error msg unknown") + "]");
        }
        int actionType = request.getExtra();
        if (actionType == Constants.REFRESH) {
            mPtrClassicFrameLayout.refreshComplete();
        } else if (actionType == Constants.LOAD_MORE) {
            mPtrClassicFrameLayout.loadMoreComplete(!mListPresenter.isLastPage());
        }
        // network error
        renderEmptyView(Constants.NET_ERROR);
    }

    protected void onSelfComplete(Response response) {

    }

    /**
     * render empty view
     *
     * @param type
     */
    private void renderEmptyView(int type) {
        EmptyView view = mEmptyView;
        if (view == null) {
            view = (EmptyView) mViewStub.inflate();
            mEmptyView = view;
            view.setButtonEvent(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refresh(Constants.REFRESH);
                }
            });
        }
        mPtrClassicFrameLayout.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        setEmptyIcon(type);
        setButtonText(type);
    }

    protected void setEmptyIcon(int type) {
        if (BuildConfig.DEBUG) {
            LogUtils.d("EmptyView type:" + type);
        }
    }

    protected void setButtonText(int type) {
        if (BuildConfig.DEBUG) {
            LogUtils.d("EmptyView type:" + type);
        }
    }

    /**
     * 加载完成
     *
     * @param context
     * @param dataSource
     * @return
     */
    protected abstract BaseRenderAdapter createAdapter(Context context, List<T> dataSource);

    @Override
    public void onComplete(final Response res) {
        LogUtils.d("action:" + action + ",response:" + res.toString() + ",type:" + type);
        UiThreadManager.postRunnable(new ThreadPoolTask("response") {
            @Override
            public void doTask(Object params) {
                Object o = res;
                if (o instanceof ApiResponse) {
                    ApiResponse response = (ApiResponse) o;
                    if (type == Constants.REFRESH) {
                        mPtrClassicFrameLayout.refreshComplete();
                    }
                    if (response != null) {
                        boolean success = response.isSucc();
                        if (success) {
                            mListPresenter.setCurrentPage(response.getCurrentPage());
                            mListPresenter.setMaxPageNumber(response.getMaxPage());
                            List<T> data = (List<T>) response.getObj();
                            if (data.isEmpty()) {
                                // no data
                                renderEmptyView(Constants.NO_DATA);
                            } else {
                                mPtrClassicFrameLayout.setVisibility(View.VISIBLE);
                                if (mEmptyView != null) {
                                    mEmptyView.setVisibility(View.GONE);
                                }
                            }
                            if (mBaseRenderAdapter == null) {
                                mBaseRenderAdapter = createAdapter(AndroidApplication.getInstance(), data);
                                mListView.setAdapter(mBaseRenderAdapter);
                                if (!mListPresenter.isLastPage()) {
                                    mPtrClassicFrameLayout.setLoadMoreEnable(true);
                                }
                            } else {
                                BaseRenderAdapter adapter = mBaseRenderAdapter;
                                if (adapter != null) {
                                    if (type == Constants.REFRESH) {
                                        adapter.clear();
                                        adapter.putData(data);
                                    } else if (type == Constants.LOAD_MORE) {
                                        adapter.addData(data);
                                        mPtrClassicFrameLayout.loadMoreComplete(!mListPresenter.isLastPage());
                                    }
                                    if (mListPresenter.isLastPage()) {
                                        mPtrClassicFrameLayout.setLoadMoreEnable(false);
                                    } else {
                                        mPtrClassicFrameLayout.setLoadMoreEnable(true);
                                    }
                                }
                            }
                        } else {
                            // server error
                            renderEmptyView(Constants.SERVER_ERROR);
                        }
                    } else {
                        if (type == Constants.LOAD_MORE) {
                            mPtrClassicFrameLayout.loadMoreComplete(!mListPresenter.isLastPage());
                        }
                        renderEmptyView(Constants.PROTOCOL_ERROR);
                    }
                } else {
                    onSelfComplete(res);
                }
            }
        });
    }
}
