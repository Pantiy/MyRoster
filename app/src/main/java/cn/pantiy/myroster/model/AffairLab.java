package cn.pantiy.myroster.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    public static AffairLab touch(Context context) {
        if (sAffairLab == null) {
            sAffairLab = new AffairLab(context);
        }
        return sAffairLab;
    }

    private AffairLab(Context context) {
        mContext = context;
        mSQLiteDatabase = new AffairDatabaseHelper(mContext).getWritableDatabase();
    }

    public List<Affair> queryAffairList(boolean isFinish) {
        List<Affair> affairList = queryAffairList();
        List<Affair> affairs = new ArrayList<>();
        for (int i = 0; i < affairList.size(); i++) {
            Affair affair = affairList.get(i);
            if (affair.isFinish() == isFinish) {
                affairs.add(affair);
            }
        }
        return affairs;
    }

    public List<Affair> queryAffairList() {
        List<Affair> affairList = new ArrayList<>();
        AffairCursorWrapper cursorWrapper = getCursorWrapper();
        if (cursorWrapper.getCount() == 0) {
            return affairList;
        }
        cursorWrapper.moveToLast();
        while (!cursorWrapper.isBeforeFirst()) {
            affairList.add(cursorWrapper.getAffair());
            cursorWrapper.moveToPrevious();
        }
        cursorWrapper.close();
        return affairList;
    }

    public Affair queryAffair(UUID affairId) {
        AffairCursorWrapper cursorWrapper = getAffairCursorWrapper(Table.ID + "=?",
                new String[] {affairId.toString()});
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToLast();
            return cursorWrapper.getAffair();
        } finally {
            cursorWrapper.close();
        }
    }

    public void insertAffair(Affair affair) {
        mSQLiteDatabase.insert(AffairDatabase.NAME, null, getContentValues(affair));
    }

    public void updateAffair(Affair affair) {
        mSQLiteDatabase.update(AffairDatabase.NAME, getContentValues(affair),
                Table.ID + "=?", new String[] {affair.getId().toString()});
    }

    public void deleteAffair(Affair affair) {
        mSQLiteDatabase.delete(AffairDatabase.NAME, Table.ID + "=?",
                new String[] {affair.getId().toString()});
//        RosterInAffairLab.touch(mContext, affair.getId().toString()).delete();
        mContext.deleteDatabase(affair.getId().toString());
    }

    public void deleteForm() {
        mSQLiteDatabase.execSQL("DELETE FROM " + AffairDatabase.NAME);
    }

    private AffairCursorWrapper getAffairCursorWrapper(String selection, String[] selectionArgs) {
        Cursor cursor = mSQLiteDatabase.query(
                AffairDatabase.NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        return new AffairCursorWrapper(cursor);
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
        contentValues.put(Table.ID, affair.getId().toString());
        contentValues.put(Table.CREATE_TIME, affair.getCreateTime().getTime());
        contentValues.put(Table.AFFAIR_NAME, affair.getAffairName());
//        contentValues.put(Table.STATE_ARRAY, affair.stateArrayToString(affair.getStateArray()));
        contentValues.put(Table.IS_FINISH, affair.isFinish()? "1" : "0");
        return contentValues;
    }
}
