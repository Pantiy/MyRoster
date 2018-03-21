package cn.pantiy.myroster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.pantiy.myroster.R;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.AffairLab;
import cn.pantiy.myroster.utils.TimeUtil;

/**
 * MyRoster
 * cn.pantiy.myroster.adapter
 * Created by pantiy on 17-6-8.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairAdapter extends BaseAdapter {

    private List<Affair> mAffairList;
    private boolean mAffairIsFinish;

    private Context mContext;

    private OnAffairListChangeListener mOnAffairListChangeListener;

    public AffairAdapter(Context context) {
        mContext = context;
        mAffairList = AffairLab.touch(context).queryAffairList();
    }

    public AffairAdapter(Context context, boolean isFinish) {
        mContext = context;
        mAffairIsFinish = isFinish;
        mAffairList = AffairLab.touch(context).queryAffairList(isFinish);
    }

    @Override
    public int getCount() {
        return mAffairList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAffairList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_for_affair,
                parent,false);
        final Affair affair = mAffairList.get(position);
        TextView affairName = (TextView) convertView.findViewById(R.id.affairName_tv);
        affairName.setText(affair.getAffairName());
        TextView createTime = (TextView) convertView.findViewById(R.id.affairCreateTime_tv);
        createTime.setText(TimeUtil.format(affair.getCreateTime()));
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.affairIsFinish_cb);
        checkBox.setChecked(affair.isFinish());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBox.setClickable(false);
                affair.setFinish(isChecked);
                AffairLab.touch(mContext).updateAffair(affair);
                mOnAffairListChangeListener.onAffairListChanged();
            }
        });

        return convertView;

    }


    public List<Affair> getAffairList() {
        return mAffairList;
    }

    public void setAffairList(List<Affair> affairList) {
        mAffairList = affairList;
    }

    public void setOnAffairListChangeListener(OnAffairListChangeListener listener) {
        mOnAffairListChangeListener = listener;
    }

    public interface OnAffairListChangeListener {
        void onAffairListChanged();
    }
}
