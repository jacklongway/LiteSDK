package com.longway.framework.core.user.register;

import com.longway.framework.core.user.User;

/**
 * Created by longway on 16/6/11.
 * Email:longway1991117@sina.com
 */

public interface IRegister {
    void getCode(String phoneNumber, RegisterListener registerListener);

    void register(User user, String code, String password, String confirmPassword, RegisterListener registerListener);
}
