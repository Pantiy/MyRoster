package cn.pantiy.myroster.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.pantiy.myroster.R;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.ClassmateInfo;

/**
 * MyRoster
 * cn.pantiy.myroster.adapter
 * Created by pantiy on 17-6-7.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairDetailAdapter extends BaseAdapter {

    private static final String TAG = "AffairDetailAdapter";

    private boolean mIsFinish;

    private Context mContext;
    private Affair mAffair;
    private List<ClassmateInfo> mClassmateInfoList;
    private boolean[] mStateArray;

    private OnAffairContentChangeListener mOnAffairContentChangeListener;

    private AffairDetailAdapter(Context context, Affair affair) {
        mContext = context;
        mAffair = affair;
        mClassmateInfoList = affair.getClassmateInfoList();
        mStateArray = affair.getStateArray();
    }

    public AffairDetailAdapter(Context context, Affair affair, boolean isFinish) {
        this(context, affair);
        mIsFinish = isFinish;
        setPrescribedAffairDetail(mIsFinish);
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
        TextView studentNum = (TextView) convertView.findViewById(R.id.studentNum_tv);
        studentNum.setText(mClassmateInfoList.get(position).getStudentNum());
        TextView studentName = (TextView) convertView.findViewById(R.id.studentName_tv);
        studentName.setText(mClassmateInfoList.get(position).getStudentName());
        final CheckBox state = (CheckBox) convertView.findViewById(R.id.state_cb);
        state.setChecked(mStateArray[position]);
        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                state.setClickable(false);
                mStateArray[position] = isChecked;
                mAffair.setStateArray(mStateArray);
                List<ClassmateInfo> classmateInfoList = new ArrayList<>();
                boolean[] stateArray = new boolean[mStateArray.length - 1];
                for (int i = 0, index = 0; i < mStateArray.length; i++) {
                    if (mStateArray[i] != isChecked) {
                        stateArray[index] = mStateArray[i];
                        classmateInfoList.add(mClassmateInfoList.get(i));
                        index++;
                    }
                }
                mOnAffairContentChangeListener.onAffairContentChanged(classmateInfoList, stateArray,
                        mIsFinish);
            }
        });
        return convertView;
    }

    private void setPrescribedAffairDetail(boolean isFinish) {
        int count = 0;
        for (boolean b : mStateArray) {
            if (b == isFinish) {
                count++;
            }
        }
        List<ClassmateInfo> classmateInfoList = new ArrayList<>();
        boolean[] stateArray = new boolean[count];
        for (int n = 0, index = 0; index < count; n++) {
            if (mStateArray[n] == isFinish) {
                classmateInfoList.add(mClassmateInfoList.get(n));
                stateArray[index] = isFinish;
                index++;
            }
        }
        mClassmateInfoList = classmateInfoList;
        mStateArray = stateArray;
        Log.i(TAG, "prescribedStateArray(" + isFinish + "):" + mAffair.stateArrayToString(stateArray));
        mAffair = new Affair(mAffair.getId(), mAffair.getAffairName());
        mAffair.setClassmateInfoList(classmateInfoList);
        mAffair.setStateArray(stateArray);
    }

    public void updateData(List<ClassmateInfo> classmateInfoList, boolean[] stateArray) {
        setClassmateInfoList(classmateInfoList);
        setStateArray(stateArray);
        this.notifyDataSetChanged();
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

    public boolean isFinish() {
        return mIsFinish;
    }

    public void setOnAffairContentChangeListener(OnAffairContentChangeListener listener) {
        mOnAffairContentChangeListener = listener;
    }

    public interface OnAffairContentChangeListener {
        void onAffairContentChanged(List<ClassmateInfo> classmateInfoList, boolean[] stateArray,
                                    boolean isFinish);
    }
}
