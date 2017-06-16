package cn.pantiy.myroster.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.pantiy.myroster.fragment.AffairDetailFragment;
import cn.pantiy.myroster.model.Affair;

/**
 * MyRoster
 * cn.pantiy.myroster.adapter
 * Created by pantiy on 17-6-14.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairDetailFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    private List<Affair> mAffairList;

    public AffairDetailFragmentPagerAdapter(Context context, FragmentManager fragmentManager,
                                            List<Affair> affairList) {
        super(fragmentManager);
        mContext = context;
        mAffairList = affairList;
    }

    @Override
    public Fragment getItem(int i) {
        return AffairDetailFragment.newInstance(mAffairList.get(i).getId());
    }

    @Override
    public int getCount() {
        return mAffairList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
