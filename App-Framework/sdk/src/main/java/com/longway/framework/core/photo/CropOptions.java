package com.longway.framework.core.photo;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/**
 * Created by longway on 16/7/12.
 * Email:longway1991117@sina.com
 */

public class CropOptions {
    public Uri input;
    public File output;
    public Bitmap.CompressFormat mCompressFormat;
    public int aspectX;
    public int aspectY;
    public int outputX;
    public int outputY;
}
