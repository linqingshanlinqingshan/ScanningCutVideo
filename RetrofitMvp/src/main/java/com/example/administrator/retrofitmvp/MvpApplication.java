package com.example.administrator.retrofitmvp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class MvpApplication extends Application {

    private static MvpApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static MvpApplication getInstance() {
        return instance;
    }
}
