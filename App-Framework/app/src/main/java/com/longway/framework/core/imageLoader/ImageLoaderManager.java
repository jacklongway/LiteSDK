package com.longway.framework.core.imageLoader;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.longway.framework.core.common.AbsManager;

import java.io.File;

/**
 * Created by longway on 16/6/6.
 * Email:longway1991117@sina.com
 */

public class ImageLoaderManager extends AbsManager<IImageLoader> implements IImageLoader {
    private static volatile ImageLoaderManager sInstance;

    private ImageLoaderManager() {

    }

    public static ImageLoaderManager getInstance() {
        if (sInstance == null) {
            synchronized (ImageLoaderManager.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoaderManager();
                }
            }
        }
        return sInstance;
    }

    private final IImageLoader mDelegate = new ImageLoaderDelegate();

    @Override
    public IImageLoader getCurrentUse() {
        IImageLoader iImageLoader = super.getCurrentUse();
        if (iImageLoader == null) {
            iImageLoader = mDelegate;
        }
        return iImageLoader;
    }

    @Override
    public void initialize() {
        getCurrentUse().initialize();
    }

    @Override
    public void clearMemoryCache() {
        getCurrentUse().clearMemoryCache();
    }

    @Override
    public void clearDiskCache() {
        getCurrentUse().clearDiskCache();
    }

    @Override
    public void clearCache() {
        getCurrentUse().clearCache();
    }

    @Override
    public void trim(int level) {
        getCurrentUse().trim(level);
    }

    @Override
    public long getDiskCacheSize() {
        return getCurrentUse().getDiskCacheSize();
    }

    @Override
    public void trimDiskCache() {
        getCurrentUse().trimDiskCache();
    }

    @Override
    public void pause() {
        getCurrentUse().pause();
    }

    @Override
    public void resume() {
        getCurrentUse().resume();
    }

    @Override
    public void destroy() {
        getCurrentUse().destroy();
    }

    @Override
    public void loadImage(Activity activity, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(activity, url, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(Activity activity, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(activity, file, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(Activity activity, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(activity, mediaStoreUri, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(Activity activity, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(activity, drawableId, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(FragmentActivity activity, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(activity, url, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(FragmentActivity activity, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(activity, file, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(FragmentActivity activity, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(activity, mediaStoreUri, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(FragmentActivity activity, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(activity, drawableId, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(Context context, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(context, url, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(Context context, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(context, file, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(Context context, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(context, mediaStoreUri, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(Context context, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(context, drawableId, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(Fragment fragment, String url, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(fragment, url, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(Fragment fragment, File file, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(fragment, file, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(Fragment fragment, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(fragment, mediaStoreUri, placeholderDrawableId, errorDrawableId, imageView);
    }

    @Override
    public void loadImage(Fragment fragment, int drawableId, int placeholderDrawableId, int errorDrawableId, ImageView imageView) {
        getCurrentUse().loadImage(fragment, drawableId, placeholderDrawableId, errorDrawableId, imageView);
    }
}
