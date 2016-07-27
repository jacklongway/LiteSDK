package com.longway.framework.core.user.login;

import com.longway.framework.core.common.AbsManager;
import com.longway.framework.core.user.User;

/**
 * Created by longway on 16/6/11.
 * Email:longway1991117@sina.com
 */

public class LoginManager extends AbsManager<ILogin> implements ILogin {

    private static volatile LoginManager sLoginManager;

    private LoginManager() {

    }

    public static LoginManager getInstance() {
        if (sLoginManager == null) {
            synchronized (LoginManager.class) {
                if (sLoginManager == null) {
                    sLoginManager = new LoginManager();
                }
            }
        }
        return sLoginManager;
    }

    private ILogin mDelegate = new LoginDelegate();

    @Override
    public ILogin getCurrentUse() {
        ILogin login = super.getCurrentUse();
        if (login == null) {
            login = mDelegate;
        }
        return login;
    }

    @Override
    public void login(User user, LoginListener loginListener) {
        getCurrentUse().login(user, loginListener);
    }

    @Override
    public void logout(User user, LoginListener loginListener) {
        getCurrentUse().logout(user, loginListener);
    }

    @Override
    public boolean isLogin(User user) {
        return getCurrentUse().isLogin(user);
    }
}
