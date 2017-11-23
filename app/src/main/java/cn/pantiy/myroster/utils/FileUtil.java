package cn.pantiy.myroster.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
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
        Log.i(TAG, "uri is " + uri);
        String path = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.i(TAG, "scheme is content");

            if (DocumentsContract.isDocumentUri(context, uri)) {
                Log.i(TAG, "uri is document type");
                String documentId = DocumentsContract.getDocumentId(uri);

                if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                    String[] splitResult = documentId.split(":");
                    String type = splitResult[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + splitResult[1];
                        return path;
                    }
                    uri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(documentId));
                }
            }

            String[] protection = new String[] {MediaStore.Files.FileColumns.DATA};
            Cursor cursor = context.getContentResolver().query(uri, protection, null,
                    null,null);
            if (cursor == null || cursor.getCount() == 0) {
                Log.i(TAG, "cursor is null or cursor's count is 0");
                return null;
            }
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            cursor.close();
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.i(TAG, "scheme is file");
            path = uri.getPath();
        }
        Log.i(TAG, "path: " + path);
        return path;
    }
}
