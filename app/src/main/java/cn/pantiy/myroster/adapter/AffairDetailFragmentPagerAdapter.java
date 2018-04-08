package cn.pantiy.myroster.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.pantiy.myroster.fragment.AffairDetailFragment;
import cn.pantiy.myroster.fragment.CompleteAffairDetailFragment;
import cn.pantiy.myroster.fragment.IncompleteAffairDetailFragment;
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
    private boolean mIsFinish;

    public AffairDetailFragmentPagerAdapter(Context context, FragmentManager fragmentManager,
                                            List<Affair> affairList, boolean isFinish) {
        super(fragmentManager);
        mContext = context;
        mAffairList = affairList;
        mIsFinish = isFinish;
    }

    @Override
    public Fragment getItem(int i) {
        if (mAffairList.get(i).isFinish()) {
            return CompleteAffairDetailFragment.newInstance(mAffairList.get(i).getId());
        } else {
            return IncompleteAffairDetailFragment.newInstance(mAffairList.get(i).getId());
        }
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
