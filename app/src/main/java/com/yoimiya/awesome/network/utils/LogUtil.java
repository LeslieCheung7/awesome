package com.yoimiya.awesome.network.utils;

import android.text.TextUtils;
import android.util.Log;

public final class LogUtil {

    private static final int I = 0x1;
    private static final int D = 0x2;
    private static final int E = 0x3;

    private static boolean IS_SHOW_LOG = true;

    public static void setIsShowLog(boolean isShowLog) {
        IS_SHOW_LOG = isShowLog;
    }

    public static void i(int type, String msg) {
        printLog(I, msg);
    }

    public static void d(int type, String msg) {
        printLog(D, msg);
    }

    public static void e(int type, String msg) {
        printLog(E, msg);
    }

    private static void printLog(int type, String msg) {
        if (!IS_SHOW_LOG) {
            return;
        }

        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        int index = 4;
        int lineNumber = elements[index].getLineNumber();
        String className = elements[index].getFileName();
        String methodName = elements[index].getMethodName();
        String logStr = "[ (" + className + ":" + lineNumber + ")#" + methodName + " ] " + msg;

        if (TextUtils.isEmpty(msg)) {
            Log.d(className, "msg is empty");
            return;
        }

        switch (type) {
            case I:
                Log.i(className, logStr);
                break;
            case D:
                Log.d(className, logStr);
                break;
            case E:
                Log.e(className, logStr);
                break;
            default:
                break;
        }
    }
}
