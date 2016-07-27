package com.longway.litesdk;

import com.longway.framework.AndroidApplication;
import com.longway.framework.core.Router.Router;
import com.longway.framework.core.Router.RouterParams;

/**
 * Created by longway on 16/7/23.
 * Email:longway1991117@sina.com
 */

public class TestApplication extends AndroidApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        RouterParams routerParams = new RouterParams();
        routerParams.mClz = HttpsActivity.class;
        routerParams.mRequestCode = RouterConstants.HTTPSACTIVITY_REQUESTCODE;
        Router.getInstance().registerActivity(RouterConstants.HTTPSACTIVITY_KEY, routerParams);
    }
}
