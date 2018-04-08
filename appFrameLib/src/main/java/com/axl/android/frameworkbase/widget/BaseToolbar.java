package com.axl.android.frameworkbase.widget;


import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.axl.android.frameworkbase.R;
import com.axl.android.frameworkbase.ui.AbsBaseActivity;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2017/5/18
 * Time: 下午2:26
 */

public class BaseToolbar implements Toolbar.OnMenuItemClickListener {

    private Toolbar toolbar;
    private AbsBaseActivity mContext;
    private onMenuClickListener onMenuClickListener;
    private TextView toolbarTitle;
    private TextView tv_right;
    public BaseToolbar(View view, AbsBaseActivity mContext) {
        this.mContext = mContext;
        this.toolbar = view.findViewById(R.id.toolbar);
        toolbarTitle = view.findViewById(R.id.toolbar_title);
        tv_right = view.findViewById(R.id.tv_right);
        setSupport();
        setDefaultNavigation();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * 设置右侧的文字按钮
     * @param txtId 文字资源id
     */
    public void setText(int txtId){
        tv_right.setText(txtId);
    }

    public TextView getRightText(){
        return tv_right;
    }

    public void setDefaultNavigation() {
        setNavigationIcon(R.mipmap.nav_back_button_write);
        setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().onBackPressed();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (onMenuClickListener != null) {
            onMenuClickListener.onMenuItemClick(item);
            return true;
        }
        return false;
    }

    public interface onMenuClickListener {
        void onMenuItemClick(MenuItem item);
    }

    public void setOnMenuClickListener(BaseToolbar.onMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    public void setSupport() {
        if (toolbar != null && mContext != null) {
            getContext().setSupportActionBar(toolbar);
            if (toolbarTitle != null) {
                ActionBar supportActionBar = getContext().getSupportActionBar();
                if (supportActionBar != null) {
                    supportActionBar.setDisplayShowTitleEnabled(false);
                }
            }
            toolbar.setOnMenuItemClickListener(this);
        }
    }

    /****
     * 设置Toolbar阴影高度
     *
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setElevation(float elevatoin) {
        if (getContext().getActionBar() != null) {
            getContext().getActionBar().setElevation(elevatoin);
        }
    }

    public AbsBaseActivity getContext() {
        return mContext;
    }

    /****
     * Toolbar 左侧icon点击回调
     *
     */
    public void setNavigationOnClickListener(View.OnClickListener onClickListener) {
        toolbar.setNavigationOnClickListener(onClickListener);
    }

    /***
     * 设置中间的标题
     */
    public void setCenterTitle(int resId) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(resId);
        }
    }
    /***
     * 设置中间的标题
     */
    public void setCenterTitle(CharSequence title) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }

    /**
     * Actionbar标题
     */
    public void setLeftTitle(int resId) {
        if (toolbar == null) {
            return;
        }
        toolbar.setTitle(resId);
    }

    /***
     * Actionbar标题
     */
    public void setLeftTitle(CharSequence title) {
        if (toolbar == null) {
            return;
        }
        toolbar.setTitle(title);
    }

    /****
     * 设置Action左侧icon
     *
     */
    protected void setNavigationIcon(Drawable icon) {
        if (toolbar == null) {
            return;
        }
        toolbar.setNavigationIcon(icon);
    }

    /****
     * 设置Action左侧icon
     *
     */
    public void setNavigationIcon(int resId) {
        if (toolbar == null) {
            return;
        }
        toolbar.setNavigationIcon(resId);
    }
}
