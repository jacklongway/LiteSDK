package com.longway.framework.executor;

import android.os.Process;

import com.longway.framework.util.LogUtils;

/**
 * Created by longway on 16/4/11.
 * Email:longway1991117@sina.com
 * 自定义线程池异步任务
 */
public abstract class ThreadPoolAsyncTask<Params, Progress, Result> implements Runnable {

    private String mName;
    private Params[] mParams;

    public ThreadPoolAsyncTask(String name, Params... params) {
        this.mName = name;
        this.mParams = params;
    }

    @Override
    public void run() {
        // 设置线程优先级为后台,可能提高ui优先级(time slice)
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        final Result result = doInBackground(mParams);
        ThreadPoolTask<String> task = new ThreadPoolTask<String>(mName + "run()") {
            @Override
            public void doTask(String parameter) {
                onPostExecute(result);
            }
        };
        UiThreadManager.postRunnable(task);
    }

    /**
     * 后台业务执行
     *
     * @param params
     * @return
     */
    abstract Result doInBackground(Params... params);

    /**
     * 进度发布
     *
     * @param value
     */
    public void pushProgress(final Progress... value) {
        ThreadPoolTask<Void> task = new ThreadPoolTask<Void>(mName + "publishProgress(Progress... value)") {
            @Override
            public void doTask(Void parameter) {
                onProgressUpdate(value);
            }
        };
        UiThreadManager.postRunnable(task);
    }

    /**
     * 执行结果提交
     *
     * @param result
     */
    protected void onPostExecute(Result result) {
        LogUtils.d(result != null ? result.toString() : "null");
    }

    /**
     * 进度接收更新
     *
     * @param value
     */
    protected void onProgressUpdate(Progress... value) {
        LogUtils.d(value != null ? value.toString() : "null");

    }


}
