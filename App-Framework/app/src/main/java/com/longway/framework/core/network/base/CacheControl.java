package com.longway.framework.core.network.base;

/**
 * Created by longway on 16/6/16.
 * Email:longway1991117@sina.com
 */

public class CacheControl {
    public static final int CACHE = 0;
    public static final int NETWORK = 1;
    public static final int CACHE_NETWORK = 2;
    public static final int NETWORK_CACHE = 3;
    private final int mCacheType;

    public CacheControl(int cacheType) {
        this.mCacheType = cacheType;
    }

    public int getCacheType() {
        return mCacheType;
    }

    public static CacheControl getCacheControl(int cacheType) {
        if (cacheType < CACHE || cacheType > NETWORK_CACHE) {
            throw new IllegalArgumentException("cacheType between 0 in 3");
        }
        return new CacheControl(cacheType);
    }

    private String cacheTypeConvert(int cacheType) {
        String cache = "no-cache";
        switch (cacheType) {
            case CACHE:
                cache = "only-if-cached";
                break;
            case NETWORK:
                cache = "no-cache";
                break;
            case CACHE_NETWORK:
                cache = "only-if-cached";
                break;
            case NETWORK_CACHE:
                cache = "no-cache";
                break;
            default:
                break;
        }
        return cache;
    }

    public String getCacheHeaderProperty() {
        return cacheTypeConvert(mCacheType);
    }
}
