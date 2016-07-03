package com.longway.framework.cache.memorycache;

import android.graphics.Bitmap;
import android.os.Build;

/**
 * Created by longway on 16/6/6.
 * Email:longway1991117@sina.com
 */

public class CustomBitmapLruMemoryCache extends LruMemoryCache<String, Bitmap> {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public CustomBitmapLruMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        int size = 0;
        if (value != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                size += value.getAllocationByteCount();
            } else {
                size += value.getByteCount();
            }
        }
        return size;
    }
}
