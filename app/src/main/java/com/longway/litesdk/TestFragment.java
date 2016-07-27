package com.longway.litesdk;

import android.os.Bundle;
import android.util.Log;

import com.longway.framework.ui.fragments.BaseFragment;

/**
 * Created by longway on 16/7/23.
 * Email:longway1991117@sina.com
 */

public class TestFragment extends BaseFragment {
    private static final String TAG = TestFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        Log.e(TAG, bundle.getString("msg"));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
