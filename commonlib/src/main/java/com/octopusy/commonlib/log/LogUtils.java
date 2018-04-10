package com.octopusy.commonlib.log;

import android.util.Log;

/**
 * Created by xiaox on 16/7/21.
 */
public class LogUtils {

    static String className;//类名
    static String methodName;//方法名
    static int lineNumber;//行数

    /** 日志信息 **/
    private static String logInfo = "";
    private static boolean isEnable = false;

    private LogUtils() {
    }

    public static void setDebugEnable(boolean isEnable){
        LogUtils.isEnable = isEnable;
    }
    private static boolean isDebuggable() {
        return isEnable;
    }

    private static String createLog(String log) {
        return "[" + className + ":" + lineNumber + "] - " + log;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }


    public static void error(String msg, Object... arguments) {
        getMethodNames(new Throwable().getStackTrace());
        logInfo = createLog(MessageFormatter.arrayFormat(msg, arguments));
        if (isDebuggable()) {
            Log.e(className, logInfo);
        }
    }

    public static void info(String msg, Object... arguments) {
        getMethodNames(new Throwable().getStackTrace());
        logInfo = createLog(MessageFormatter.arrayFormat(msg, arguments));
        if (isDebuggable()) {
            Log.i(className, logInfo);
        }
    }

    public static void trace(String msg, Object... arguments) {
        getMethodNames(new Throwable().getStackTrace());
        logInfo = createLog(MessageFormatter.arrayFormat(msg, arguments));
        if (isDebuggable()) {
            Log.v(className, logInfo);
        }
    }

    public static void debug(String msg, Object... arguments) {
        getMethodNames(new Throwable().getStackTrace());
        logInfo = createLog(MessageFormatter.arrayFormat(msg, arguments));
        if (isDebuggable()) {
            Log.d(className, logInfo);
        }
    }

    public static void warn(String msg, Object... arguments) {
        getMethodNames(new Throwable().getStackTrace());
        logInfo = createLog(MessageFormatter.arrayFormat(msg, arguments));
        if (isDebuggable()) {
            Log.w(className, logInfo);
        }
    }

}
