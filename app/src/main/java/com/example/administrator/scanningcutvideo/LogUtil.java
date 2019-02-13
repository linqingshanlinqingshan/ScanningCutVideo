package com.example.administrator.scanningcutvideo;

import android.util.Log;

/**
 * Description: ScreenUtil
 * Creator: yxc
 * date: $date $time
 */
public class LogUtil {

    private static final boolean isBebug = true;

    /**
     * 打印请求信息
     * Log.e
     */
    public static void logError(String tag, String msg) {
        if (isBebug) {
            Log.e(tag, msg);
        }
    }


    public static void logDebug(String tag, String msg) {
        if (isBebug) {
            Log.d(tag, msg);
        }
    }

    public static void logInfo(String tag, String msg) {
        if (isBebug) {
            Log.i(tag, msg);
        }
    }

}
