package cn.pantiy.myroster.database.Affair;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import cn.pantiy.myroster.model.Affair;

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

        String id = getString(getColumnIndex(Table.ID));
        String affairName = getString(getColumnIndex(Table.AFFAIR_NAME));
        String stateArrayString = getString(getColumnIndex(Table.STATE_ARRAY));
        String isFinish = getString(getColumnIndex(Table.IS_FINISH));

        Affair affair = new Affair(UUID.fromString(id), affairName);
        affair.setStateArray(affair.parseStateArrayString(stateArrayString));
        affair.setFinish(isFinish.equals("1"));

        return affair;
    }
}
