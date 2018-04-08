package com.axl.android.frameworkbase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axl.android.frameworkbase.R;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxDialogFragment;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2017/11/2
 * Time: 18:36
 */

public abstract class BaseFragment extends RxDialogFragment {

    public View mContentView;

    /**
     * 屏幕参数
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle extras = getArguments();
        if (null != extras) {
            getBundleExtras(extras);
        }

        if (getContentViewLayoutID() != 0) {
            mContentView = inflater.inflate(getContentViewLayoutID(), container, false);
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
        initViewsAndEvents();
    }

    /**
     * get bundle data
     *
     * @param extras
     */
    protected abstract void getBundleExtras(Bundle extras);

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();

    /**
     * init all views and add events
     */
    protected abstract void initViewsAndEvents();


    /********************************
     * 【跳转到其他界面】
     *******************************************/
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, false);
    }

    public void openActivity(Class<?> pClass, boolean isFinish) {
        openActivity(pClass, null, isFinish);
    }

    public void openActivity(Class<?> pClass, Bundle pBundle) {
        openActivity(pClass, pBundle, false);
    }

    public void openActivity(Class<?> pClass, Bundle pBundle, boolean isFinish) {
        Intent intent = new Intent(getContext(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }

        startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
    }


    public void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    public void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }


    /**
     * show toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
        //防止遮盖虚拟按键
        if (null != msg && !TextUtils.isEmpty(msg)) {
            showToastShort(msg);
        }
    }

    public void showToastLong(CharSequence str) {
        try {
            Snackbar.make(getView(), str, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToastShort(CharSequence str) {
        try {
            Snackbar.make(getView(), str, Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToastLong(int resId) {
        try {
            showToastLong(getString(resId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToastShort(int resId) {
        try {
            showToastShort(getString(resId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * snackbar 增加右侧提示
     */
    public void showToast(int resId, int actionStrId, View.OnClickListener onClickListener) {
        try {
            Snackbar snackbar = Snackbar.make(getView(), resId, Snackbar.LENGTH_LONG);
            if (actionStrId > 0) {
                snackbar.setAction(actionStrId, onClickListener);
                snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
            }
            snackbar.show();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    /****
     * snackbar 增加右侧提示
     */
    public void showToast(String str, String actionStr, View.OnClickListener onClickListener) {
        try {
            Snackbar snackbar = Snackbar.make(getView(), str, Snackbar.LENGTH_LONG);
            snackbar.setAction(actionStr, onClickListener);
            snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
