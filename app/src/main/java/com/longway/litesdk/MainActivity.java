package com.longway.litesdk;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.longway.framework.core.Router.Router;
import com.longway.framework.core.Router.RouterParams;
import com.longway.framework.core.network.NetworkListener;
import com.longway.framework.core.network.NetworkManager;
import com.longway.framework.core.network.base.Request;
import com.longway.framework.core.network.base.Response;
import com.longway.framework.ui.activities.BaseActivity;
import com.longway.framework.util.ToastUtils;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkManager networkManager = NetworkManager.getInstance(getApplicationContext());
        //networkManager.addAssetsCert("https://kyfw.12306.cn/", "SRCA.crt");
        final Request request = new Request("https://kyfw.12306.cn/otn/leftTicket/init");
        networkManager.executeGetAsync(request, new NetworkListener() {
            @Override
            public void onStart(Request request) {
                Log.e(TAG, request.getURL());
            }

            @Override
            public void onError(Request request, Exception ex) {
                Log.e(TAG, ex.toString());
            }

            @Override
            public void onComplete(Response response) {
                Log.e(TAG, response.getBody());
            }
        });

    }

    @Override
    protected String getCloseWaringMsg() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.container;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RouterConstants.HTTPSACTIVITY_REQUESTCODE:
                    ToastUtils.showToast(getApplicationContext(), data.getStringExtra("msg"), true);
                    break;
            }
        } else {

        }
    }

    Router router = Router.getInstance();

    public void start(View v) {
        String key = RouterConstants.HTTPSACTIVITY_KEY;
        router.routerActivityForResult(this, key);
    }

    public void startService(View v) {
        RouterParams routerParams = new RouterParams();
        routerParams.mClz = TestService.class;
        Bundle bundle = new Bundle();
        bundle.putString("msg", "service");
        routerParams.mParams = bundle;
        routerParams.mKey = "params";
        //router.routerStartService(this, routerParams);
        router.routerBinderService(this, routerParams, serviceConnection);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e(TAG, iBinder.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public void startFragment(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("msg", "fragment");
        router.routerFragment(this, TestFragment.class, bundle);
    }
}
