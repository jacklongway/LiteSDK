package com.longway.framework.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.ExifInterface;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 解码辅助类
 */
public final class BitmapHelper {
	public static boolean makesureSizeNotTooLarge(Rect rect) {
		int FIVE_M = 5242880;
		if (rect.width() * rect.height() * 2 > FIVE_M) {
			return false;
		}
		return true;
	}

	public static int getSampleSizeOfNotTooLarge(Rect rect) {
		int FIVE_M = 5242880;
		double ratio = rect.width() * rect.height() * 2.0D / FIVE_M;
		return ratio >= 1.0D ? (int) ratio : 1;
	}

	public static int getSampleSizeAutoFitToScreen(int vWidth, int vHeight,
			int bWidth, int bHeight) {
		if ((vHeight == 0) || (vWidth == 0)) {
			return 1;
		}
		int ratio = Math.max(bWidth / vWidth, bHeight / vHeight);

		int ratioAfterRotate = Math.max(bHeight / vWidth, bWidth / vHeight);

		return Math.min(ratio, ratioAfterRotate);
	}

	public static boolean verifyBitmap(byte[] datas) {
		return verifyBitmap(new ByteArrayInputStream(datas));
	}

	public static boolean verifyBitmap(Bitmap bitmap) {
		if (bitmap == null || bitmap.isRecycled()) {
			return false;
		}
		return true;
	}

	public static boolean verifyBitmap(InputStream input) {
		if (input == null) {
			return false;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		input = (input instanceof BufferedInputStream) ? input
				: new BufferedInputStream(input);
		BitmapFactory.decodeStream(input, null, options);
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (options.outHeight > 0) && (options.outWidth > 0);
	}

	public static boolean verifyBitmap(String path) {
		try {
			return verifyBitmap(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}


	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}
