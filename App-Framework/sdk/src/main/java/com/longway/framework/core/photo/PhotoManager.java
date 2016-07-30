package com.longway.framework.core.photo;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import com.longway.framework.AndroidApplication;
import com.longway.framework.util.FileUtils;
import com.longway.framework.util.IntentUtils;

import java.io.File;

/**
 * Created by longway on 16/7/12.
 * Email:longway1991117@sina.com
 */

public class PhotoManager implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = PhotoManager.class.getSimpleName();
    public static final int TAKE_PHOTO = IntentUtils.TAKE_REQUEST_CODE;
    public static final int PICK_PHOTO = IntentUtils.PICK_REQUEST_CODE;
    public static final int CROP_PHOTO = IntentUtils.CROP_REQUEST_CODE;
    private static final String EXTRA_CAMERA_OUT_PATH = "camera_out_path";
    private static final String EXTRA_CROP_TEMP_PATH = "crop_out_path";
    private PhotoListener mPhotoListener;
    private File mOutPath;
    private File mCropTempPath;
    private AlertDialog mAlertDialog;
    private Activity mActivity;

    private AndroidApplication getFrameworkApplication(Activity activity) {
        Application application = (Application) activity.getApplicationContext();
        if (application instanceof AndroidApplication) {
            AndroidApplication androidApplication = (AndroidApplication) application;
            return androidApplication;
        }
        return null;
    }

    public PhotoManager(final Activity a, PhotoListener photoListener) {
        this.mActivity = a;
        AndroidApplication androidApplication = getFrameworkApplication(a);
        if (androidApplication != null) {
            androidApplication.addActivityLifecycleCallback(this);
        }
        this.mPhotoListener = photoListener;
    }

    public void launchPhotoSelector(String title, String[] actions, Activity activity, final PhotoActionListener photoActionListener) {
        if (mAlertDialog == null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setItems(actions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (photoActionListener != null) {
                        photoActionListener.onPhotoAction(which, PhotoManager.this);
                    }
                }
            });
            alertDialog.setTitle(title);
            alertDialog.setCancelable(true);
            mAlertDialog = alertDialog.create();
            mAlertDialog.setCanceledOnTouchOutside(true);
        }
        mAlertDialog.show();
    }

    public void launchPhotoSelector(String title, int arrayId, Activity activity, final PhotoActionListener photoActionListener) {
        launchPhotoSelector(title, activity.getResources().getStringArray(arrayId), activity, photoActionListener);
    }

    public void setOutPath(File outPath) {
        this.mOutPath = outPath;
    }

    public File getOutPath() {
        return mOutPath;
    }

    public void takePhoto(Activity activity,
                          File outPath) {
        if (!IntentUtils.hasCamera(activity)) {
            if (mPhotoListener != null) {
                mPhotoListener.onException(new PhotoException(PhotoConstants.NO_CAMERA, PhotoConstants.getMessage(PhotoConstants.NO_CAMERA)));
            }
            return;
        }
        if (!IntentUtils.hasActivity(activity, MediaStore.ACTION_IMAGE_CAPTURE)) {
            if (mPhotoListener != null) {
                mPhotoListener.onException(new PhotoException(PhotoConstants.NO_CAMERA_APP, PhotoConstants.getMessage(PhotoConstants.NO_CAMERA_APP)));
            }
            return;
        }
        if (activity.isFinishing()) {
            return;
        }
        try {
            IntentUtils.takePicture(activity, IntentUtils.TAKE_REQUEST_CODE, outPath);
            this.mOutPath = outPath;
        } catch (Throwable throwable) {
            if (mPhotoListener != null) {
                String msg = throwable != null ? throwable.getMessage() : PhotoConstants.getMessage(PhotoConstants.LAUNCH_CAMERA_APP_FAIL);
                mPhotoListener.onException(new PhotoException(PhotoConstants.LAUNCH_CAMERA_APP_FAIL, msg));
            }
        }
    }

    public void handlePhoto(Activity activity, int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case IntentUtils.TAKE_REQUEST_CODE:
                    handleTakePhoto(activity);
                    break;
                case IntentUtils.PICK_REQUEST_CODE:
                    handlePickPhoto(activity, intent.getData());
                    break;
                case IntentUtils.CROP_REQUEST_CODE:
                    handleCropPhoto(activity);
                    break;
                default:
                    break;
            }
        } else {
            if (mPhotoListener != null) {
                mPhotoListener.onCancel();
            }
        }
    }

    public void handleTakePhoto(Activity activity) {
        File file = IntentUtils.handleCameraFile(activity, mOutPath);
        if (FileUtils.isValidateFile(file)) {
            if (mPhotoListener != null) {
                mPhotoListener.onException(new PhotoException(PhotoConstants.TAKE_PHOTO_FAIL, PhotoConstants.getMessage(PhotoConstants.TAKE_PHOTO_FAIL)));
            }
        } else {
            if (mPhotoListener != null) {
                mPhotoListener.onComplete(TAKE_PHOTO, file);
            }
        }
    }

    public void pickPhoto(Activity activity) {
        if (!IntentUtils.hasActivity(activity, Intent.ACTION_GET_CONTENT)) {
            if (mPhotoListener != null) {
                mPhotoListener.onException(new PhotoException(PhotoConstants.NO_ALBUM_APP, PhotoConstants.getMessage(PhotoConstants.NO_ALBUM_APP)));
            }
            return;
        }
        if (activity.isFinishing()) {
            return;
        }
        try {
            IntentUtils.pickImage(activity, IntentUtils.PICK_REQUEST_CODE);
        } catch (Throwable throwable) {
            if (mPhotoListener != null) {
                String msg = throwable != null ? throwable.getMessage() : PhotoConstants.getMessage(PhotoConstants.LAUNCH_ALBUM_APP_FAIL);
                mPhotoListener.onException(new PhotoException(PhotoConstants.LAUNCH_ALBUM_APP_FAIL, msg));
            }
        }
    }

    public void handlePickPhoto(Activity activity, Uri uri) {
        String path = IntentUtils.getFilePath(activity, uri);
        if (path == null) {
            if (mPhotoListener != null) {
                mPhotoListener.onException(new PhotoException(PhotoConstants.PICK_PHOTO_FAIL, PhotoConstants.getMessage(PhotoConstants.PICK_PHOTO_FAIL)));
            }
        } else {
            if (!FileUtils.isValidateFile(path)) {
                if (mPhotoListener != null) {
                    mPhotoListener.onException(new PhotoException(PhotoConstants.PICK_PHOTO_FAIL, PhotoConstants.getMessage(PhotoConstants.PICK_PHOTO_FAIL)));
                }
            } else {
                if (mPhotoListener != null) {
                    mPhotoListener.onComplete(PICK_PHOTO, new File(path));
                }
            }
        }
    }

    public void setCropTempPath(File cropTempPath) {
        this.mCropTempPath = cropTempPath;
    }

    public File getCropTempPath() {
        return mCropTempPath;
    }

    public void handleCropPhoto(Activity activity) {
        if (!FileUtils.isValidateFile(mCropTempPath)) {
            if (mPhotoListener != null) {
                mPhotoListener.onException(new PhotoException(PhotoConstants.CROP_FAIL, PhotoConstants.getMessage(PhotoConstants.CROP_FAIL)));
            }
        } else {
            if (mPhotoListener != null) {
                mPhotoListener.onComplete(CROP_PHOTO, mCropTempPath);
            }
        }
    }

    public void cropPhoto(Activity activity, CropOptions cropOptions) {
        if (!IntentUtils.hasActivity(activity, "com.android.camera.action.CROP")) {
            if (mPhotoListener != null) {
                mPhotoListener.onException(new PhotoException(PhotoConstants.NO_CROP_APP, PhotoConstants.getMessage(PhotoConstants.NO_CROP_APP)));
            }
            return;
        }
        if (activity.isFinishing()) {
            return;
        }
        try {
            IntentUtils.cropPhoto(activity, cropOptions);
            mCropTempPath = cropOptions.output;
        } catch (Throwable throwable) {
            if (mPhotoListener != null) {
                String msg = throwable != null ? throwable.getMessage() : PhotoConstants.getMessage(PhotoConstants.LAUNCH_CROP_APP_FAIL);
                mPhotoListener.onException(new PhotoException(PhotoConstants.LAUNCH_CROP_APP_FAIL, msg));
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (mActivity == activity) {
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(EXTRA_CAMERA_OUT_PATH)) {
                    mOutPath = (File) savedInstanceState.getSerializable(EXTRA_CAMERA_OUT_PATH);
                }
                if (savedInstanceState.containsKey(EXTRA_CROP_TEMP_PATH)) {
                    mCropTempPath = (File) savedInstanceState.getSerializable(EXTRA_CROP_TEMP_PATH);
                }
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (activity == mActivity) {
            outState.putSerializable(EXTRA_CAMERA_OUT_PATH, mOutPath);
            outState.putSerializable(EXTRA_CROP_TEMP_PATH, mCropTempPath);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity == mActivity) {
            AndroidApplication androidApplication = getFrameworkApplication(activity);
            if (androidApplication != null) {
                androidApplication.removeActivityLifecycleCallback(PhotoManager.this);
            }
        }
    }
}
