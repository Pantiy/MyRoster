package cn.pantiy.myroster.model;

import java.util.List;

/**
 * MyRoster
 * cn.pantiy.myroster.model
 * Created by pantiy on 17-6-7.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class Affair {

    private String mAffairName;
    private List<ClassmateInfo> mClassmateInfoList;
    private boolean[] mStateArray;

    public Affair(String affairName, List<ClassmateInfo> classmateInfoList) {
        setAffairName(affairName);
        setClassmateInfoList(classmateInfoList);
        mStateArray = new boolean[classmateInfoList.size()];
        for (int i = 0; i < mStateArray.length; i++) {
            mStateArray[i] = false;
        }
    }

    public String getAffairName() {
        return mAffairName;
    }

    public void setAffairName(String affairName) {
        mAffairName = affairName;
    }

    public List<ClassmateInfo> getClassmateInfoList() {
        return mClassmateInfoList;
    }

    public void setClassmateInfoList(List<ClassmateInfo> classmateInfoList) {
        mClassmateInfoList = classmateInfoList;
    }

    public boolean[] getStateArray() {
        return mStateArray;
    }

    public void setStateArray(boolean[] stateArray) {
        mStateArray = stateArray;
    }

    public boolean[] parseStateArrayString(String stateArrayString) {
        boolean[] stateArray = new boolean[stateArrayString.length()];
        for (int i = 0; i < stateArray.length; i++) {
            stateArray[i] = stateArrayString.charAt(i) == '1';
        }
        return stateArray;
    }

    public String stateArrayToString(boolean[] stateArray) {
        String stateArrayString = "";
        for (boolean state : stateArray) {
            stateArrayString += state? "1" : "0";
        }
        return stateArrayString;
    }
}
