package com.example.administrator.retrofitmvp;


import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * 管理整个app的activity实例 注意隐式持有
 *
 * @author zyj
 */
public class AppMgr {
    private static volatile AppMgr mIntance;
    private static List<Activity> actLists = new LinkedList<Activity>();

    private AppMgr() {
        if (actLists == null) {
            actLists = new LinkedList<Activity>();
        }
    }

    /*
     * 单例实现
     */
    public static AppMgr getInstance() {
        if (mIntance == null) {
            synchronized (AppMgr.class) {
                if (mIntance == null) {
                    return new AppMgr();
                }
            }
        }
        return mIntance;
    }


    public void addAct(Activity a) {
        actLists.add(a);
    }

    public void closeAct(Activity a) {
        a.finish();
        actLists.remove(a);
    }

    public void closeAllActs() {//清理前面的所有activity
        try {
            for (Activity act : actLists) {
                if (act != null)
                    act.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            actLists.clear();
        }
    }

    public void closeAllActsAndExit() {//清理前面的所有activity并退出
        try {
            for (Activity act : actLists) {
                if (act != null)
                    act.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            actLists.clear();
            System.exit(0);
        }
    }

    /**
     * 获取开启过的活动数量
     *
     * @return
     */
    public int getActCount() {
        if (actLists != null) {
            return actLists.size();
        } else {
            return 0;
        }
    }

    public List<Activity> getActList() {
        return actLists;
    }
}