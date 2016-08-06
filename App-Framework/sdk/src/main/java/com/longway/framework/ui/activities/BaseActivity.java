package com.longway.framework.ui.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.longway.elabels.R;
import com.longway.framework.AndroidApplication;
import com.longway.framework.core.network.base.netstate.NetChangeObserver;
import com.longway.framework.core.network.base.netstate.NetworkStateReceiver;
import com.longway.framework.handler.H;
import com.longway.framework.handler.HandlerCallback;
import com.longway.framework.ui.fragments.BaseFragment;
import com.longway.framework.ui.fragments.ILoading;
import com.longway.framework.ui.fragments.LoadingDialogFragment;
import com.longway.framework.ui.fragments.LoadingFragment;
import com.longway.framework.ui.fragments.fragmentStack.FragmentBuildParams;
import com.longway.framework.util.KeyBoardUtils;
import com.longway.framework.util.NetWorkUtil;
import com.longway.framework.util.ToastUtils;
import com.longway.framework.util.UIUtils;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by longway on 15/7/26.
 * 基本活动部分属性可以为fragment提供支撑
 */
public abstract class BaseActivity extends AppCompatActivity implements HandlerCallback, AndroidApplication.IMemoryInfo, LoadingDialogFragment.IDialogDismiss {
    protected EventBus mEventBus = EventBus.getDefault();
    private NetChangeObserver mNetChangeObserver;
    protected H mH;
    protected LoadingDialogFragment mLoadingDialogFragment;
    private BaseFragment mCurrentFragment;
    private boolean isWarningClose = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.initImmersionStyle(this);
        Window window = getWindow();
        // remove bg
        window.setBackgroundDrawable(new BitmapDrawable());
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (fullScreen()) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(getLayoutId()); // layout id

