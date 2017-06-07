package cn.pantiy.myroster.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.pantiy.myroster.fragment.BaseFragment;

/**
 * MyRoster
 * cn.pantiy.myroster.adapter
 * Created by pantiy on 17-6-4.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> mFragmentList;

    private Context mContext;

    public MainFragmentPagerAdapter(Context context, FragmentManager fragmentManager,
                                    List<BaseFragment> fragmentList) {
        super(fragmentManager);
        mContext = context;
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
