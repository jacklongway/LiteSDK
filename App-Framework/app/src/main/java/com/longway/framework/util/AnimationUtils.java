package com.longway.framework.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseArray;
import android.view.animation.Animation;

import java.lang.ref.SoftReference;

/**
 * Created by longway on 15/12/19.
 * 带有缓存的动画工具类
 */
public class AnimationUtils {
    private static SparseArray<SoftReference<Animation>> sAnimation = new SparseArray<SoftReference<Animation>>();

    public static Animation loadAnimation(Context context, int id) {
        Animation animation = null;
        synchronized (AnimationUtils.class) {
            if (sAnimation.indexOfKey(id) >= 0) { // from cache?
                animation = sAnimation.get(id).get();
            }
            if (animation == null) {
                try {
                    animation = android.view.animation.AnimationUtils.loadAnimation(context, id);
                    sAnimation.put(id, new SoftReference<Animation>(animation));// put cache
                } catch (Resources.NotFoundException ex) {
                    // id?
                } catch (Throwable ex) {
                    // ignore error
                }
            }
        }
        return animation;
    }

    public static synchronized void clearAnimationCache() {
        sAnimation.clear();
    }
}
