package cn.pantiy.myroster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.pantiy.myroster.R;
import cn.pantiy.myroster.model.ClassmateInfo;

/**
 * Created by Pantiy on 2017/11/30.
 * Copyright © 2016 All rights Reserved by Pantiy
 */

public class RosterAdapter extends BaseAdapter {

    private Context mContext;

    private List<ClassmateInfo> mClassmateInfoList;

    public RosterAdapter(Context context, List<ClassmateInfo> classmateInfoList) {
        mContext = context;
        mClassmateInfoList = classmateInfoList;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ClassmateInfo classmateInfo = (ClassmateInfo) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_for_roster, parent,
                    false);
        }

        TextView studentNum = (TextView) convertView.findViewById(R.id.studentNum_tv);
        studentNum.setText(classmateInfo.getStudentNum());
        TextView studentName = (TextView) convertView.findViewById(R.id.studentName_tv);
        studentName.setText(classmateInfo.getStudentName());

        return convertView;
    }
}
