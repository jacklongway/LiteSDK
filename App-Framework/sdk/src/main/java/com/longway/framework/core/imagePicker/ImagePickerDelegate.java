package com.longway.framework.core.imagePicker;

import android.app.Activity;
import android.content.Intent;

import java.util.List;

/**
 * Created by longway on 16/6/10.
 * Email:longway1991117@sina.com
 */

public class ImagePickerDelegate implements IImagePicker {
    @Override
    public void jumpToImagePicker(Activity activity, List<String> selected) {

    }

    @Override
    public List<String> parseSelectedImageList(Intent intent) {
        return null;
    }

}
