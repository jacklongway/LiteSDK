package com.longway.framework.core.user.personalInfo;

import com.longway.framework.core.user.User;

/**
 * Created by longway on 16/6/11.
 * Email:longway1991117@sina.com
 */

public interface IPersonalInfo {
    void modifyAvatar(User user, PersonalInfoListener personalInfoListener);

    void updatePersonInfo(User user, PersonalInfoListener personalInfoListener);
}
