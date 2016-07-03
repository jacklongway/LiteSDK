package com.longway.framework.ui.activities;

import com.longway.framework.ui.lifecycle.IActivityLifecycle;
import com.longway.framework.ui.lifecycle.IComponent;
import com.longway.framework.ui.lifecycle.LifecycleContainer;

/**
 * Created by longway on 16/4/23.
 * Email:longway1991117@sina.com
 */
public abstract class XBaseHeaderActivity extends BaseHeaderActivity implements IComponent {
    private LifecycleContainer mLifecycleContainer = new LifecycleContainer();

    @Override
    public void addComponent(IActivityLifecycle lifecycle) {
        mLifecycleContainer.addComponent(lifecycle);
    }

    @Override
    public void removeComponent(IActivityLifecycle lifecycle) {
        mLifecycleContainer.removeComponent(lifecycle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifecycleContainer.activityCompleteVisible();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mLifecycleContainer.activityVisibleFromInvisible();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLifecycleContainer.activityPartialVisible();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLifecycleContainer.activityInVisible();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleContainer.activityOnDestroy();
    }
}
