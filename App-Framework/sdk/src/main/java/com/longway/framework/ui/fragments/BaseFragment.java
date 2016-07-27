package com.longway.framework.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longway.elabels.BuildConfig;
import com.longway.framework.AndroidApplication;
import com.longway.framework.core.network.base.netstate.NetChangeObserver;
import com.longway.framework.core.network.base.netstate.NetworkStateReceiver;
import com.longway.framework.ui.fragments.fragmentStack.IFragment;
import com.longway.framework.util.LogUtils;
import com.longway.framework.util.NetWorkUtil;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by longway on 15/7/26.
 */
public abstract class BaseFragment extends Fragment implements AndroidApplication.IMemoryInfo, IFragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    private EventBus eventBus = EventBus.getDefault();
    private NetChangeObserver mNCO = new NetChangeObserver() {
        @Override
        public void onConnect(NetWorkUtil.netType type) {
            super.onConnect(type);
            BaseFragment.this.onConnect(type);
        }

        @Override
        public void onDisConnect() {
            super.onDisConnect();
            BaseFragment.this.onDisconnect();
        }
    };


    protected void onConnect(NetWorkUtil.netType type) {
        if (BuildConfig.DEBUG) {
            LogUtils.d(TAG.concat(":").concat(String.valueOf(type)));
        }
    }

    protected void onDisconnect() {
        if (BuildConfig.DEBUG) {
            LogUtils.d(TAG.concat(":disconnect"));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        eventBus.register(this);
        if (BuildConfig.DEBUG) {
            RefWatcher refWatcher = AndroidApplication.getRefWatcher(getActivity());
            refWatcher.watch(this);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getParentFragment() == null) {
            setRetainInstance(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * requestCode must be >=0
     *
     * @param intent
     * @param requestCode
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    protected boolean injectView() {
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (injectView()) {
            ButterKnife.inject(this, view);
        }

        initView(view);
    }

    protected void initView(View view) {

    }

    public abstract int getLayoutId();

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
        } catch (Throwable throwable) {
            // ignore
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        eventBus.unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        AndroidApplication.getInstance().unregisterMemoryListener(this);
        unregisterNetObserver();
    }

    @Override
    public void onResume() {
        super.onResume();
        AndroidApplication.getInstance().registerMemoryListener(this);
        registerNetObserver();
    }

    @Override
    public void goodTimeToReleaseMemory(int level) {
        // child override to release memory
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onEnter() {
        if (BuildConfig.DEBUG) {
            LogUtils.d(this.getClass().getSimpleName() + " onEnter");
        }
        View view = getView();
        if (view != null) {
            int visibility = view.getVisibility();
            if (visibility == View.GONE) {
                view.setVisibility(View.VISIBLE);
            }
        }
        registerNetObserver();
    }

    private void registerNetObserver() {
        NetworkStateReceiver.registerNetworkStateReceiver(AndroidApplication.getInstance());
        NetworkStateReceiver.registerObserver(mNCO);
        NetworkStateReceiver.checkNetworkState(AndroidApplication.getInstance());
    }

    @Override
    public void onLeave() {
        if (BuildConfig.DEBUG) {
            LogUtils.d(this.getClass().getSimpleName() + " onLeave");
        }
        View view = getView();
        if (view != null) {
            int visibility = view.getVisibility();
            if (visibility == View.VISIBLE) {
                view.setVisibility(View.GONE);
            }
        }
        unregisterNetObserver();
    }

    private void unregisterNetObserver() {
        NetworkStateReceiver.unRegisterNetworkStateReceiver(AndroidApplication.getInstance());
        NetworkStateReceiver.removeRegisterObserver(mNCO);
    }

    @Override
    public void onBack() {
        if (BuildConfig.DEBUG) {
            LogUtils.d(this.getClass().getSimpleName() + " onBack");
        }
    }

    @Override
    public void onBack(Bundle bundle) {
        if (BuildConfig.DEBUG) {
            LogUtils.d(this.getClass().getSimpleName() + " onBack");
        }
    }

    @Override
    public boolean processBackKey() {
        if (BuildConfig.DEBUG) {
            LogUtils.d(this.getClass().getSimpleName() + " processBackKey");
        }
        return false;
    }

}
