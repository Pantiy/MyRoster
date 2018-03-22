package cn.pantiy.myroster.database.ClassmateInfo;

/**
 * MyRoster
 * cn.pantiy.myroster.database
 * Created by pantiy on 17-6-7.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public final class ClassmateInfoDatabase {

    public static final String NAME = "classmate_info";

    public static String getSql() {
        return "create table " + NAME + "(" +
                Table.STUDENT_NUM + "," +
                Table.STUDENT_NAME + ")";
    }

    public static final class Table {
        public static final String STUDENT_NUM = "student_num";
        public static final String STUDENT_NAME = "student_name";
        public static final String STUDENT_STATE = "student_state";
    }
}
