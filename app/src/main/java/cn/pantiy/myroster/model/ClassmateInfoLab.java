package cn.pantiy.myroster.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.pantiy.myroster.database.ClassmateInfo.ClassmateInfoCursorWrapper;
import cn.pantiy.myroster.database.ClassmateInfo.ClassmateInfoDatabase;
import cn.pantiy.myroster.database.ClassmateInfo.ClassmateInfoDatabaseHelper;

import static cn.pantiy.myroster.database.ClassmateInfo.ClassmateInfoDatabase.Table;

/**
 * Created by Pantiy on 2017/6/2.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class ClassmateInfoLab {

    private static final String TAG = "ClassmateInfoLab";

    private static ClassmateInfoLab sClassmateInfoLab;

    private SQLiteDatabase mSQLiteDatabase;

    public static ClassmateInfoLab touch(Context context) {
        if (sClassmateInfoLab == null) {
            sClassmateInfoLab = new ClassmateInfoLab(context);
        }
        return sClassmateInfoLab;
    }

    private ClassmateInfoLab (Context context) {
        mSQLiteDatabase = new ClassmateInfoDatabaseHelper(context).getReadableDatabase();
    }

    public List<ClassmateInfo> queryClassmateInfoList() {
        List<ClassmateInfo> classmateInfoList = new ArrayList<>();
        ClassmateInfoCursorWrapper cursorWrapper = getCursorWrapper();
        if (cursorWrapper.getCount() == 0) {
            return classmateInfoList;
        }
        cursorWrapper.moveToFirst();
        while (!cursorWrapper.isAfterLast()) {
            classmateInfoList.add(cursorWrapper.getClassmateInfo(true));
            cursorWrapper.moveToNext();
        }
        cursorWrapper.close();
        return classmateInfoList;
    }

    public void setClassmateInfoList(List<String[]> excelContent) {
        List<ClassmateInfo> classmateInfoList = new ArrayList<>();
        for (int i = 0; i < excelContent.size(); i++) {
            String[] rowContent = excelContent.get(i);
            ClassmateInfo classmateInfo = new ClassmateInfo(rowContent[0], rowContent[1]);
            classmateInfoList.add(classmateInfo);
        }
        Log.i(TAG, "setSelectedClassmateInfoList()");
        saveClassmateInfoList(cleanEmptyInfo(classmateInfoList));
    }

    public void deleteForm() {
        mSQLiteDatabase.execSQL("DELETE FROM " + ClassmateInfoDatabase.NAME);
    }

    private void saveClassmateInfoList(List<ClassmateInfo> classmateInfoList) {
        for (ClassmateInfo classmateInfo : classmateInfoList) {
            insertClassmateInfo(getContentValues(classmateInfo));
        }
    }

    private void insertClassmateInfo(ContentValues contentValues) {
        mSQLiteDatabase.insert(ClassmateInfoDatabase.NAME, null, contentValues);
    }

    private ClassmateInfoCursorWrapper getCursorWrapper() {

        Cursor cursor = mSQLiteDatabase.query(
                ClassmateInfoDatabase.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        return new ClassmateInfoCursorWrapper(cursor);
    }

    private ContentValues getContentValues(ClassmateInfo classmateInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.STUDENT_NUM, classmateInfo.getStudentNum());
        contentValues.put(Table.STUDENT_NAME, classmateInfo.getStudentName());
        return contentValues;
    }

    private List<ClassmateInfo> cleanEmptyInfo(List<ClassmateInfo> classmateInfoList) {
        for (int i = 0; i < classmateInfoList.size(); i++) {
            ClassmateInfo classmateInfo = classmateInfoList.get(i);
            if (classmateInfo.getStudentName() == null || classmateInfo.getStudentNum() == null) {
                classmateInfoList.remove(i);
                i--;
            }
        }
        return classmateInfoList;
    }
}
