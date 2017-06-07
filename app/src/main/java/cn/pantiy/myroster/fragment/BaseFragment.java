package cn.pantiy.myroster.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * MyRoster
 * cn.pantiy.myroster.fragment
 * Created by pantiy on 17-6-4.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    protected View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContext = getActivity();
        mView = inflater.inflate(setLayoutRes(), container, false);
        init();
        return mView;
    }

    protected void init() {
        initData();
        initViews(mView);
        setupAdapter();
        setupListener();
    }

    protected abstract void initData();

    protected abstract void initViews(View view);

    protected abstract void setupAdapter();

    protected abstract void setupListener();

    protected abstract int setLayoutRes();
}
