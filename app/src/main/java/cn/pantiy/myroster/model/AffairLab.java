package cn.pantiy.myroster.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.pantiy.myroster.database.Affair.AffairCursorWrapper;
import cn.pantiy.myroster.database.Affair.AffairDatabase;
import cn.pantiy.myroster.database.Affair.AffairDatabaseHelper;

import static cn.pantiy.myroster.database.Affair.AffairDatabase.Table;

/**
 * MyRoster
 * cn.pantiy.myroster.model
 * Created by pantiy on 17-6-8.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairLab {

    private static AffairLab sAffairLab;

    private SQLiteDatabase mSQLiteDatabase;

    public static AffairLab touch(Context context) {
        if (sAffairLab == null) {
            sAffairLab = new AffairLab(context);
        }
        return sAffairLab;
    }

    private AffairLab(Context context) {
        mSQLiteDatabase = new AffairDatabaseHelper(context).getWritableDatabase();
    }

    public List<Affair> getAffairList() {
        List<Affair> affairList = new ArrayList<>();
        AffairCursorWrapper cursorWrapper = getCursorWrapper();
        if (cursorWrapper.getCount() == 0) {
            return affairList;
        }
        cursorWrapper.moveToFirst();
        while (!cursorWrapper.isAfterLast()) {
            affairList.add(cursorWrapper.getAffair());
            cursorWrapper.moveToNext();
        }
        cursorWrapper.close();
        return affairList;
    }

    public void addAffair(Affair affair) {
        mSQLiteDatabase.insert(AffairDatabase.NAME, null, getContentValues(affair));
    }

    public void updateAffair(Affair affair) {
        mSQLiteDatabase.update(AffairDatabase.NAME, getContentValues(affair),
                Table.AFFAIR_NAME + "=?", new String[] {affair.getAffairName()});
    }

    private AffairCursorWrapper getCursorWrapper() {
        Cursor cursor = mSQLiteDatabase.query(
                AffairDatabase.NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        return new AffairCursorWrapper(cursor);
    }

    private ContentValues getContentValues(Affair affair) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.AFFAIR_NAME, affair.getAffairName());
        contentValues.put(Table.STATE_ARRAY, affair.stateArrayToString(affair.getStateArray()));
        return contentValues;
    }
}
