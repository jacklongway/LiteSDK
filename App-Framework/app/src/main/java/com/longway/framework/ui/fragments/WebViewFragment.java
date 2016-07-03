package com.longway.framework.ui.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.longway.elabels.BuildConfig;
import com.longway.framework.ui.widget.XWebView;

import cn.pedant.SafeWebViewBridge.InjectedChromeClient;

/**
 * Created by longway on 15/12/26.
 */

/**
 * Created by longway on 15/12/13.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class WebViewFragment extends Fragment {
    private XWebView mWebView;
    private boolean mIsWebViewAvailable;
    private IWebViewCallback mIWebViewCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mIWebViewCallback = (IWebViewCallback) activity;
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    public WebViewFragment() {
    }

    public void loadURL(String url) {
        mWebView.loadUrl(url);
    }

    /**
     * Called to instantiate the view. Creates and returns the WebView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = new XWebView(getActivity());
        mWebView.removeJavascriptInterface("searchBoxJavaBredge_");
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.KITKAT) {
            //在 Chrome 中输入 chrome://inspect
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!webSettings.getLoadsImagesAutomatically()) {
                    webSettings.setLoadsImagesAutomatically(true);
                }
                if (mIWebViewCallback != null) {
                    mIWebViewCallback.onPageFinished(view, url);
                }

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (mIWebViewCallback != null) {
                    mIWebViewCallback.onReceivedError(view, errorCode, description, failingUrl);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (mIWebViewCallback != null) {
                    return mIWebViewCallback.shouldOverrideUrlLoading(view, url);
                }
                return shouldOverrideUrlLoading(view, url);
            }
        });
        InjectParams injectParams = mIWebViewCallback.buildInjectParams();
        if (injectParams != null) {
            String injectName = injectParams.injectedName;
            Class<?> injectClz = injectParams.injectedCls;
            if (!TextUtils.isEmpty(injectName) && injectClz != null) {
                mWebView.setWebChromeClient(new CustomChromeClient(injectName, injectClz));
            } else {
                mWebView.setWebChromeClient(new DefaultChromeClient());
            }
        } else {
            mWebView.setWebChromeClient(new DefaultChromeClient());
        }
        mIsWebViewAvailable = true;
        return mWebView;
    }

    private class DefaultChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mIWebViewCallback != null) {
                mIWebViewCallback.onReceivedTitle(view, title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (mIWebViewCallback != null) {
                mIWebViewCallback.onProgressChanged(view, newProgress);
            }
        }
    }

    private class CustomChromeClient extends InjectedChromeClient {

        public CustomChromeClient(String injectedName, Class injectedCls) {
            super(injectedName, injectedCls);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }


        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mIWebViewCallback != null) {
                mIWebViewCallback.onReceivedTitle(view, title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (mIWebViewCallback != null) {
                mIWebViewCallback.onProgressChanged(view, newProgress);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    /**
     * Called when the WebView has been detached from the fragment.
     * The WebView is no longer available after this time.
     */
    @Override
    public void onDestroyView() {
        mIsWebViewAvailable = false;
        super.onDestroyView();
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    /**
     * Gets the WebView.
     */
    public WebView getWebView() {
        return mIsWebViewAvailable ? mWebView : null;
    }

    public interface IWebViewCallback {
        void onReceivedTitle(WebView view, String title);

        void onPageFinished(WebView view, String url);

        void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

        void onProgressChanged(WebView view, int newProgress);

        boolean shouldOverrideUrlLoading(WebView view, String url);

        InjectParams buildInjectParams();
    }

    public static class InjectParams {
        public final String injectedName;
        public final Class<?> injectedCls;

        public InjectParams(String injectedName, Class<?> injectedCls) {
            this.injectedName = injectedName;
            this.injectedCls = injectedCls;
        }
    }
}