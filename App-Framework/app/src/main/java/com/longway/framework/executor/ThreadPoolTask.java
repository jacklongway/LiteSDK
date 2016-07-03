package com.longway.framework.executor;

/**
 *
 */
public abstract class ThreadPoolTask<T> implements Runnable {
    protected String mName;
    protected T mParameter;

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

    @Override
    public void run() {
        doTask(mParameter);
    }

    @Override
    public String toString() {
        return getName();
    }
}
