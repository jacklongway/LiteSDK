package com.longway.framework.report;

import com.longway.framework.AndroidApplication;
import com.longway.framework.util.FileUtils;

import java.util.Locale;

/**
 * 记录主线程上一些可能导致ANR的情况
 */
public class RecordAnrTask {
    private static final String DIR = AndroidApplication.getInstance().getCacheDir().getAbsolutePath();
    private static final String FILE_NAME = "to_be_anr.txt";

    /**
     * 保存广播时候，可能发生anr的情况
     *
     * @param broadcastName 广播回调类的名字
     * @param gap           时间差
     */
    public static void asyncRecordBroadcast(String broadcastName, long gap) {
        if (gap > 500) {
            String text = String.format(Locale.CHINESE, "BroadcastReceiver|%s|%d", broadcastName, gap);
            FileUtils.asyncSave2File(DIR, FILE_NAME, text);
        }
    }

    /**
     * 使用线程池类的主线程
     *
     * @param threadName 线程的名字
     * @param gap        时间差
     */
    public static void asyncRecordMainThread(String threadName, long gap) {

        if (gap > 500) {
            String text = String.format(Locale.CHINESE, "PostTaskMainThread|%s|%d", threadName, gap);
            FileUtils.asyncSave2File(DIR, FILE_NAME, text);
        }
    }
}
