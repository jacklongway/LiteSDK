package com.longway.framework.core.user.login;

import com.longway.framework.core.user.User;

/**
 * Created by longway on 16/6/11.
 * Email:longway1991117@sina.com
 */

public class LoginDelegate implements ILogin {
    @Override
    public void login(User user, LoginListener loginListener) {
    }

    @Override
    public void logout(User user, LoginListener loginListener) {

    }

    @Override
    public boolean isLogin(User user) {
        return false;
    }
}
