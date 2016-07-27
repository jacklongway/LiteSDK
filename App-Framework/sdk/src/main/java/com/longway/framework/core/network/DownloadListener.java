package com.longway.framework.core.network;

/**
 * Created by longway on 16/6/17.
 * Email:longway1991117@sina.com
 */

public interface DownloadListener extends NetworkListener {
    void onProgress(long downloaded, long total);
}
