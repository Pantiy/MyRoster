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
import java.util.List;
import cn.pantiy.myroster.R;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.AffairLab;
import cn.pantiy.myroster.model.ClassmateInfo;

/**
 * MyRoster
 * cn.pantiy.myroster.adapter
 * Created by pantiy on 17-6-7.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairDetailAdapter extends BaseAdapter {

    private static final String TAG = "AffairDetailAdapter";

    private Context mContext;
    private Affair mAffair;
    private List<ClassmateInfo> mClassmateInfoList;
    private boolean[] mStateArray;

    public AffairDetailAdapter(Context context, Affair affair) {
        mContext = context;
        mAffair = affair;
        mClassmateInfoList = affair.getClassmateInfoList();
        mStateArray = affair.getStateArray();
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
        CheckBox state = (CheckBox) convertView.findViewById(R.id.state_cb);
        state.setChecked(mStateArray[position]);
        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mStateArray[position] = isChecked;
                mAffair.setStateArray(mStateArray);
                Log.i(TAG, "stateArray" + mAffair.stateArrayToString(mStateArray));
            }
        });
        return convertView;
    }

    public void updateAffair() {
        AffairLab.touch(mContext).updateAffair(mAffair);
    }
}
