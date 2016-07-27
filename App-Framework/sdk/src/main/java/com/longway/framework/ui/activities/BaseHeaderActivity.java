package com.longway.framework.ui.activities;

import android.view.View;
import android.view.ViewGroup;

import com.longway.elabels.R;
import com.longway.framework.ui.widget.HeaderView;

import butterknife.ButterKnife;

/**
 * Created by longway on 16/5/5.
 * Email:longway1991117@sina.com
 */
public abstract class BaseHeaderActivity extends BaseActivity {
    protected HeaderView mHeaderView;

    @Override
    protected boolean injectView() {
        return false;
    }

    @Override
    protected void initView() {
        super.initView();
        ViewGroup content = (ViewGroup) findViewById(R.id.framework_content);
        ButterKnife.inject(this, View.inflate(this, getContentLayoutId(), content));
        mHeaderView = (HeaderView) findViewById(R.id.framework_hv);
        onAddContent();
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_header_layout;
    }

    protected abstract int getContentLayoutId();

    protected void onAddContent() {

    }
}
