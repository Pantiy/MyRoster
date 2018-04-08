package cn.pantiy.myroster.fragment;

import java.util.UUID;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.AffairLab;

/**
 * Created by Pantiy on 2018/4/8.
 * Copyright © 2016 All rights Reserved by Pantiy
 */
public abstract class AffairDetailFragment extends BaseFragment{

    protected static final String KEY_AFFAIR_ID = "affairId";

    protected Affair mAffair;

    protected IncompleteNumChangedCallback mCallback;

//    protected static AffairDetailFragment newInstance(UUID affairId, boolean isCompleted) {
//        AffairDetailFragment affairDetailFragment;
//        if (isCompleted) {
//            affairDetailFragment = new CompleteAffairDetailFragment();
//        } else {
//            affairDetailFragment = new IncompleteAffairDetailFragment();
//        }
//        Bundle args = new Bundle();
//        args.putSerializable(KEY_AFFAIR_ID, affairId);
//        affairDetailFragment.setArguments(args);
//        return affairDetailFragment;
//    }

    @Override
    protected void initData() {
        UUID affairId = (UUID) getArguments().getSerializable(KEY_AFFAIR_ID);
        mAffair = AffairLab.touch(mContext).queryAffair(affairId);
        mCallback = (IncompleteNumChangedCallback) getActivity();
    }

    public interface IncompleteNumChangedCallback {
        void onIncompleteNumChanged();
    }
}
