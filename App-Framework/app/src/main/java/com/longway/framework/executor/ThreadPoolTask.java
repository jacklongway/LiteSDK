package com.longway.framework.executor;

/**
 *
 */
public abstract class ThreadPoolTask<T> implements Runnable {
    protected String mName;
    protected T mParameter;
    private boolean mCanceled = false;
    private boolean mFinished = false;
    private boolean mRunning = false;

    public boolean isCanceled() {
        return mCanceled;
    }

    public boolean isFinished() {
        return mFinished;
    }

    public boolean isRunning() {
        return mRunning;
    }

    public void setCanceled(boolean canceled) {
        this.mCanceled = canceled;
    }

    public abstract void doTask(T parameter);

    public ThreadPoolTask(String name) {
        mName = name;
    }

    public String getName() {
        return (mName == null ? "" : mName);
    }

    public ThreadPoolTask setParameter(T parameter) {
        mParameter = parameter;
        return this;
    }

    protected void onCancel() {

    }

    @Override
    public void run() {
        if (!mCanceled) {
            mRunning = true;
            doTask(mParameter);
            mFinished = true;
        } else {
            onCancel();
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
