package cn.pantiy.myroster.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import cn.pantiy.myroster.database.ClassmateInfo.ClassmateInfoCursorWrapper;
import cn.pantiy.myroster.database.ClassmateInfo.ClassmateInfoDatabase;
import cn.pantiy.myroster.database.ClassmateInfo.ClassmateInfoDatabase.Table;
import cn.pantiy.myroster.database.RosterInAffairDatabaseHelper;

/**
 * Created by Pantiy on 2018/1/28.
 * Copyright © 2016 All rights Reserved by Pantiy
 */

public class RosterInAffairLab {

    private static RosterInAffairLab sRosterInAffairLab = null;

//    private Context mContext;
    private String mAffairId;
    private SQLiteDatabase mSQLiteDatabase;

    public static RosterInAffairLab touch(Context context, String affairId) {
        if (sRosterInAffairLab == null || !affairId.equals(sRosterInAffairLab.mAffairId)) {
            sRosterInAffairLab = new RosterInAffairLab(context, affairId);
//            sRosterInAffairLab.mContext = context;
        }
        return sRosterInAffairLab;
    }

    private RosterInAffairLab(Context context, String affairId) {
        mAffairId = affairId;
        mSQLiteDatabase = RosterInAffairDatabaseHelper.newInstance(context, mAffairId).getReadableDatabase();
    }

    public List<ClassmateInfo> queryRoster() {
        List<ClassmateInfo> classmateInfoList = new ArrayList<>();
        ClassmateInfoCursorWrapper cursorWrapper = getCursorWrapper();
        if (cursorWrapper.getCount() == 0) {
            return null;
        }
        cursorWrapper.moveToFirst();
        while (!cursorWrapper.isAfterLast()) {
            classmateInfoList.add(cursorWrapper.getClassmateInfo(false));
            cursorWrapper.moveToNext();
        }
        cursorWrapper.close();
        return classmateInfoList;
    }

//    public void delete() {
////        mSQLiteDatabase.execSQL("DROP TABLE " + wrapperAffairIdTable(mAffairId));
//        mContext.deleteDatabase(mAffairId);
//    }

    public void updateState(List<ClassmateInfo> classmateInfoList) {
        for (ClassmateInfo classmateInfo : classmateInfoList) {
            mSQLiteDatabase.update(wrapperAffairIdTable(mAffairId),
                    getContentValues(classmateInfo),
                    Table.STUDENT_NUM + "=?",
                    new String[]{classmateInfo.getStudentNum()});
        }
    }

    public void updateState(ClassmateInfo classmateInfo) {
        mSQLiteDatabase.update(wrapperAffairIdTable(mAffairId),
                getContentValues(classmateInfo),
                Table.STUDENT_NUM + "=?",
                new String[]{classmateInfo.getStudentNum()});
    }

    private ClassmateInfoCursorWrapper getCursorWrapper() {
        Cursor cursor = mSQLiteDatabase.query(
                wrapperAffairIdTable(mAffairId),
                null,
                null,
                null,
                null,
                null,
                null);
        return new ClassmateInfoCursorWrapper(cursor);
    }

    private String wrapperAffairIdTable(String affairId) {
        return "\"" + affairId + "\"";
    }

    private ContentValues getContentValues(ClassmateInfo classmateInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.STUDENT_STATE, classmateInfo.getState() ? 1 : 0);
        return contentValues;
    }
}
