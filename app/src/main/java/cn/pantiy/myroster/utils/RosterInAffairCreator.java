package cn.pantiy.myroster.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import cn.pantiy.myroster.database.ClassmateInfo.RosterInAffairDatabaseHelper;
import cn.pantiy.myroster.model.ClassmateInfo;
import cn.pantiy.myroster.model.ClassmateInfoLab;

import static cn.pantiy.myroster.database.ClassmateInfo.ClassmateInfoDatabase.Table.STUDENT_NAME;
import static cn.pantiy.myroster.database.ClassmateInfo.ClassmateInfoDatabase.Table.STUDENT_NUM;

/**
 * Created by Pantiy on 2018/1/28.
 * Copyright © 2016 All rights Reserved by Pantiy
 */

public final class RosterInAffairCreator {

    private Context mContext;
    private String mAffairId;
    private SQLiteDatabase mSQLiteDatabase;

    public static void create(Context context, String affairId) {
        new RosterInAffairCreator(context, affairId).insertData();
    }

    private RosterInAffairCreator(Context context, String affairId) {
        mContext = context;
        mAffairId = affairId;
        mSQLiteDatabase = RosterInAffairDatabaseHelper.newInstance(mContext, mAffairId)
                .getWritableDatabase();
    }

    private void insertData() {

        List<ClassmateInfo> classmateInfoList =
                ClassmateInfoLab.touch(mContext).queryClassmateInfoList();

        for (ClassmateInfo classmateInfo : classmateInfoList) {
            mSQLiteDatabase.insert("\"" + mAffairId + "\"", null, getContentValues(classmateInfo));
        }
    }

    private ContentValues getContentValues(ClassmateInfo classmateInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_NUM, classmateInfo.getStudentNum());
        contentValues.put(STUDENT_NAME, classmateInfo.getStudentName());
        return contentValues;
    }
}
