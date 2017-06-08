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

/**
 * MyRoster
 * cn.pantiy.myroster.adapter
 * Created by pantiy on 17-6-8.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairAdapter extends BaseAdapter {

    private List<Affair> mAffairList;

    private Context mContext;

    public AffairAdapter(Context context) {
        mContext = context;
        mAffairList = AffairLab.touch(context).getAffairList();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_for_affair,
                    parent,false);
        }
        TextView affairName = (TextView) convertView.findViewById(R.id.affairName_tv);
        affairName.setText(mAffairList.get(position).getAffairName());
        return convertView;
    }

    public List<Affair> getAffairList() {
        return mAffairList;
    }

    public void setAffairList(List<Affair> affairList) {
        mAffairList = affairList;
    }
}
