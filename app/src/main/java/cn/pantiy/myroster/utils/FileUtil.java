package cn.pantiy.myroster.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * MyRoster
 * cn.pantiy.myroster.utils
 * Created by pantiy on 17-6-18.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public final class FileUtil {

    private static final String TAG = "FileUtil";

    public static String getPath(Context context, Uri uri) {
        String path = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] protection = new String[] {MediaStore.Files.FileColumns.DATA};
            Cursor cursor = context.getContentResolver().query(uri, protection, null,
                    null,null);
            if (cursor == null || cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            cursor.close();
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        Log.i(TAG, "path: " + path);
        return path;
    }
}
