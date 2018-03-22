package cn.pantiy.myroster.database.ClassmateInfo;

import android.database.Cursor;
import android.database.CursorWrapper;

import cn.pantiy.myroster.model.ClassmateInfo;

/**
 * MyRoster
 * cn.pantiy.myroster.database
 * Created by pantiy on 17-6-7.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class ClassmateInfoCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ClassmateInfoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ClassmateInfo getClassmateInfo(boolean invokeByClassmateInfoLab) {

        String studentNum = getString(getColumnIndex(ClassmateInfoDatabase.Table.STUDENT_NUM));
        String studentName = getString(getColumnIndex(ClassmateInfoDatabase.Table.STUDENT_NAME));
        boolean state = false;
        if (!invokeByClassmateInfoLab) {
            state = (getInt(getColumnIndex(ClassmateInfoDatabase.Table.STUDENT_STATE)) == 1);
        }

        return new ClassmateInfo(studentNum, studentName, state);
    }


}
