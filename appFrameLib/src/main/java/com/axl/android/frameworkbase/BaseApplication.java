package com.axl.android.frameworkbase;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2017/10/26
 * Time: 15:41
 */

public class BaseApplication extends MultiDexApplication {

    private static Context sAppContext;
    private static String sCacheDir;

    // 当前系统是否设置为英文系统
    private static boolean englishSystem = false;
    // FIXME: 2017/12/8 Lgq当选择一语言时，其它两个语言为false
    private static boolean chineseSystem = false;
    private static boolean chineseTwSystem = false;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            sCacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            sCacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    public static String getAppCacheDir() {
        return sCacheDir;
    }


    public static Context getAppContext() {
        return sAppContext;
    }

    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }


    // FIXME: 2018/1/17 LGQ 新增了多语言
    public static void setEnglishSystem(boolean englishSystem) {
        BaseApplication.englishSystem = englishSystem;
    }
    public static boolean isEnglishSystem() {
        return englishSystem;
    }

    public static void setChineseSystem(boolean chineseSystem) {
        BaseApplication.chineseSystem = chineseSystem;
    }
    public static boolean isChineseSystem() {
        return chineseSystem;
    }

    public static void setChineseTwSystem(boolean chineseTwSystem) {
        BaseApplication.chineseTwSystem = chineseTwSystem;
    }
    public static boolean isChineseTwSystem() {
        return chineseTwSystem;
    }
}
