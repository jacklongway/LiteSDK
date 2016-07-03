package com.longway.framework.ui.lifecycle;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by longway on 16/4/23.
 * Email:longway1991117@sina.com
 */
public class LifecycleContainer implements IComponent, IActivityLifecycle {
    private ConcurrentHashMap<String, IActivityLifecycle> mComponent = new ConcurrentHashMap<>();

    public LifecycleContainer() {

    }

    @Override
    public void addComponent(IActivityLifecycle lifecycle) {
        if (lifecycle == null) {
            return;
        }
        if (mComponent == null) {
            return;
        }
        mComponent.put(lifecycle.toString(), lifecycle);
    }

    @Override
    public void removeComponent(IActivityLifecycle lifecycle) {
        if (lifecycle == null) {
            return;
        }
        if (mComponent == null) {
            return;
        }
        mComponent.remove(lifecycle.toString());
    }

    @Override
    public void activityCompleteVisible() {
        if (mComponent == null) {
            return;
        }
        Enumeration<String> keys = mComponent.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            IActivityLifecycle lifecycle = mComponent.get(key);
            if (lifecycle != null) {
                lifecycle.activityCompleteVisible();
            }
        }
    }

    @Override
    public void activityPartialVisible() {
        if (mComponent == null) {
            return;
        }
        Enumeration<String> keys = mComponent.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            IActivityLifecycle lifecycle = mComponent.get(key);
            if (lifecycle != null) {
                lifecycle.activityPartialVisible();
            }
        }
    }

    @Override
    public void activityVisibleFromInvisible() {
        if (mComponent == null) {
            return;
        }
        Enumeration<String> keys = mComponent.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            IActivityLifecycle lifecycle = mComponent.get(key);
            if (lifecycle != null) {
                lifecycle.activityVisibleFromInvisible();
            }
        }
    }

    @Override
    public void activityInVisible() {
        if (mComponent == null) {
            return;
        }
        Enumeration<String> keys = mComponent.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            IActivityLifecycle lifecycle = mComponent.get(key);
            if (lifecycle != null) {
                lifecycle.activityInVisible();
            }
        }
    }

    @Override
    public void activityOnDestroy() {
        if (mComponent == null) {
            return;
        }
        Enumeration<String> keys = mComponent.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            IActivityLifecycle lifecycle = mComponent.get(key);
            if (lifecycle != null) {
                lifecycle.activityOnDestroy();
            }
        }
    }
}
