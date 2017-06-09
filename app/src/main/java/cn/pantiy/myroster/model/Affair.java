package cn.pantiy.myroster.model;

import java.util.List;
import java.util.UUID;

import cn.pantiy.myroster.global.MyApplication;

/**
 * MyRoster
 * cn.pantiy.myroster.model
 * Created by pantiy on 17-6-7.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class Affair {

    private UUID mId;
    private String mAffairName;
    private List<ClassmateInfo> mClassmateInfoList;
    private boolean[] mStateArray;
    private boolean mIsFinish;

    public Affair(String affairName) {
        this(UUID.randomUUID(), affairName);
    }

    public Affair(UUID id, String affairName) {
        mId = id;
        setAffairName(affairName);
        setClassmateInfoList(ClassmateInfoLab.touch(MyApplication.getContext()).getClassmateInfoList());
        mStateArray = new boolean[getClassmateInfoList().size()];
        for (int i = 0; i < mStateArray.length; i++) {
            mStateArray[i] = false;
        }
        mIsFinish = false;
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

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public boolean isFinish() {
        return mIsFinish;
    }

    public void setFinish(boolean finish) {
        mIsFinish = finish;
    }
}
