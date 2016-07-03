package com.longway.framework.core.imagePicker;

import android.app.Activity;
import android.content.Intent;

import com.longway.framework.core.common.AbsManager;

import java.util.List;

/**
 * Created by longway on 16/6/10.
 * Email:longway1991117@sina.com
 */

public class ImagePickerManager extends AbsManager<IImagePicker> implements IImagePicker {
    private static volatile ImagePickerManager sImagePickerManager;

    private ImagePickerManager() {

    }

    public static ImagePickerManager getInstance() {
        if (sImagePickerManager == null) {
            synchronized (ImagePickerManager.class) {
                if (sImagePickerManager == null) {
                    sImagePickerManager = new ImagePickerManager();
                }
            }
        }
        return sImagePickerManager;
    }

    private final IImagePicker mDelegate = new ImagePickerDelegate();

    @Override
    public IImagePicker getCurrentUse() {
        IImagePicker iImagePicker = super.getCurrentUse();
        if (iImagePicker == null) {
            iImagePicker = mDelegate;
        }
        return iImagePicker;
    }

    @Override
    public void jumpToImagePicker(Activity activity, List<String> selected) {
        getCurrentUse().jumpToImagePicker(activity, selected);
    }

    @Override
    public List<String> parseSelectedImageList(Intent intent) {
        return getCurrentUse().parseSelectedImageList(intent);
    }
}
