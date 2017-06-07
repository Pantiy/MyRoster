package cn.pantiy.myroster.database.Affair;

/**
 * MyRoster
 * cn.pantiy.myroster.database.Affair
 * Created by pantiy on 17-6-8.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public final class AffairDatabase {

    public static final String NAME = "affair";

    public static String getSql() {
        return "create table " + NAME + "(" +
                Table.AFFAIR_NAME + "," +
                Table.STATE_ARRAY + ")";
    }

    public static final class Table {
        public static final String AFFAIR_NAME = "affair_name";
        public static final String STATE_ARRAY = "state_array";
    }
}
