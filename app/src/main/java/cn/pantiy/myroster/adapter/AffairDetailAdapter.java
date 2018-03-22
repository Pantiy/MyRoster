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
    private List<ClassmateInfo> mSelectedClassmateInfoList;
//    private boolean[] mStateArray;

    private OnAffairContentChangeListener mOnAffairContentChangeListener;

    private AffairDetailAdapter(Context context, Affair affair) {
        mContext = context;
        mAffair = affair;
//        mSelectedClassmateInfoList = new ArrayList<>();
        //mSelectedClassmateInfoList = RosterInAffairLab.touch(context, affair.getId().toString()).getRoster();
//        mStateArray = affair.getStateArray();
    }

    public AffairDetailAdapter(Context context, Affair affair, boolean isFinish) {
        this(context, affair);
        mIsFinish = isFinish;
        mSelectedClassmateInfoList = selectClassmateInfo(affair.getClassmateInfoList(), isFinish);
    }

    @Override
    public int getCount() {
        return mSelectedClassmateInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSelectedClassmateInfoList.get(position);
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
        studentNum.setText(mSelectedClassmateInfoList.get(position).getStudentNum());
        TextView studentName = (TextView) convertView.findViewById(R.id.studentName_tv);
        studentName.setText(mSelectedClassmateInfoList.get(position).getStudentName());
        final CheckBox state = (CheckBox) convertView.findViewById(R.id.state_cb);
        state.setChecked(mSelectedClassmateInfoList.get(position).getState());
        Log.i(TAG, mAffair.isFinish() + "");
        if (!mAffair.isFinish()) {
            state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mSelectedClassmateInfoList.get(position).setState(isChecked);
//                    mStateArray[position] = isChecked;
//                    mAffair.setStateArray(mStateArray);
//                    List<ClassmateInfo> classmateInfoList = new ArrayList<>();
//                    boolean[] stateArray = new boolean[mStateArray.length - 1];
//                    for (int i = 0, index = 0; i < mStateArray.length; i++) {
//                        if (mStateArray[i] != isChecked) {
//                            stateArray[index] = mStateArray[i];
//                            classmateInfoList.add(mSelectedClassmateInfoList.get(i));
//                            index++;
//                        }
//                    }
                    mOnAffairContentChangeListener.onAffairContentChanged(mSelectedClassmateInfoList.remove(position), mIsFinish);
                }
            });
        } else {
            state.setClickable(false);
        }
        return convertView;
    }

//    private void setPrescribedAffairDetail(boolean isFinish) {
//        int count = 0;
//        for (boolean b : mStateArray) {
//            if (b == isFinish) {
//                count++;
//            }
//        }
//        List<ClassmateInfo> classmateInfoList = new ArrayList<>();
//        boolean[] stateArray = new boolean[count];
//        for (int n = 0, index = 0; index < count; n++) {
//            if (mStateArray[n] == isFinish) {
//                classmateInfoList.add(mSelectedClassmateInfoList.get(n));
//                stateArray[index] = isFinish;
//                index++;
//            }
//        }
//        mSelectedClassmateInfoList = classmateInfoList;
////        mStateArray = stateArray;
//        Log.i(TAG, "prescribedStateArray(" + isFinish + "):" + mAffair.stateArrayToString(stateArray));
//        mAffair = new Affair(mAffair.getId(), mAffair.getAffairName(), mAffair.isFinish());
//        mAffair.setSelectedClassmateInfoList(classmateInfoList);
//        mAffair.setStateArray(stateArray);
//    }

    public void updateData() {
//        setSelectedClassmateInfoList(selectClassmateInfo(classmateInfoList, mIsFinish));

//        setStateArray(stateArray);
        this.notifyDataSetChanged();
    }

    public List<ClassmateInfo> getSelectedClassmateInfoList() {
        return mSelectedClassmateInfoList;
    }

    public void setSelectedClassmateInfoList(List<ClassmateInfo> selectedClassmateInfoList) {
        mSelectedClassmateInfoList = selectedClassmateInfoList;
    }

//    public boolean[] getStateArray() {
//        return mStateArray;
//    }

//    public void setStateArray(boolean[] stateArray) {
//        mStateArray = stateArray;
//    }

    private List<ClassmateInfo> selectClassmateInfo(List<ClassmateInfo> classmateInfoList, boolean state) {
        List<ClassmateInfo> selectedClassmateInfoList = new ArrayList<>();
        for (ClassmateInfo classmateInfo : classmateInfoList) {
            if (classmateInfo.getState() == state) {
                selectedClassmateInfoList.add(classmateInfo);
            }
        }
        return selectedClassmateInfoList;
    }

    public boolean isFinish() {
        return mIsFinish;
    }

    public void setOnAffairContentChangeListener(OnAffairContentChangeListener listener) {
        mOnAffairContentChangeListener = listener;
    }

    public interface OnAffairContentChangeListener {
        void onAffairContentChanged(ClassmateInfo changed, boolean isFinish);
    }
}
