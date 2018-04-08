package com.axl.android.frameworkbase.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.axl.android.frameworkbase.AppActivityManager;
import com.axl.android.frameworkbase.R;
import com.axl.android.frameworkbase.utils.netstatus.NetChangeObserver;
import com.axl.android.frameworkbase.utils.netstatus.NetStateReceiver;
import com.axl.android.frameworkbase.utils.netstatus.NetUtils;
import com.axl.android.frameworkbase.view.VaryViewHelperController;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2017/10/13
 * Time: 17:10
 */

public abstract class AbsBaseActivity extends RxAppCompatActivity {
    private static final String TAG = AbsBaseActivity.class.getSimpleName();

    protected View mContentView;

    /**
     * Log tag
     */
    protected static String TAG_LOG = null;
    /**
     * 屏幕参数
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    /**
     * 上下文
     */
    protected Context mContext = null;

    /**
     * 联网状态
     */
    protected NetChangeObserver mNetChangeObserver = null;

    /**
     * loading view controller
     */
    private VaryViewHelperController mVaryViewHelperController = null;

    /**
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        // base setup
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }


        mContext = this;
        TAG_LOG = this.getClass().getSimpleName();
        AppActivityManager.getInstance().add(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;

        if (getContentViewLayoutID() != 0) {
            mContentView = View.inflate(this, getContentViewLayoutID(), null);
            setContentView(mContentView);
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                super.onNetConnected(type);
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                super.onNetDisConnect();
                onNetworkDisConnected();
            }
        };

        NetStateReceiver.registerObserver(mNetChangeObserver);

        initViewsAndEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (null != getLoadingTargetView()) {
            mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
        }
    }

    @Override
    public void finish() {
        super.finish();
        AppActivityManager.getInstance().remove(this);
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
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


    /**
     * get loading target view
     */
    protected abstract View getLoadingTargetView();

    /**
     * network connected
     */
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    ;

    /**
     * network disconnected
     */
    protected void onNetworkDisConnected() {

    }


    /**
     * toggle overridePendingTransition
     *
     * @return
     */
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    ;

    /**
     * get the overridePendingTransition mode
     */
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.FADE;
    }


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
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }

        startActivity(intent);
        if (isFinish) {
            finish();
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

    protected void showToast(int resId) {
        //防止遮盖虚拟按键
        showToastShort(resId);
    }

    public void showToastLong(CharSequence str) {
        try {
            hideKeyboard();
            Snackbar.make(mContentView, str, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToastShort(CharSequence str) {
        try {
            hideKeyboard();
            Snackbar.make(mContentView, str, Snackbar.LENGTH_SHORT).show();
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
            hideKeyboard();
            Snackbar snackbar = Snackbar.make(mContentView, resId, Snackbar.LENGTH_LONG);
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
            hideKeyboard();
            Snackbar snackbar = Snackbar.make(mContentView, str, Snackbar.LENGTH_LONG);
            snackbar.setAction(actionStr, onClickListener);
            snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * toggle show loading
     *
     * @param toggle
     */
    protected void toggleShowLoading(boolean toggle, String msg) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showLoading(msg);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     *
     * @param toggle
     */
    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show error
     *
     * @param toggle
     */
    protected void toggleShowError(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showError(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show network error
     *
     * @param toggle
     */
    protected void toggleNetworkError(boolean toggle, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showNetworkError(onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }


    /**
     * 隐藏软键盘
     */
    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if ((imm != null && imm.isActive()) && getCurrentFocus() != null) {
            if (getWindow() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    //显示虚拟键盘
    protected void showKeyboard(View v) {
        Observable.just("")
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe(s -> {
                    InputMethodManager inputManager =
                            (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.showSoftInput(v, 0);
                    }
                }, error -> Logger.e(error.getMessage()));
    }

    // FIXME: 2018/3/19 LGQ 让输入密码框英文字体变为常规字体
    public static void EditTextDefault(EditText et){
        et.setTypeface(Typeface.DEFAULT);
        et.setTransformationMethod(new PasswordTransformationMethod());
    }

}
