package cn.pantiy.myroster.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import java.util.List;
import java.util.UUID;

import cn.pantiy.myroster.R;
import cn.pantiy.myroster.adapter.AffairDetailFragmentPagerAdapter;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.AffairLab;

/**
 * MyRoster
 * cn.pantiy.myroster.activity
 * Created by pantiy on 17-6-14.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairDetailActivity extends BaseActivity {

    private static final String TAG = "AffairDetailActivity";

    private static final String EXTRA_AFFAIR_ID = "affairId";
    private static final String EXTRA_IS_FINISH = "isFinish";

    private List<Affair> mAffairList;
    private UUID mAffairId;
    private boolean mIsFinish;

    private ViewPager mViewPager;

    public static Intent newInstance(Context context, UUID affairId, boolean isFinish) {
        Intent intent = new Intent(context, AffairDetailActivity.class);
        intent.putExtra(EXTRA_AFFAIR_ID, affairId);
        intent.putExtra(EXTRA_IS_FINISH, isFinish);
        return intent;
    }

    @Override
    protected void initData() {
        mAffairId = (UUID) getIntent().getSerializableExtra(EXTRA_AFFAIR_ID);
        mIsFinish = getIntent().getBooleanExtra(EXTRA_IS_FINISH, false);
        mAffairList = AffairLab.touch(this).getAffairList(mIsFinish);
    }

    @Override
    protected void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.fragment_viewPager);
    }

    @Override
    protected void setupAdapter() {
        AffairDetailFragmentPagerAdapter fragmentPagerAdapter =
                new AffairDetailFragmentPagerAdapter(this, getSupportFragmentManager(),
                        mAffairList);
        mViewPager.setAdapter(fragmentPagerAdapter);
        setCurrentFragment();
    }

    @Override
    protected void setupListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(mAffairList.get(position).getAffairName());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setCurrentFragment() {
        for (int i = 0; i < mAffairList.size(); i++) {
            if (mAffairList.get(i).getId().equals(mAffairId)) {
                mViewPager.setCurrentItem(i);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(mAffairList.get(i).getAffairName());
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
                break;
            }
        }
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_affair_detail;
    }
}
