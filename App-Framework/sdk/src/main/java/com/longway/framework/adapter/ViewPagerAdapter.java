package com.longway.framework.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.longway.elabels.R;

import java.util.ArrayList;

/**
 * Email:longway1991117@sina.com
 * 加载带有缓存的view
 */

public abstract class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> mCacheView = new ArrayList<>();

    public ViewPagerAdapter() {

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    protected abstract View createView(int position);

    protected abstract void bindData(View view, int position);

    protected abstract int getViewType(int position);

    private View getViewByViewType(int position) {
        View view = null;
        if (mCacheView.isEmpty()) {
            return view;
        }
        for (View v : mCacheView) {
            if (v != null && v.getTag(R.id.view_key) == getViewType(position)) {
                view = v;
            }
        }
        mCacheView.remove(view);
        return view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getViewByViewType(position);
        if (view == null) {
            view = createView(position);
            view.setTag(R.id.view_key, getViewType(position));
        }
        bindData(view, position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        mCacheView.add(view);
    }

}
