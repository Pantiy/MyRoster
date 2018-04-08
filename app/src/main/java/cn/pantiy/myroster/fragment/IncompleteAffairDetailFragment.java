package cn.pantiy.myroster.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.UUID;
import cn.pantiy.myroster.R;
import cn.pantiy.myroster.adapter.AffairDetailAdapter;
import cn.pantiy.myroster.model.RosterInAffairLab;
import cn.refactor.library.SmoothCheckBox;

/**
 * Created by Pantiy on 2018/3/31.
 * Copyright © 2016 All rights Reserved by Pantiy
 */
public class IncompleteAffairDetailFragment extends AffairDetailFragment {

//    private static final String KEY_AFFAIR_ID = "affairId";

//    private Affair mAffair;

    private ListView mClassmateInfoLv;
    private Button mConfirmBtn;

    public static IncompleteAffairDetailFragment newInstance(UUID affairId) {
        IncompleteAffairDetailFragment fragment = new IncompleteAffairDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_AFFAIR_ID, affairId);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    protected void initData() {
//        UUID affairId = (UUID) getArguments().getSerializable(KEY_AFFAIR_ID);
//        mAffair = AffairLab.touch(mContext).queryAffair(affairId);
//    }

    @Override
    protected void initViews(View view) {
        mClassmateInfoLv = view.findViewById(R.id.classmateInfo_lv);
        mConfirmBtn = view.findViewById(R.id.confirm_btn);
    }

    @Override
    protected void setupAdapter() {
        mClassmateInfoLv.setAdapter(new AffairDetailAdapter(mContext, mAffair, mAffair.isFinish()));
    }

    @Override
    protected void setupListener() {

        mClassmateInfoLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SmoothCheckBox state = view.findViewById(R.id.state_checkBox);
                state.setChecked(!state.isChecked(), true);
            }
        });

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AffairDetailAdapter adapter = (AffairDetailAdapter) mClassmateInfoLv.getAdapter();

                if (adapter.getChangedClassmateInfoList().size() != 0) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            RosterInAffairLab.touch(mContext, mAffair.getId().toString())
                                    .updateState(adapter.getChangedClassmateInfoList());
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.clearChanged();
                                    mCallback.onIncompleteNumChanged();
                                    Toast.makeText(mContext, "已更改", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }.start();
                } else {
                    Toast.makeText(mContext, "无更改项", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_imcomplete_affair_detail;
    }
}
