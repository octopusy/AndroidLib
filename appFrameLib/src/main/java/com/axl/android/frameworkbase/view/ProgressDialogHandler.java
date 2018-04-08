package com.axl.android.frameworkbase.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.axl.android.frameworkbase.net.utils.ProgressCancelListener;
import com.axl.android.frameworkbase.widget.DialogHelper;

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private Dialog pd;
    private DialogHelper mDialogHelper;
    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
        this.mDialogHelper = new DialogHelper(context);
    }

    private void initProgressDialog(){
        if (pd == null) {
            pd = mDialogHelper.getLoadingDialog();

            pd.setCancelable(cancelable);

            if (cancelable) {
                pd.setOnCancelListener(dialogInterface -> mProgressCancelListener.onCancelProgress());
            }

            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissProgressDialog(){
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}