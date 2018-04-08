package cn.pantiy.myroster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import cn.pantiy.myroster.R;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.ClassmateInfo;
import cn.refactor.library.SmoothCheckBox;

/**
 * MyRoster
 * cn.pantiy.myroster.adapter
 * Created by pantiy on 17-6-7.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairDetailAdapter extends BaseAdapter {

    private static final String TAG = "AffairDetailAdapter";

    private boolean mAffairIsFinished;

    private Context mContext;

    private Affair mAffair;
    private List<ClassmateInfo> mClassmateInfoList;
    private List<ClassmateInfo> mChangedClassmateInfoList;
    private boolean[] mChangedMarks;

    private AffairDetailAdapter(Context context, Affair affair) {
        mContext = context;
        mAffair = affair;
        mAffairIsFinished = mAffair.isFinish();
    }

    public AffairDetailAdapter(Context context, Affair affair, boolean isFinish) {
        this(context, affair);
        if (mAffairIsFinished) {
            mClassmateInfoList = selectClassmateInfo(affair.getClassmateInfoList(), isFinish);
        } else {
            mClassmateInfoList = mAffair.getClassmateInfoList();
            mChangedClassmateInfoList = new LinkedList<>();
            mChangedMarks = new boolean[mClassmateInfoList.size()];
        }
    }

    @Override
    public int getCount() {
        return mClassmateInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mClassmateInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_for_affair_detail,
                parent, false);
        TextView studentNum = convertView.findViewById(R.id.studentNum_tv);
        studentNum.setText(mClassmateInfoList.get(position).getStudentNum());
        TextView studentName = convertView.findViewById(R.id.studentName_tv);
        studentName.setText(mClassmateInfoList.get(position).getStudentName());
        SmoothCheckBox state = convertView.findViewById(R.id.state_checkBox);
        state.setChecked(mClassmateInfoList.get(position).getState());
        if (mAffairIsFinished) {
            state.setClickable(false);
        } else {
            state.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                    mClassmateInfoList.get(position).setState(isChecked);
                    if (!mChangedMarks[position]) {
                        mChangedClassmateInfoList.add(mClassmateInfoList.get(position));
                    } else {
                        mChangedClassmateInfoList.remove(mClassmateInfoList.get(position));
                    }
                    mChangedMarks[position] = !mChangedMarks[position];
                }
            });
        }

        return convertView;
    }

    public List<ClassmateInfo> getChangedClassmateInfoList() {
        return mChangedClassmateInfoList;
    }

    public void clearChanged() {
        mChangedClassmateInfoList = new LinkedList<>();
        mChangedMarks = new boolean[mClassmateInfoList.size()];
    }

    private List<ClassmateInfo> selectClassmateInfo(List<ClassmateInfo> classmateInfoList, boolean state) {
        List<ClassmateInfo> selectedClassmateInfoList = new ArrayList<>();
        for (ClassmateInfo classmateInfo : classmateInfoList) {
            if (classmateInfo.getState() == state) {
                selectedClassmateInfoList.add(classmateInfo);
            }
        }
        return selectedClassmateInfoList;
    }

}
