package cn.pantiy.myroster.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Pantiy on 2018/3/20.
 * Copyright © 2016 All rights Reserved by Pantiy
 */

public class PermissionUtil {

    public static boolean checkPermission(Context context, String permission) {
        return (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermissions(android.support.v4.app.Fragment fragment,
                                             String[] permissions, int requestCode) {
        fragment.requestPermissions(permissions, requestCode);
    }

    public static void requestPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
}
