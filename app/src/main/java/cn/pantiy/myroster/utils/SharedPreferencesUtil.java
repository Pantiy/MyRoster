package cn.pantiy.myroster.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cn.pantiy.myroster.global.MyApplication;

/**
 * MyRoster
 * cn.pantiy.myroster.utils
 * Created by pantiy on 17-6-6.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public final class SharedPreferencesUtil {

    private static SharedPreferences sSharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(MyApplication.getContext());

    private static SharedPreferences.Editor sEditor = PreferenceManager
            .getDefaultSharedPreferences(MyApplication.getContext()).edit();

    public static boolean putBoolean(String key, boolean value) {
        sEditor.putBoolean(key, value).apply();
        return value;
    }

    public static boolean getBoolean(String key) {
        return sSharedPreferences.getBoolean(key, false);
    }
}
