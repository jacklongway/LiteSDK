package com.longway.framework.ui.fragments;

import android.view.View;
import android.view.ViewGroup;

import com.longway.elabels.R;
import com.longway.framework.ui.widget.HeaderView;

import butterknife.ButterKnife;

/**
 * Created by longway on 16/5/5.
 * Email:longway1991117@sina.com
 */
public abstract class BaseHeaderFragment extends BaseFragment {
    protected HeaderView mHeaderView;

    @Override
    protected boolean injectView() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_header_layout;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ViewGroup content = (ViewGroup) view.findViewById(R.id.framework_content);
        ButterKnife.inject(this, View.inflate(getActivity(), getContentLayoutId(), content));
        mHeaderView = (HeaderView) view.findViewById(R.id.framework_hv);
        onAddContent();
    }

    protected abstract int getContentLayoutId();

    protected void onAddContent() {

    }
}
