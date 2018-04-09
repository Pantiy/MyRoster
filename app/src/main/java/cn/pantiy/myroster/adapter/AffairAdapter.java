package cn.pantiy.myroster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import cn.pantiy.myroster.R;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.AffairLab;
import cn.pantiy.myroster.utils.TimeUtil;
import cn.refactor.library.SmoothCheckBox;

/**
 * MyRoster
 * cn.pantiy.myroster.adapter
 * Created by pantiy on 17-6-8.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairAdapter extends BaseAdapter {

    private List<Affair> mAffairList;

    private Context mContext;

    private OnAffairListChangeListener mOnAffairListChangeListener;

    public AffairAdapter(Context context) {
        mContext = context;
        mAffairList = AffairLab.touch(context).queryAffairList();
    }

    public AffairAdapter(Context context, boolean isFinish) {
        mContext = context;
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
        TextView affairName = convertView.findViewById(R.id.affairName_tv);
        affairName.setText(affair.getAffairName());
        TextView createTime = convertView.findViewById(R.id.affairCreateTime_tv);
        createTime.setText(TimeUtil.format(affair.getCreateTime()));
        final SmoothCheckBox smoothCheckBox = convertView.findViewById(R.id.affairIsFinish_checkBox);
        smoothCheckBox.setChecked(affair.isFinish());
        smoothCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                affair.setFinish(isChecked);
                AffairLab.touch(mContext).updateAffair(affair);
                mOnAffairListChangeListener.onAffairListChanged();
            }
        });

        return convertView;

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
