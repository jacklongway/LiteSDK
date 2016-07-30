package com.longway.framework.core.photo;

import com.longway.framework.exception.AppFrameworkBaseException;

/**
 * Created by longway on 16/7/29.
 * Email:longway1991117@sina.com
 */

public class PhotoException extends AppFrameworkBaseException {
    public PhotoException(int code, String message) {
        super(code, message);
    }
    public PhotoException(String message) {
        super(message);
    }

}
