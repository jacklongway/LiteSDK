package com.longway.framework.core.imagePicker;

import android.app.Activity;
import android.content.Intent;

import java.util.List;

/**
 * Created by longway on 16/6/10.
 * Email:longway1991117@sina.com
 */

public interface IImagePicker {
    void jumpToImagePicker(Activity activity, List<String> selected);

    List<String> parseSelectedImageList(Intent intent);
}
