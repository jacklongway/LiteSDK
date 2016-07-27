package com.longway.framework.ui.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.longway.elabels.R;
import com.longway.framework.ui.dialog.LoadDialog;
import com.longway.framework.util.ScreenUtils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoadingDialogFragment extends DialogFragment {
    private String mHintText;
    private LoadDialog mLoadDialog;
    private IDialogDismiss mIDialogDismiss;

    public void setIDialogDismiss(IDialogDismiss iDialogDismiss) {
        mIDialogDismiss = iDialogDismiss;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // call back to cancel request
        if (mIDialogDismiss != null) {
            mIDialogDismiss.dialogDismiss();
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    public void setHintText(String hint) {
        mHintText = hint;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mLoadDialog = new LoadDialog(getActivity(), R.style.LoadingDialog);
        setCancelable(true);
        mLoadDialog.setLoadingHintText(mHintText);
        return mLoadDialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        final Window w = getDialog().getWindow();
        WindowManager.LayoutParams params = w.getAttributes();
        int size = ScreenUtils.getScreenWidth(getActivity()) / 4;
        params.width = size;
        params.height = size;
        w.setAttributes(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return null;
    }

    public interface IDialogDismiss {
        void dialogDismiss();
    }
}
