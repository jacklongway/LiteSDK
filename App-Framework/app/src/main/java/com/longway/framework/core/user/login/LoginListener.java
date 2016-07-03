package com.longway.framework.core.user.login;

import com.longway.framework.core.user.User;

/**
 * Created by longway on 16/6/11.
 * Email:longway1991117@sina.com
 */

public interface LoginListener {
    void onStart(User user);

    void onError(Exception ex, User user);

    void onComplete(int code, String errorMsg, User user);
}
