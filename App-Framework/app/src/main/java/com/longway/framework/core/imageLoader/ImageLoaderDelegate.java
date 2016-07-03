package com.longway.framework.core.imageLoader;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.longway.framework.AndroidApplication;

import java.io.File;

/**
 * Created by longway on 16/6/6.
 * Email:longway1991117@sina.com
 */

public class ImageLoaderDelegate implements IImageLoader {
    private GlideManager manager = GlideManager.getInstance(AndroidApplication.getInstance());

    @Override
        public void initialize() {

    }

    @Override
    public void clearMemoryCache() {
        manager.clearMemoryCache();
    }

    @Override
    public void clearDiskCache() {
        manager.clearDiskCache();
    }

    @Override
    public void clearCache() {
        manager.clearCache();
    }

    @Override
    public void trim(int level) {
        manager.trimMemory(level);
    }

    @Override
    public long getDiskCacheSize() {
        return 0;
    }

    @Override
    public void trimDiskCache() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void loadImage(Activity activity, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        manager.loadImage(activity, url, placeholderDrawableId, errorDrawableId, imageView);
    }
    @Override
    public void loadImage(Activity activity, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).load(file).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    @Override
    public void loadImage(Activity activity, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).loadFromMediaStore(mediaStoreUri).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(Activity activity, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).load(drawableId).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    @Override
    public void loadImage(FragmentActivity activity, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).load(url).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(FragmentActivity activity, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).load(file).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(FragmentActivity activity, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).loadFromMediaStore(mediaStoreUri).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(FragmentActivity activity, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(activity).load(drawableId).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(Context context, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(context).load(url).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(Context context, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(context).load(file).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(Context context, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(context).loadFromMediaStore(mediaStoreUri).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(Context context, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(context).load(drawableId).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(Fragment fragment, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(fragment).load(url).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(Fragment fragment, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(fragment).load(file).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(Fragment fragment, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(fragment).loadFromMediaStore(mediaStoreUri).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }
    @Override
    public void loadImage(Fragment fragment, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        Glide.with(fragment).load(drawableId).centerCrop().placeholder(placeholderDrawableId).error(errorDrawableId).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

}
