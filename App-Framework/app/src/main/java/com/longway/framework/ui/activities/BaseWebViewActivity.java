package com.longway.framework.ui.activities;

import android.content.Intent;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.longway.elabels.R;
import com.longway.framework.ui.fragments.WebViewFragment;
import com.longway.framework.ui.widget.HorizontalProgressView;
import com.longway.framework.util.AppUtils;
import com.longway.framework.util.LogUtils;

/*********************************
 * Created by longway on 16/5/11 下午10:46.
 * packageName:com.longway.framework.ui.activities
 * projectName:trunk
 * Email:longway1991117@sina.com
 ********************************/
public class BaseWebViewActivity extends BaseHeaderActivity implements WebViewFragment.IWebViewCallback {
    private static final String TAG = "WebViewActivity";
    public static final String WEB_VIEW_URL = "webView_url";
    private static final int MAX_PROGRESS = 100;
    private WebViewFragment mWebViewFragment;
    protected HorizontalProgressView mProgressView;

    @Override
    protected final int getContentLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected String getCloseWaringMsg() {
        return null;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected void initView() {
        super.initView();
        mProgressView = (HorizontalProgressView) findViewById(R.id.framework_progress_view);
    }

    @Override
    protected void onAddContent() {
        super.onAddContent();
        WebViewFragment webViewFragment = (WebViewFragment) findFragment(WebViewFragment.class.getSimpleName());
        mWebViewFragment = webViewFragment;
        Intent intent = getIntent();
        String url = intent.getStringExtra(WEB_VIEW_URL);
        LogUtils.d(TAG + ":" + url);
        webViewFragment.loadURL(url);
    }

    @Override
    public void onBackPressed() {
        WebView webView = mWebViewFragment.getWebView();
        if (webView != null) {
            if (webView.canGoBack()) {
                webView.loadUrl("javascript:window.history.back();");
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void processFinish() {
        super.processFinish();
        if (!TextUtils.equals(AppUtils.getProcessName(), getPackageName())) {
            Process.killProcess(Process.myPid());
        }
    }

    /**
     * subclass override set title by headerView
     *
     * @param view
     * @param title
     */
    @Override
    public void onReceivedTitle(WebView view, String title) {

    }

    /**
     * page load finish
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        mProgressView.setVisibility(View.GONE);
    }


    /**
     * load progress changed
     *
     * @param view
     * @param newProgress
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress >= MAX_PROGRESS) {
            mProgressView.setVisibility(View.GONE);
        } else {
            mProgressView.setCurrentProgress(newProgress);
        }
    }

    /**
     * subclass override url loading way
     *
     * @param view
     * @param url
     * @return
     */
    protected boolean urlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        mProgressView.setVisibility(View.VISIBLE);
        return urlLoading(view, url);
    }

    /**
     * subclass override implements js object inject
     *
     * @return
     */
    @Override
    public WebViewFragment.InjectParams buildInjectParams() {
        return null;
    }
}
