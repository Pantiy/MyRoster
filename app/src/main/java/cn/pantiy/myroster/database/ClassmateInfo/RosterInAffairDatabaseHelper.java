package cn.pantiy.myroster.database.ClassmateInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pantiy on 2018/1/28.
 * Copyright © 2016 All rights Reserved by Pantiy
 */

public class RosterInAffairDatabaseHelper extends SQLiteOpenHelper {

    private static String sAffairId;
    private static final int VERSION = 1;

    public static RosterInAffairDatabaseHelper newInstance(Context context, String affairId) {
        sAffairId = affairId;
        return new RosterInAffairDatabaseHelper(context);
    }

    private static String generateSQL(String fromName) {
        return "create table " + "\"" + fromName + "\"" + "(" +
                ClassmateInfoDatabase.Table.STUDENT_NUM + "," +
                ClassmateInfoDatabase.Table.STUDENT_NAME + ")";
    }

    private RosterInAffairDatabaseHelper(Context context) {
        super(context, sAffairId, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(generateSQL(sAffairId));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
