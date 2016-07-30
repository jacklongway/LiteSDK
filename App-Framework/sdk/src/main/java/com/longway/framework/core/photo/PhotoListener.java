package com.longway.framework.core.photo;

import java.io.File;

/**
 * Created by longway on 16/7/12.
 * Email:longway1991117@sina.com
 */

public interface PhotoListener {
    void onComplete(int action,File file);
    void onException(PhotoException ex);
    void onCancel();
}
