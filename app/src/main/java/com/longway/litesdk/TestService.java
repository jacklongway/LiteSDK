package com.longway.litesdk;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by longway on 16/7/23.
 * Email:longway1991117@sina.com
 */

public class TestService extends Service {
    private static final String TAG = TestService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    private class LocalBinder extends Binder {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "instance");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle bundle = intent.getBundleExtra("params");
        Log.e(TAG, bundle.getString("msg"));
    }
}
