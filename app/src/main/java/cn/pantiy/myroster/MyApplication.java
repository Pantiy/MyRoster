package cn.pantiy.myroster;

import android.app.Application;
import android.content.Context;

/**
 * MyRoster
 * cn.pantiy.myroster
 * Created by pantiy on 17-6-6.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class MyApplication extends Application {

    private static Context sContext = null;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
