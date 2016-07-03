package com.longway.framework.ui.lifecycle;

/**
 * Created by longway on 16/4/23.
 * Email:longway1991117@sina.com
 */
public interface IComponent {
    void addComponent(IActivityLifecycle lifecycle);
    void removeComponent(IActivityLifecycle lifecycle);
}
