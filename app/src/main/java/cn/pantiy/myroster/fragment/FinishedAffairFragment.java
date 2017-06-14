package cn.pantiy.myroster.fragment;

/**
 * MyRoster
 * cn.pantiy.myroster.fragment
 * Created by pantiy on 17-6-4.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class FinishedAffairFragment extends AffairFragment {

    private static FinishedAffairFragment sFinishedAffairFragment;

    public static FinishedAffairFragment newInstance() {
        if (sFinishedAffairFragment == null) {
            sFinishedAffairFragment = new FinishedAffairFragment();
        }
        return sFinishedAffairFragment;
    }

    @Override
    protected boolean setFinish() {
        return true;
    }
}