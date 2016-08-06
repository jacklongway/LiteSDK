package com.longway.framework.version;

/**
 * Created by longway on 16/8/6.
 * Email:longway1991117@sina.com
 */

public interface IVersionChecker {
    void onCheckStart();

    void onCheckSuccess(String result);

    void onCheckError(String errorMsg);
}