        if (injectView()) {
            ButterKnife.inject(this); // view inject
        }
        mEventBus.register(this);// event register
        buildNetworkObserver();
        initView();
        dataBinding(); // data binding
    }

    protected boolean injectView() {
        return true;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    protected void initView() {

    }

    protected abstract String getCloseWaringMsg();


    @Override
    public void goodTimeToReleaseMemory(int level) {
        // chile override to releaseMemory
    }


    public Fragment findFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public Fragment replaceFragment(int containerViewId, Class<? extends BaseFragment> clz, Bundle args) {
        Fragment instance = Fragment.instantiate(this, clz.getName(), args);
        getSupportFragmentManager().beginTransaction().replace(containerViewId, instance, clz.getSimpleName()).commit();
        return instance;
    }

    public void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
    }

    public Fragment addFragment(int containerId, Class<? extends BaseFragment> clz, Bundle params) {
        Fragment fragment = Fragment.instantiate(this, clz.getName(), params);
        getSupportFragmentManager().beginTransaction().add(containerId, fragment, fragment.toString()).commit();
        return fragment;
    }

    public Fragment showLoading(int containerId) {
       return showLoading(containerId,null);
    }

    public Fragment showLoading(int containerId, ILoading loading) {
        LoadingFragment fragment = (LoadingFragment) addFragment(containerId, LoadingFragment.class, null);
        fragment.setILoading(loading);
        return fragment;
    }

    public void removeLoading(Fragment fragment){
         removeFragment(fragment);
    }

    /**
     * 注解避免编译警告的同时,还需要判断版本逻辑
     *
     * @param msg
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDialog(String msg) {
        enSureLoadingDialogFragmentNoNull();
        mLoadingDialogFragment.setHintText(msg);
        mLoadingDialogFragment.show(getFragmentManager(), LoadingDialogFragment.class.getSimpleName());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void dismissDialog() {
        if (mLoadingDialogFragment != null) {
            mLoadingDialogFragment.dismissAllowingStateLoss();
            mLoadingDialogFragment = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void enSureLoadingDialogFragmentNoNull() {
        synchronized (this) {
            if (mLoadingDialogFragment == null) {
                mLoadingDialogFragment = (LoadingDialogFragment) android.app.Fragment.instantiate(this, LoadingDialogFragment.class.getName());
                mLoadingDialogFragment.setIDialogDismiss(this);
            }
        }
    }

    /**
     * dialog dismiss
     */
    @Override
    public void dialogDismiss() {

    }

    /**
     * 网络连接
     *
     * @param type
     */
    protected void onConnect(NetWorkUtil.netType type) {

    }

    /**
     * 断开网络链接
     */
    protected void onDisConnect() {
    }

    protected void buildNetworkObserver() {
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onConnect(NetWorkUtil.netType type) {
                super.onConnect(type);
                BaseActivity.this.onConnect(type);
            }

            @Override
            public void onDisConnect() {
                super.onDisConnect();
                BaseActivity.this.onDisConnect();
            }
        };
    }

    /**
     * /**
     * data binding ex: read intent
     */
    protected void dataBinding() {
    }

    /**
     * full screen show?
     *
     * @return
     */
    protected boolean fullScreen() {
        return false;
    }

    /**
     * layout id
     *
     * @return
     */

    public abstract int getLayoutId();

    protected void handleMessage(Message msg) {
        // handler msg
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * dispatch message
     *
     * @param msg
     */
    @Override
    public void dispatchMessage(Message msg) {
        handleMessage(msg);
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            mEventBus.unregister(this);
            mH.clear();
        } catch (Throwable throwable) {

        }
    }

    private void enSureMainHandlerNoNull() {
        if (mH == null) {
            mH = new H(this);
        }
    }

    protected void sendMsgToMainThread(Message msg) {
        enSureMainHandlerNoNull();
        mH.sendMessage(msg);
    }

    protected Message getMsg(int what, Object object, int arg1, int arg2) {
        return H.getMsg(what, object, arg1, arg2);
    }

    @Override
    public void onBackPressed() {
        // perform anim
        if (processBackKey()) {
            return;
        }
        back();
    }

    protected void back() {
        BaseFragment fragment = mCurrentFragment;
        boolean enable = true;
        if (fragment != null) {
            enable = !fragment.processBackKey();
        }
        if (enable) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int c = fragmentManager.getBackStackEntryCount();
            if (c <= 1 && isTaskRoot()) {
                String closeWarningMsg = getCloseWaringMsg();
                if (!isWarningClose && !TextUtils.isEmpty(closeWarningMsg)) {
                    ToastUtils.showToast(this, closeWarningMsg, false);
                    isWarningClose = true;
                } else {
                    doBack();
                }
            } else {
                isWarningClose = false;
                doBack();
            }
        }
    }

    protected void processFinish() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        if (canOverridePendingTransition()) {
            overridePendingTransition(finishActivityInAnimationResId(), finishActivityOutAnimationResId());
        }
    }

    private void doBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int c = fragmentManager.getBackStackEntryCount();
        if (c <= 1) {
            processFinish();
        } else {
            fragmentManager.popBackStackImmediate();
            c = fragmentManager.getBackStackEntryCount();
            String tag = fragmentManager.getBackStackEntryAt(c - 1).getName();
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment != null && fragment instanceof BaseFragment) {
                mCurrentFragment = (BaseFragment) fragment;
                ((BaseFragment) fragment).onBack();
            }
        }
    }

    /**
     * sdk>=16
     *
     * @return
     */
    private boolean canOverridePendingTransition() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    protected boolean processBackKey() {
        return false;
    }

    /**
     * subclass override implement self animation
     *
     * @return
     */
    protected int startActivityInAnimationResId() {
        return R.anim.start_activity_in;
    }

    protected int startActivityOutAnimationResId() {
        return R.anim.start_activity_out;
    }

    protected int finishActivityInAnimationResId() {
        return R.anim.finish_activity_in;
    }

    protected int finishActivityOutAnimationResId() {
        return R.anim.finish_activity_out;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivityForResult(intent, -1);
    }

    /**
     * requestCode must be >=0
     *
     * @param intent
     * @param requestCode
     * @param options
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        if (canOverridePendingTransition()) {
            overridePendingTransition(startActivityInAnimationResId(), startActivityOutAnimationResId());
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        try {
            super.onStop();
            AndroidApplication.getInstance().unregisterMemoryListener(this);
            NetworkStateReceiver.removeRegisterObserver(mNetChangeObserver);
            NetworkStateReceiver.unRegisterNetworkStateReceiver(this);
        } catch (RuntimeException ex) {

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            AndroidApplication.getInstance().registerMemoryListener(this);
            NetworkStateReceiver.registerObserver(mNetChangeObserver);
            NetworkStateReceiver.registerNetworkStateReceiver(this);
            NetworkStateReceiver.checkNetworkState(this);
        } catch (RuntimeException ex) {

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected abstract int getFragmentContainerId();

    public Fragment addFragment(Class<? extends BaseFragment> clz, Bundle params) {
        if (clz == null) {
            return null;
        }
        return addFragment(new FragmentBuildParams(clz, params));
    }

    public Fragment addFragment(Class<? extends BaseFragment> clz) {
        return addFragment(new FragmentBuildParams(clz, null));
    }

    public Fragment addFragment(FragmentBuildParams fragmentBuildParams) {
        if (fragmentBuildParams == null) {
            return null;
        }
        Class<? extends BaseFragment> clz = fragmentBuildParams.mClz;
        if (clz == null) {
            return null;
        }
        try {

            String tag = clz.getName();
            BaseFragment fragment = (BaseFragment) findFragment(tag);
            if (fragment == null) {
                fragment = (BaseFragment) Fragment.instantiate(this, tag, fragmentBuildParams.mParams);
            }
            BaseFragment baseFragment = mCurrentFragment;
            if (baseFragment == fragment) {
                return fragment;
            }
            mCurrentFragment = fragment;
            fragment.onEnter(); // enter perform logic
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (!fragment.isAdded()) {
                fragmentTransaction.add(getFragmentContainerId(), fragment, tag).addToBackStack(tag);
            } else {
                fragmentTransaction.show(fragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
            if (baseFragment != null) {
                baseFragment.onLeave();
            }
            isWarningClose = false;
            return fragment;
        } catch (Fragment.InstantiationException ex) {
            ex.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public void popTopFragment(Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
        int c = fragmentManager.getBackStackEntryCount();
        if (c >= 1) {
            String tag = fragmentManager.getBackStackEntryAt(c - 1).getName();
            Fragment fragment = findFragment(tag);
            if (fragment != null && fragment instanceof BaseFragment) {
                mCurrentFragment = (BaseFragment) fragment;
                ((BaseFragment) fragment).onBack(bundle);
            }
        }
    }

    public void popToRootFragment(Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int c = fragmentManager.getBackStackEntryCount();
        if (c <= 1) {
            return;
        }
        while (c > 2) {
            fragmentManager.popBackStackImmediate();
            c--;
        }
        popTopFragment(bundle);
    }

    public void goToFragment(Class<? extends BaseFragment> clz, Bundle bundle) {
        if (clz == null) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = clz.getName();
        Fragment fragment = findFragment(tag);
        if (fragment != null && fragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) fragment;
            mCurrentFragment = baseFragment;
            baseFragment.onBack(bundle);
        }
        fragmentManager.popBackStackImmediate(tag, 0);
    }


    public void openOrCloseSoftkeyBoard(View view, boolean open) {
        KeyBoardUtils.openOrCloseKeyBoard(view, open);
    }

}
