package com.longway.framework.cache.memorycache;

import com.longway.framework.bean.BaseBean;

/**
 * Created by longway on 16/6/6.
 * Email:longway1991117@sina.com
 */

public class CustomBeanLruMemoryCache extends LruMemoryCache<String, BaseBean> {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public CustomBeanLruMemoryCache(int maxSize) {
        super(maxSize);
    }
    
}
