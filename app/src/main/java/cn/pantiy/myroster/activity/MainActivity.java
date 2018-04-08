package cn.pantiy.myroster.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import cn.pantiy.myroster.R;
import cn.pantiy.myroster.adapter.AffairFragmentPagerAdapter;
import cn.pantiy.myroster.fragment.AffairFragment;
import cn.pantiy.myroster.fragment.BaseFragment;
import cn.pantiy.myroster.fragment.CompleteAffairFragment;
import cn.pantiy.myroster.fragment.IncompleteAffairFragment;

/**
 * MyRoster
 * cn.pantiy.myroster
 * Created by pantiy on 17-6-4.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class MainActivity extends BaseActivity implements AffairFragment.OnCreateAffairCallback{

    private static final String TAG = "MainActivity";

    public static final int INCOMPLETE = 0;
    public static final int FINISHED = 1;

    private List<BaseFragment> mFragmentList;
    private int mCurrent = 0;

    private BottomNavigationView mBottomNavigationView;
    private ViewPager mFragmentViewPager;


    @Override
    protected void initData() {
        mFragmentList = new ArrayList<>();
        IncompleteAffairFragment incompleteAffairFragment = IncompleteAffairFragment.newInstance();
        mFragmentList.add(INCOMPLETE, incompleteAffairFragment);
        CompleteAffairFragment completeAffairFragment = CompleteAffairFragment.newInstance();
        mFragmentList.add(FINISHED, completeAffairFragment);
    }

    @Override
    protected void initViews() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mFragmentViewPager = (ViewPager) findViewById(R.id.fragment_viewPager);
    }

    @Override
    protected void setupAdapter() {
        AffairFragmentPagerAdapter adapter = new AffairFragmentPagerAdapter(this,
                getSupportFragmentManager(), mFragmentList);
        mFragmentViewPager.setAdapter(adapter);
    }

    @Override
    protected void setupListener() {

        mFragmentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                switchCurrentFragment(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.incomplete:
                        switchCurrentFragment(INCOMPLETE);
                        return true;
                    case R.id.finished:
                        switchCurrentFragment(FINISHED);
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void switchCurrentFragment(int current) {
        mCurrent = current;
        switch (current) {
            case INCOMPLETE:
                mBottomNavigationView.getMenu().getItem(INCOMPLETE).setChecked(true);
                break;
            case FINISHED:
                mBottomNavigationView.getMenu().getItem(FINISHED).setChecked(true);
                break;
            default:
                break;
        }
        mFragmentViewPager.setCurrentItem(current);
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreateAffair() {
        switchCurrentFragment(INCOMPLETE);
    }
}
