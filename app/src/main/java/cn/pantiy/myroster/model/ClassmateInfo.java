package cn.pantiy.myroster.model;

/**
 * Test
 * com.pantiy.test
 * Created by Pantiy on 2017/6/2.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class ClassmateInfo {

    private String mStudentNum;
    private String mName;
//    private boolean mIsFinish;

    public ClassmateInfo(String studentNum, String studentName) {
        setStudentNum(studentNum);
        setName(studentName);
    }

    public String getStudentName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getStudentNum() {
        return mStudentNum;
    }

    public void setStudentNum(String studentNum) {
        mStudentNum = studentNum;
    }

    public boolean equals(ClassmateInfo classmateInfo) {
        return this.getStudentNum().equals(classmateInfo.getStudentNum());
    }
}
