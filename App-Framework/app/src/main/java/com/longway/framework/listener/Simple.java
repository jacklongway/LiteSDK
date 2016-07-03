package com.longway.framework.listener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by longway on 16/6/28.
 * Email:longway1991117@sina.com
 */

public class Simple implements BaseListener<Activity,Bundle,View> {
    @Override
    public void onAction(Activity caller, Bundle params, View obj) {

    }
}
