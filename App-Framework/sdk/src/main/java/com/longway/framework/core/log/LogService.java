package com.longway.framework.core.log;

import android.app.IntentService;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import com.longway.framework.util.FileUtils;

import java.io.File;

/**
 * Created by longway on 16/6/29.
 * Email:longway1991117@sina.com
 */

public class LogService extends IntentService {
    private static final String TAG = LogService.class.getSimpleName();
    public static final int PERFORMANCE_MEMORY = 0x01;
    public static final String TRIM_MEMORY = "trim_memory";
    public static final String GC = "GC";
    public static final String DUMP_TYPE = "dump_type";
    public static final String DUMP_FILENAME = "dump_memory_info";
    public static final String DUMP_DIR = "dump";
    public static final String DUMP_SUFFIX = ".txt";
    public static final String PROCESS_ID = "process_id";
    public static final String EVENT = "dump_event";
    public static final String TRIM_MEMORY_LEVEL = "trim_memory_level";

    private final File mDumpDir;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public LogService() {
        super(TAG);
        mDumpDir = FileUtils.getCacheDir(this, DUMP_DIR);
    }

    private void dumpPerformanceInfo(Intent intent) {
        int pid = intent.getIntExtra(PROCESS_ID, 0);
        if (pid == 0) {
            pid = Process.myPid();
        }
        Log.d(TAG, "pid:" + pid);
        String event = intent.getStringExtra(EVENT);
        if (event == null) {
            event = "unknown";
        }
        int trimLevel = intent.getIntExtra(TRIM_MEMORY_LEVEL, 0);
        boolean success = FileUtils.save2File(mDumpDir.getAbsolutePath(), DUMP_FILENAME.concat(DUMP_SUFFIX), PerformanceMonitor.dumpProcessMemoryInfo(this, pid, event, trimLevel), true);
        Log.d(TAG, "pid " + pid + " dump status " + success);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int type = intent.getIntExtra(DUMP_TYPE, -1);
        if (type == -1) {
            return;
        }
        switch (type) {
            case PERFORMANCE_MEMORY:
                dumpPerformanceInfo(intent);
                break;
            default:
                break;
        }
    }
}
