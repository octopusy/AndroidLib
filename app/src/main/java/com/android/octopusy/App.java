package com.android.octopusy;

import android.content.Context;
import com.axl.android.frameworkbase.BaseApplication;

/**
 * @project：AndroidLib
 * @author：- octopusy on 2018/4/10 14:12
 * @email：zhangh@qtopay.cn
 */
public class App extends BaseApplication{

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;


    }
}
