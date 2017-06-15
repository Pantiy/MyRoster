package cn.pantiy.myroster.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.List;
import java.util.UUID;

import cn.pantiy.myroster.R;
import cn.pantiy.myroster.adapter.AffairDetailAdapter;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.AffairLab;
import cn.pantiy.myroster.model.ClassmateInfo;

/**
 * MyRoster
 * cn.pantiy.myroster.fragment
 * Created by pantiy on 17-6-14.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairDetailFragment extends BaseFragment implements AffairDetailAdapter.OnAffairContentChangeListener{

    private static final String TAG = "AffairDetailFragment";

    private static final String KEY_AFFAIR_ID = "affairId";

    private static final int INCOMPLETE = 0;
    private static final int FINISHED = 1;

    private Affair mAffair;

    private RadioGroup mRadioGroup;
    private ListView mIncompleteLv;
    private ListView mFinishedLv;

    private AffairDetailAdapter mIncompleteAdapter;
    private AffairDetailAdapter mFinishedAdapter;

    public static AffairDetailFragment newInstance(UUID affairId) {
        AffairDetailFragment affairDetailFragment = new AffairDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_AFFAIR_ID, affairId);
        affairDetailFragment.setArguments(args);
        return affairDetailFragment;
    }

    @Override
    protected void initData() {
        UUID affairId = (UUID) getArguments().getSerializable(KEY_AFFAIR_ID);
        mAffair = AffairLab.touch(mContext).getAffair(affairId);
    }

    @Override
    protected void initViews(View view) {
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        mIncompleteLv = (ListView) view.findViewById(R.id.incomplete_lv);
        mFinishedLv = (ListView) view.findViewById(R.id.finished_lv);
    }

    @Override
    protected void setupAdapter() {
        setAffairDetailAdapter(false);
        setAffairDetailAdapter(true);
    }

    @Override
    protected void setupListener() {

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.incomplete_radioBtn:
                        switchListView(INCOMPLETE);
                        break;
                    case R.id.finished_radioBtn:
                        switchListView(FINISHED);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void updateAdapterData(AffairDetailAdapter adapter, List<ClassmateInfo> classmateInfoList,
                                   boolean[] stateArray) {
        adapter.updateData(classmateInfoList, stateArray);
        updateAffair(classmateInfoList, adapter.isFinish());
        setAffairDetailAdapter(!adapter.isFinish());
    }

    private void updateAffair(List<ClassmateInfo> classmateInfoList, boolean isFinish) {
        List<ClassmateInfo> allClassmateInfoList = mAffair.getClassmateInfoList();
        boolean[] allStateArray = mAffair.getStateArray();
        int index = 0;
        for (int i = 0; i < allClassmateInfoList.size(); i++) {
            if (index < classmateInfoList.size()
                    && allClassmateInfoList.get(i).equals(classmateInfoList.get(index))) {
                index++;
            } else {
                if (allStateArray[i] == isFinish) {
                    Log.i(TAG, "oldStateArray:" + mAffair.stateArrayToString(allStateArray));
                    allStateArray[i] = !allStateArray[i];
                    Log.i(TAG, "newStateArray:" + mAffair.stateArrayToString(allStateArray));
                }
            }
        }
        AffairLab.touch(mContext).updateAffair(mAffair);
    }

    private void switchListView(int current) {

        switch (current) {
            case INCOMPLETE:
                mIncompleteLv.setVisibility(View.VISIBLE);
                mFinishedLv.setVisibility(View.GONE);
                break;
            case FINISHED:
                mIncompleteLv.setVisibility(View.GONE);
                mFinishedLv.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void setAffairDetailAdapter(boolean isFinish) {
        if (isFinish) {
            mFinishedAdapter = new AffairDetailAdapter(mContext, mAffair, true);
            mFinishedAdapter.setOnAffairContentChangeListener(this);
            mFinishedLv.setAdapter(mFinishedAdapter);
        } else {
            mIncompleteAdapter = new AffairDetailAdapter(mContext, mAffair, false);
            mIncompleteAdapter.setOnAffairContentChangeListener(this);
            mIncompleteLv.setAdapter(mIncompleteAdapter);
        }
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_affair_detail;
    }

    @Override
    public void onAffairContentChanged(List<ClassmateInfo> classmateInfoList, boolean[] stateArray,
                                       boolean isFinish) {
        if (isFinish) {
            updateAdapterData(mFinishedAdapter, classmateInfoList, stateArray);
        } else {
            updateAdapterData(mIncompleteAdapter, classmateInfoList, stateArray);
        }
    }
}
