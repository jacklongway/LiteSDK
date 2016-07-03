package com.longway.framework.executor;

import android.os.AsyncTask;

import com.longway.elabels.BuildConfig;
import com.longway.framework.util.LogUtils;

import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程管理类，所有线程资源的申请都应该在这里
 */
public class ThreadPoolManager {
    // 是否打印线程情况
    private static final boolean OPTIMIZE_DEBUG = BuildConfig.DEBUG;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAX_POOL_SIZE = CPU_COUNT << 1 + 1;
    // 线程池的分类
    public static final int LONG_TASK_POOL = 0;
    public static final int SHORT_TASK_POOL = 1;
    public static final int PIC_TASK_POOL = 2;
    private static final int POOL_COUNT = 3;

    private volatile static ThreadPoolManager sInstance = null;

    // 不同线程池的容器
    private ThreadPoolExecutorWarp[] mPoolArray = null;
    private ExecutorService mDebugThreadPool = null;

    public static ThreadPoolManager getInstance() {
        if (sInstance == null) {
            synchronized (ThreadPoolManager.class) {
                if (sInstance == null) {
                    sInstance = new ThreadPoolManager();
                }
            }
        }
        return sInstance;
    }

    private ThreadPoolManager() {
        mPoolArray = new ThreadPoolExecutorWarp[POOL_COUNT];
        // 耗时任务
        mPoolArray[LONG_TASK_POOL] = new ThreadPoolExecutorWarp(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());  // 等价于Executors.newCachedThreadPool();
        // 短小任务
        mPoolArray[SHORT_TASK_POOL] = new ThreadPoolExecutorWarp(CORE_POOL_SIZE, MAX_POOL_SIZE, 10L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(128));
        // 图片处理任务
        mPoolArray[PIC_TASK_POOL] = new ThreadPoolExecutorWarp(CORE_POOL_SIZE, MAX_POOL_SIZE, 60L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        // 调优时候查看线程
        if (OPTIMIZE_DEBUG) {
            mDebugThreadPool = Executors.newSingleThreadExecutor();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (ThreadPoolExecutorWarp pool : mPoolArray) {
                        pool.printInfo();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mDebugThreadPool.execute(this);
                }
            };
            mDebugThreadPool.execute(runnable);
        }
    }

    public void pauseTask(int taskType) {
        if (taskType < SHORT_TASK_POOL || taskType > PIC_TASK_POOL) {
            throw new IllegalArgumentException("type must be [0,2]");
        }
        ThreadPoolExecutorWarp poolExecutor = mPoolArray[taskType];
        if (!poolExecutor.isShutdown()) {
            poolExecutor.pause();
        }
    }

    public void resumeTask(int taskType) {
        if (taskType < SHORT_TASK_POOL || taskType > PIC_TASK_POOL) {
            throw new IllegalArgumentException("type must be [0,2]");
        }
        ThreadPoolExecutorWarp poolExecutor = mPoolArray[taskType];
        if (!poolExecutor.isShutdown()) {
            poolExecutor.resume();
        }
    }

    public void pauseAll() {
        ThreadPoolExecutorWarp[] threadPoolExecutorWarps = mPoolArray;
        for (ThreadPoolExecutorWarp threadPoolExecutorWarp : threadPoolExecutorWarps) {
            if (!threadPoolExecutorWarp.isShutdown()) {
                threadPoolExecutorWarp.pause();
            }
        }
    }

    public void resumeAll() {
        ThreadPoolExecutorWarp[] threadPoolExecutorWarps = mPoolArray;
        for (ThreadPoolExecutorWarp threadPoolExecutorWarp : threadPoolExecutorWarps) {
            if (!threadPoolExecutorWarp.isShutdown()) {
                threadPoolExecutorWarp.resume();
            }
        }
    }

    /**
     * 提交耗时任务
     */
    public synchronized void postLongTask(ThreadPoolTask task) {
        ThreadPoolExecutor pool = mPoolArray[LONG_TASK_POOL];
        if (!pool.isShutdown()) {
            pool.execute(task);
        }
    }

    /**
     * 提交短小的任务到后台处理
     */
    public synchronized void postShortTask(ThreadPoolTask task) {
        ThreadPoolExecutor pool = mPoolArray[SHORT_TASK_POOL];
        if (!pool.isShutdown()) {
            pool.execute(task);
        }
    }

    /**
     * 提交短小Future task到后台处理
     *
     * @param task
     * @return
     */
    public synchronized Future postShortFutureTask(ThreadPoolTask task) {
        ThreadPoolExecutor pool = mPoolArray[SHORT_TASK_POOL];
        if (!pool.isShutdown()) {
            return pool.submit(task);
        }
        return null;
    }

    /**
     * 提交异步任务并行执行
     *
     * @param asyncTask
     * @param params
     */
    public synchronized void postAsyncTask(AsyncTask asyncTask, Object... params) {
        ThreadPoolExecutor poolExecutor = mPoolArray[SHORT_TASK_POOL];
        if (!poolExecutor.isShutdown()) {
            asyncTask.executeOnExecutor(poolExecutor, params);
        }
    }

    /**
     * 提交图片的任务到后台处理
     */
    public synchronized void postPicTask(ThreadPoolTask task) {
        ThreadPoolExecutor pool = mPoolArray[PIC_TASK_POOL];
        if (!pool.isShutdown()) {
            pool.execute(task);
        }
    }

    /**
     * 获取处理图片的线程池，暴露出来用于给imageloader之类的开源代码使用
     *
     * @return 图片线程池
     */
    public ThreadPoolExecutor getPicPool() {
        return mPoolArray[PIC_TASK_POOL];
    }

    /**
     * 关闭真个线程池
     */
    public synchronized void exit() {
        for (ThreadPoolExecutor pool : mPoolArray) {
            if (!pool.isShutdown()) {
                pool.shutdownNow();
            }
        }
        mPoolArray = null;
    }


    /**
     * 用于打印线程池现在的情况
     */
    public void printPoolInfo() {
        String msg = String.format(Locale.US, "Long/Short/Pic  %d/%d/%d.  Long largest=%d, Short/Pic wait=%d/%d",
                mPoolArray[LONG_TASK_POOL].getActiveCount(), mPoolArray[SHORT_TASK_POOL].getActiveCount(), mPoolArray[PIC_TASK_POOL].getActiveCount(),
                mPoolArray[LONG_TASK_POOL].getLargestPoolSize(), mPoolArray[SHORT_TASK_POOL].getQueue().size(), mPoolArray[PIC_TASK_POOL].getQueue().size());
        LogUtils.d(msg);
    }
}
