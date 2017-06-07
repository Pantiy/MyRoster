package cn.pantiy.myroster.database.ClassmateInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * MyRoster
 * cn.pantiy.myroster.database
 * Created by pantiy on 17-6-7.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class ClassmateInfoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "classmateInfo.db";
    private static final int VERSION = 1;

    public ClassmateInfoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ClassmateInfoDatabase.getSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
