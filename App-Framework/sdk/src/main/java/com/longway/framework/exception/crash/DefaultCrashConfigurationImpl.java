package com.longway.framework.exception.crash;

/*********************************
 * Created by longway on 16/5/17 下午4:17.
 * packageName:com.longway.framework.exception.crash
 * projectName:MPTPAPP
 * Email:longway1991117@sina.com
 ********************************/
public class DefaultCrashConfigurationImpl implements ICrashConfiguration {
    private static final int DELAY = 2000;

    @Override
    public boolean restartApp() {
        return true;
    }

    @Override
    public void uploadCrashInfoToServer(CrashInfo crashInfo) {

    }

    @Override
    public int restartDelayTime() {
        return DELAY;
    }

    @Override
    public boolean onSelfCompleteHandlerCrash() {
        return false;
    }
}
