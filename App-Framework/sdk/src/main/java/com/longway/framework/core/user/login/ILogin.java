package com.longway.framework.core.user.login;

import com.longway.framework.core.user.User;

/**
 * Created by longway on 16/6/11.
 * Email:longway1991117@sina.com
 */

public interface ILogin {
    void login(User user, LoginListener loginListener);

    void logout(User user, LoginListener loginListener);

    boolean isLogin(User user);
}
