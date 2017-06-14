package cn.pantiy.myroster.fragment;

/**
 * MyRoster
 * cn.pantiy.myroster.fragment
 * Created by pantiy on 17-6-4.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class IncompleteAffairFragment extends AffairFragment {

    private static IncompleteAffairFragment sIncompleteAffairFragment;

    public static IncompleteAffairFragment newInstance() {
        if (sIncompleteAffairFragment == null) {
            sIncompleteAffairFragment = new IncompleteAffairFragment();
        }
        return sIncompleteAffairFragment;
    }

    @Override
    protected boolean setFinish() {
        return false;
    }
}