package com.axl.android.frameworkbase.ui;

import android.os.Bundle;

import com.axl.android.frameworkbase.widget.BaseToolbar;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2017/10/24
 * Time: 16:46
 */

public abstract class ToolBarActivity extends AbsBaseActivity {


    protected BaseToolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = new BaseToolbar(mContentView, this);
        mToolbar.setDefaultNavigation();
        initToolBar();
    }

    protected abstract void initToolBar();
}
