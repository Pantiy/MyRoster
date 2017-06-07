package cn.pantiy.myroster.database.Affair;

import android.database.Cursor;
import android.database.CursorWrapper;

import cn.pantiy.myroster.MyApplication;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.ClassmateInfoLab;

import static cn.pantiy.myroster.database.Affair.AffairDatabase.*;

/**
 * MyRoster
 * cn.pantiy.myroster.database.Affair
 * Created by pantiy on 17-6-8.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairCursorWrapper extends CursorWrapper {

    public AffairCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Affair getAffair() {

        String affairName = getString(getColumnIndex(Table.AFFAIR_NAME));
        String stateArrayString = getString(getColumnIndex(Table.STATE_ARRAY));

        boolean[] stateArray = new boolean[stateArrayString.length()];
        for (int i = 0; i < stateArray.length; i++) {
            stateArray[i] = stateArrayString.charAt(i) == '1';
        }

        Affair affair = new Affair(affairName,
                ClassmateInfoLab.touch(MyApplication.getContext()).getClassmateInfoList());
        affair.setStateArray(stateArray);

        return affair;
    }
}
