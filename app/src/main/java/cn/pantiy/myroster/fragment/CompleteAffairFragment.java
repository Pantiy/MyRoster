package cn.pantiy.myroster.fragment;

/**
 * MyRoster
 * cn.pantiy.myroster.fragment
 * Created by pantiy on 17-6-4.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class CompleteAffairFragment extends AffairFragment {

    private static CompleteAffairFragment sCompleteAffairFragment;

    public static CompleteAffairFragment newInstance() {
        if (sCompleteAffairFragment == null) {
            sCompleteAffairFragment = new CompleteAffairFragment();
        }
        return sCompleteAffairFragment;
    }

    @Override
    protected boolean setFinish() {
        return true;
    }
}