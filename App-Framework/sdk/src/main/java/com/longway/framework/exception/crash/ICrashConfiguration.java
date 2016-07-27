package com.longway.framework.exception.crash;

/*********************************
 * Created by longway on 16/5/17 下午2:58.
 * packageName:com.longway.framework
 * projectName:MPTPAPP
 * Email:longway1991117@sina.com
 ********************************/
public interface ICrashConfiguration {
    /**
     * 是否重新启动app
     *
     * @return
     */
    boolean restartApp();

    /**
     * 上传crash信息到服务端
     *
     * @param crashInfo
     */
    void uploadCrashInfoToServer(CrashInfo crashInfo);

    /**
     * 重启延迟时间
     *
     * @return
     */
    int restartDelayTime();

    /**
     * 自己已经处理,无需框架来处理
     *
     * @return
     */
    boolean onSelfCompleteHandlerCrash();

}
