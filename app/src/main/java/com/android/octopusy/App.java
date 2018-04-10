package com.android.octopusy;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.axl.android.frameworkbase.BaseApplication;

import org.xutils.x;

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
        MultiDex.install(mContext);
        x.Ext.init(this);
        //是否是开发、调试模式
        x.Ext.setDebug(BuildConfig.DEBUG);//是否输出debug日志，开启debug会影响性能

        
    }
}
