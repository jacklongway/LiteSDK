package com.longway.framework.core.common;

/**
 * Created by longway on 16/6/6.
 * Email:longway1991117@sina.com
 */

public interface IBusinessComponent {
    AbsManager addBusinessComponent(String key, AbsManager absManager);

    AbsManager removeBusinessComponent(String key);

    AbsManager getBusinessComponent(String key);
}
