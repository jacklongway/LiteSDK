package com.longway.framework.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.longway.framework.AndroidApplication;

import java.io.File;

/**
 * 通用数据填充器
 *
 * @author longway
 */
public class ViewHold {
    private SparseArray<View> mViews;
    private View mContentView;

    private ViewHold() {
        // TODO Auto-generated constructor stub
        if (mViews == null) {
            mViews = new SparseArray<View>();
        }
    }

    public View getContentView() {
        return mContentView;
    }

    public boolean checkViewInjected(int id) {
        return mViews.get(id, null) != null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getView(int id) {
        T view = (T) mViews.get(id);
        if (view == null) {
            view = (T) mContentView.findViewById(id);
            if (view != null) {
                mViews.put(id, (View) view);
            }
            return view;
        }
        return view;
    }

    public static boolean hasViewHold(View view) {
        if (view != null) {
            Object o = view.getTag();
            if (o != null && o instanceof ViewHold) {
                return true;
            }
        }
        return false;
    }

    public static ViewHold getViewHold(LayoutInflater inflater, View contentView,
                                       int layoutId) {
        if (hasViewHold(contentView)) {
            return (ViewHold) contentView.getTag();
        }
        ViewHold hold = new ViewHold();
        hold.mContentView = inflater
                .inflate(layoutId, null);
        hold.mContentView.setTag(hold);
        return hold;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, String text) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(text);
        }
    }

    public void setTextVisible(int viewId, int visi) {

        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setVisibility(visi);
        }
    }

    public void setImage(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageResource(resId);
        }
    }

    public void setImage(int viewId, Drawable drawable) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }

    public void loadImage(Activity activity, int viewId, int placeholderDrawableId, int errorDrawableId, int drawableResId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(activity, drawableResId, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(FragmentActivity activity, int viewId, int placeholderDrawableId, int errorDrawableId, int drawableResId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(activity, drawableResId, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(Fragment fragment, int viewId, int placeholderDrawableId, int errorDrawableId, int drawableResId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(fragment, drawableResId, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(Context context, int viewId, int placeholderDrawableId, int errorDrawableId, int drawableResId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(context, drawableResId, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(Activity activity, String url, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(activity, url, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(Activity activity, File file, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(activity, file, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(Activity activity, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(activity, mediaStoreUri, placeholderDrawableId, errorDrawableId, imageView);
        }
    }


    public void loadImage(FragmentActivity activity, String url, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(activity, url, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(FragmentActivity activity, File file, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(activity, file, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(FragmentActivity activity, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(activity, mediaStoreUri, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(Context context, String url, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(context, url, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(Context context, File file, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(context, file, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(Context context, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(context, mediaStoreUri, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(Fragment fragment, String url, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(fragment, url, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(Fragment fragment, File file, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(fragment, file, placeholderDrawableId, errorDrawableId, imageView);
        }
    }

    public void loadImage(Fragment fragment, Uri mediaStoreUri, int placeholderDrawableId, int errorDrawableId, int viewId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            AndroidApplication.getInstance().getGlideInstance().loadImage(fragment, mediaStoreUri, placeholderDrawableId, errorDrawableId, imageView);
        }
    }


}
