package com.gwb.superrecycleview;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by ${GongWenbo} on 2018/5/22 0022.
 */
public class App extends Application {

    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        // Logger日志
        Logger.addLogAdapter(new AndroidLogAdapter());
        LeakCanary.install(this);
        sApp = this;
    }

    public static App getApp() {
        return sApp;
    }

}
