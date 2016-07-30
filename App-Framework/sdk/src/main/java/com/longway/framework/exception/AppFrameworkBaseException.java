package com.longway.framework.exception;

/*********************************
 * Created by longway on 16/5/17 下午3:05.
 * packageName:com.longway.framework.exception
 * projectName:MPTPAPP
 * Email:longway1991117@sina.com
 ********************************/
public class AppFrameworkBaseException extends RuntimeException {
    private int mCode;

    public int getCode() {
        return mCode;
    }

    public AppFrameworkBaseException(String message) {
        super(message);
    }

    public AppFrameworkBaseException(int code, String message) {
        super(message);
        this.mCode = code;
    }

    public AppFrameworkBaseException(Throwable throwable) {
        super(throwable);
    }

    public AppFrameworkBaseException(int code, Throwable throwable) {
        super(throwable);
        this.mCode = code;
    }
}
