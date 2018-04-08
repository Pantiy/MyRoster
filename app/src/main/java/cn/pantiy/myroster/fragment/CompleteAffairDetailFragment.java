package cn.pantiy.myroster.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.UUID;
import cn.pantiy.myroster.R;
import cn.pantiy.myroster.adapter.AffairDetailAdapter;

/**
 * MyRoster
 * cn.pantiy.myroster.fragment
 * Created by pantiy on 17-6-14.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class CompleteAffairDetailFragment extends AffairDetailFragment {

    private static final String TAG = "AffairDetailFragment";

    private static final int INCOMPLETE = 0;
    private static final int FINISHED = 1;

    private RadioGroup mRadioGroup;
    private ListView mIncompleteLv;
    private ListView mFinishedLv;

    public static CompleteAffairDetailFragment newInstance(UUID affairId) {
        CompleteAffairDetailFragment completeAffairDetailFragment = new CompleteAffairDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_AFFAIR_ID, affairId);
        completeAffairDetailFragment.setArguments(args);
        return completeAffairDetailFragment;
    }

    @Override
    protected void initViews(View view) {
        mRadioGroup = view.findViewById(R.id.radioGroup);
        mIncompleteLv = view.findViewById(R.id.incomplete_lv);
        mFinishedLv = view.findViewById(R.id.finished_lv);
        RadioButton finishedRadioBtn = mView.findViewById(R.id.finished_radioBtn);
        finishedRadioBtn.setChecked(true);
        switchListView(FINISHED);
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
            mFinishedLv.setAdapter(new AffairDetailAdapter(mContext, mAffair, true));
        } else {
            mIncompleteLv.setAdapter(new AffairDetailAdapter(mContext, mAffair, false));
        }
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_complete_affair_detail;
    }

}