package com.example.administrator.retrofitmvp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * 联网状态的工具类
 * DCL单例模式
 */

public class NetStateUtil {

    private static NetStateUtil mInstance = null;

    private NetStateUtil() {
    }

    public static NetStateUtil getInstance() {
        if (mInstance == null) {
            synchronized (NetStateUtil.class) {
                if (mInstance == null) {
                    mInstance = new NetStateUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 网络连接不可用的状态
     * @return
     */
    public boolean setNetStatuTIp(Context mContext) {
//        if (!LoveBabyConfig.NetIsConnected){
//            SimplexToast.show(mContext, "网络连接断开，请检查网络");
//            return true;
//        }
        return false;
    }

    /**
     * 判断当前是否是wifi环境
     * @return
     */
    public boolean isWifiOpen(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        if (!info.isAvailable() || !info.isConnected()) {
            return false;
        }
        if (info.getType() != ConnectivityManager.TYPE_WIFI) {
            return false;
        }
        return true;
    }


}
