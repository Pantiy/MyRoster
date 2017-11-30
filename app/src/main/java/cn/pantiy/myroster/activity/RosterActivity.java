package cn.pantiy.myroster.activity;

import android.graphics.Color;
import android.widget.ListView;
import android.widget.TextView;

import cn.pantiy.myroster.R;
import cn.pantiy.myroster.adapter.RosterAdapter;
import cn.pantiy.myroster.model.ClassmateInfoLab;

/**
 * Created by Pantiy on 2017/11/30.
 * Copyright © 2016 All rights Reserved by Pantiy
 */

public class RosterActivity extends BaseActivity {

    private TextView mStudentNumTv;
    private TextView mStudentNameTv;
    private ListView mRosterLv;

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mStudentNumTv = (TextView) findViewById(R.id.studentNum_tv);
        mStudentNumTv.setText("学号");
        mStudentNumTv.setTextColor(Color.BLACK);
        mStudentNameTv = (TextView) findViewById(R.id.studentName_tv);
        mStudentNameTv.setText("姓名");
        mStudentNameTv.setTextColor(Color.BLACK);
        mRosterLv = (ListView) findViewById(R.id.roster_lv);
    }

    @Override
    protected void setupAdapter() {
        mRosterLv.setAdapter(new RosterAdapter(this,
                ClassmateInfoLab.touch(this).getClassmateInfoList()));
    }

    @Override
    protected void setupListener() {

    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_roster;
    }
}
