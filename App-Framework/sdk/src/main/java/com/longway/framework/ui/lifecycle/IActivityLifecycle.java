package com.longway.framework.ui.lifecycle;

/**
 * Created by longway on 16/4/23.
 * Email:longway1991117@sina.com
 */
public interface IActivityLifecycle {
    void activityCompleteVisible();

    void activityPartialVisible();

    void activityVisibleFromInvisible();

    void activityInVisible();

    void activityOnDestroy();
}
