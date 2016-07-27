package com.longway.framework.reference.softreference;

import android.graphics.Bitmap;

/*********************************
 * Created by longway on 16/5/26 上午10:33.
 * packageName:com.longway.framework.reference.softreference
 * projectName:trunk
 * Email:longway1991117@sina.com
 ********************************/
public class SoftReferenceBitmap extends BaseSoftReference<Bitmap> {

    public SoftReferenceBitmap(Bitmap bitmap) {
        super(bitmap);
    }

    @Override
    public boolean referenceActive() {
        Bitmap bitmap = getReference();
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        return true;
    }
}
