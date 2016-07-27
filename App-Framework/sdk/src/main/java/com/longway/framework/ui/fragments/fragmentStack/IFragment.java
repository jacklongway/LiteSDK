package com.longway.framework.ui.fragments.fragmentStack;

import android.os.Bundle;

/**
 * Created by longway on 16/4/23.
 * Email:longway1991117@sina.com
 */
public interface IFragment {
    void onEnter();

    void onLeave();

    void onBack();

    void onBack(Bundle bundle);

    boolean processBackKey();
}
