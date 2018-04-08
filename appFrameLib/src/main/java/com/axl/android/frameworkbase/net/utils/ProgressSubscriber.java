package com.axl.android.frameworkbase.net.utils;

import android.content.Context;

import com.axl.android.frameworkbase.R;
import com.axl.android.frameworkbase.net.exception.ApiException;
import com.axl.android.frameworkbase.utils.netstatus.NetUtils;
import com.axl.android.frameworkbase.view.ProgressDialogHandler;

import io.reactivex.subscribers.DisposableSubscriber;


public abstract class ProgressSubscriber<T> extends DisposableSubscriber<T> implements ProgressCancelListener {


    private ProgressDialogHandler dialogHandler;

    private Context mContext;

    public ProgressSubscriber(Context context) {
        this.dialogHandler = new ProgressDialogHandler(context, this, true);
        this.mContext = context;
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
    }


    /**
     * 显示Dialog
     */
    public void showProgressDialog() {
        if (dialogHandler != null) {
            dialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog();
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    /**
     * 隐藏Dialog
     */
    private void dismissProgressDialog() {
        if (dialogHandler != null) {
            dialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            dialogHandler = null;
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (!NetUtils.isNetworkConnected(mContext)) { //这里自行替换判断网络的代码
            _onError(mContext.getResources().getString(R.string.text_no_network));
        } else if (e instanceof ApiException) {
            _onError(e.getMessage());
        } else {
            _onError(mContext.getResources().getString(R.string.text_net_error));
        }
        dismissProgressDialog();
    }


    @Override
    public void onCancelProgress() {
//        if (mDisposable != null && !mDisposable.isDisposed()) {
//            mDisposable.dispose();
//        }
        if (!isDisposed()) {
            dispose();
        }
    }

    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);

}

