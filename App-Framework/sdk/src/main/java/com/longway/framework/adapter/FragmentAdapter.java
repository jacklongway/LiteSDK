package com.longway.framework.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.longway.framework.ui.fragments.fragmentStack.FragmentBuildParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载fragment
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<FragmentBuildParams> mFragments;
    private Context mCtx;

    public FragmentAdapter(FragmentManager fm, Context context, ArrayList<FragmentBuildParams> fragments) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.mCtx = context;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        FragmentBuildParams fragmentBuildParams = mFragments.get(arg0);
        return Fragment.instantiate(mCtx, fragmentBuildParams.mClz.getName(), fragmentBuildParams.mParams);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mFragments != null ? mFragments.size() : 0;
    }
}
