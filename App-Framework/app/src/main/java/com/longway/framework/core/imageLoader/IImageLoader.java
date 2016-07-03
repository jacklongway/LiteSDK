package com.longway.framework.core.imageLoader;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by longway on 16/6/6.
 * Email:longway1991117@sina.com
 */

public interface IImageLoader {
    void initialize();

    void clearMemoryCache();

    void clearDiskCache();

    void clearCache();

    void trim(int level);

    long getDiskCacheSize();

    void trimDiskCache();

    void pause();

    void resume();

    void destroy();

    void loadImage(Activity activity, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(Activity activity, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(Activity activity, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(Activity activity, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(FragmentActivity activity, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(FragmentActivity activity, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(FragmentActivity activity, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(FragmentActivity activity, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(Context context, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(Context context, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(Context context, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(Context context, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(Fragment fragment, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(Fragment fragment, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(Fragment fragment, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView);

    void loadImage(Fragment fragment, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView);
}
