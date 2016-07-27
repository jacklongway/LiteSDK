package com.longway.framework.executor;


import com.longway.framework.util.LogUtils;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadPoolExecutorWarp extends ThreadPoolExecutor {
    private ArrayList<Runnable> mRunningTaskArray = null;
    private Object mRunningTaskArrayLock;
    private ReentrantLock mPauseReentrantLock = new ReentrantLock();
    private Condition mPauseCondition = mPauseReentrantLock.newCondition();
    private AtomicBoolean mPause = new AtomicBoolean();

    public ThreadPoolExecutorWarp(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        mRunningTaskArray = new ArrayList<Runnable>();
        mRunningTaskArrayLock = new Object();
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        synchronized (mRunningTaskArrayLock) {
            mRunningTaskArray.add(r);
        }
        mPauseReentrantLock.lock();
        try {
            while (mPause.get()) {
                mPauseCondition.await();
            }
        } catch (InterruptedException e) {
            t.interrupt();
            e.printStackTrace();
        } finally {
            mPauseReentrantLock.unlock();
        }
    }

    public void pause() {
        if (mPause.get()) {
            return;
        }
        mPauseReentrantLock.lock();
        try {
            if (mPause.get()) {// recheck
                return;
            }
            mPause.set(true);
        } finally {
            mPauseReentrantLock.unlock();
        }
    }

    @Override
    protected void terminated() {
        super.terminated();
    }

    public void resume() {
        if (!mPause.get()) {
            return;
        }
        mPauseReentrantLock.lock();
        try {
            if (!mPause.get()) {// recheck
                return;
            }
            mPause.set(false);
            mPauseCondition.signalAll();
        } finally {
            mPauseReentrantLock.unlock();
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        synchronized (mRunningTaskArrayLock) {
            mRunningTaskArray.remove(r);
        }
    }

    public void printInfo() {
        StringBuffer sb = new StringBuffer();
        synchronized (mRunningTaskArrayLock) {
            for (Runnable runnable : mRunningTaskArray) {
                if (runnable != null) {
                    sb.append(runnable.toString()).append(", ");
                }
            }
        }
        if (sb.length() > 0) {
            LogUtils.d("cur thread:" + sb.toString());
        }
    }
}
