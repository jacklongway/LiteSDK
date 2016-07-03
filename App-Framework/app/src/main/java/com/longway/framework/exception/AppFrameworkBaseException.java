package com.longway.framework.exception;

/*********************************
 * Created by longway on 16/5/17 下午3:05.
 * packageName:com.longway.framework.exception
 * projectName:MPTPAPP
 * Email:longway1991117@sina.com
 ********************************/
public class AppFrameworkBaseException extends RuntimeException {
    public AppFrameworkBaseException(String message) {
        super(message);
    }

    public AppFrameworkBaseException(Throwable throwable) {
        super(throwable);
    }
}
