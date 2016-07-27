package com.longway.framework.core.imageLoader;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.longway.framework.executor.ThreadPoolManager;
import com.longway.framework.executor.ThreadPoolTask;

import java.io.File;

/**
 * Email:longway1991117@sina.com
 */
public class GlideManager {

    private static volatile GlideManager sGlideManager;
    private Glide mGlide;

    private GlideManager(Context context) {
        mGlide = Glide.get(context);
    }

    public static GlideManager getInstance(Context context) {
        if (sGlideManager == null) {
            synchronized (GlideManager.class) {
                if (sGlideManager == null) {
                    sGlideManager = new GlideManager(context);
                }
            }
        }
        return sGlideManager;
    }

    public void clearDiskCache() {
        synchronized (GlideManager.class) {
            ThreadPoolManager.getInstance().postLongTask(new ThreadPoolTask("clearDiskCache") {
                @Override
                public void doTask(Object parameter) {
                    mGlide.clearDiskCache();
                }
            });
        }
    }

    public void clearMemoryCache() {
        mGlide.clearMemory();
    }

    public void clearCache() {
        clearMemoryCache();
        clearDiskCache();
    }

    public void trimMemory(final int level) {
        mGlide.trimMemory(level);
    }

    public static void clear(View view) {
        Glide.clear(view);
    }

    public void loadImage(Activity activity, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).load(url).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(Activity activity, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).load(file).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(Activity activity, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).loadFromMediaStore(mediaStoreUri).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(Activity activity, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).load(drawableId).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }


    public void loadImage(FragmentActivity activity, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).load(url).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(FragmentActivity activity, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).load(file).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(FragmentActivity activity, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).loadFromMediaStore(mediaStoreUri).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(FragmentActivity activity, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).load(drawableId).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(Context context, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(context).load(url).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(Context context, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(context).load(file).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(Context context, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(context).loadFromMediaStore(mediaStoreUri).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(Context context, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(context).load(drawableId).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(Fragment fragment, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(fragment).load(url).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(Fragment fragment, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(fragment).load(file).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(Fragment fragment, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(fragment).loadFromMediaStore(mediaStoreUri).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public void loadImage(Fragment fragment, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(fragment).load(drawableId).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

}
