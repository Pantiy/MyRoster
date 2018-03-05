package cn.pantiy.myroster.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import cn.pantiy.myroster.database.ClassmateInfo.ClassmateInfoCursorWrapper;
import cn.pantiy.myroster.database.ClassmateInfo.RosterInAffairDatabaseHelper;

/**
 * Created by Pantiy on 2018/1/28.
 * Copyright © 2016 All rights Reserved by Pantiy
 */

public class RosterInAffairLab {

    private static RosterInAffairLab sRosterInAffairLab = null;

    private String mAffairId;
    private SQLiteDatabase mSQLiteDatabase;

    public static RosterInAffairLab touch(Context context, String affairId) {
        if (sRosterInAffairLab == null) {
            sRosterInAffairLab = new RosterInAffairLab(context, affairId);
        }
        return sRosterInAffairLab;
    }

    private RosterInAffairLab(Context context, String affairId) {
        mAffairId = affairId;
        mSQLiteDatabase = RosterInAffairDatabaseHelper.newInstance(context, mAffairId).getReadableDatabase();
    }

    public List<ClassmateInfo> getRoster() {
        List<ClassmateInfo> classmateInfoList = new ArrayList<>();
        ClassmateInfoCursorWrapper cursorWrapper = getCursorWrapper();
        if (cursorWrapper.getCount() == 0) {
            return null;
        }
        cursorWrapper.moveToFirst();
        while (!cursorWrapper.isAfterLast()) {
            classmateInfoList.add(cursorWrapper.getClassmateInfo());
            cursorWrapper.moveToNext();
        }
        cursorWrapper.close();
        return classmateInfoList;
    }

    public void delete() {
        mSQLiteDatabase.execSQL("DELETE FROM " + mAffairId);
    }

    private ClassmateInfoCursorWrapper getCursorWrapper() {
        Cursor cursor = mSQLiteDatabase.query(
                "\"" + mAffairId + "\"",
                null,
                null,
                null,
                null,
                null,
                null);
        return new ClassmateInfoCursorWrapper(cursor);
    }
}
