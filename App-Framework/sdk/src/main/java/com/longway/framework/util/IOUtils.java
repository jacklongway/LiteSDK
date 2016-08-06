package com.longway.framework.util;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {

    private IOUtils() {
    }

    public static void closeQuietly(Closeable... closeable) {
        if (closeable != null) {
            for (Closeable c : closeable) {
                try {
                    if (c != null) {
                        c.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void closeQuietly(AutoCloseable... closeable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (closeable != null) {
                for (AutoCloseable autoCloseable : closeable) {
                    try {
                        if (autoCloseable != null) {
                            autoCloseable.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void closeQuietly(Cursor... cursor) {
        if (cursor != null) {
            for (Cursor c : cursor) {
                try {
                    if (c != null) {
                        c.close();
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }
}
