package cn.pantiy.myroster.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import cn.pantiy.myroster.R;
import cn.pantiy.myroster.adapter.AffairDetailFragmentPagerAdapter;
import cn.pantiy.myroster.fragment.AffairDetailFragment;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.AffairLab;
import cn.pantiy.myroster.model.ClassmateInfoLab;
import cn.pantiy.myroster.utils.ExcelUtil;
import cn.pantiy.myroster.utils.PermissionUtil;
import jxl.write.WriteException;

/**
 * MyRoster
 * cn.pantiy.myroster.activity
 * Created by pantiy on 17-6-14.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class AffairDetailActivity extends BaseActivity implements AffairDetailFragment.OnAffairContentChangedCallback{

    private static final String TAG = "AffairDetailActivity";
    private static final int REQUEST_WRITE_PERMISSION_CODE = 1;

    private static final String EXTRA_AFFAIR_ID = "affairId";
    private static final String EXTRA_IS_FINISH = "isFinish";

    private boolean mSubtitleVisible;
    private boolean mIsFinish;


    private AffairLab mAffairLab;
    private List<Affair> mAffairList;
    private UUID mAffairId;

    private ActionBar mActionBar;
    private ViewPager mViewPager;
    private AffairDetailFragmentPagerAdapter mFragmentPagerAdapter;

    public static Intent newInstance(Context context, UUID affairId, boolean isFinish) {
        Intent intent = new Intent(context, AffairDetailActivity.class);
        intent.putExtra(EXTRA_AFFAIR_ID, affairId);
        intent.putExtra(EXTRA_IS_FINISH, isFinish);
        return intent;
    }

    @Override
    protected void initData() {
        mSubtitleVisible = false;
        mAffairLab = AffairLab.touch(this);
        mAffairId = (UUID) getIntent().getSerializableExtra(EXTRA_AFFAIR_ID);
        Log.i(TAG, "UUID" + mAffairId.toString());
        mIsFinish = getIntent().getBooleanExtra(EXTRA_IS_FINISH, false);
        Affair affair = mAffairLab.queryAffair(mAffairId);
        if (affair.isFinish() != mIsFinish) {
            mIsFinish = !mIsFinish;
        }
        mAffairList = AffairLab.touch(this).queryAffairList(mIsFinish);
    }

    @Override
    protected void initViews() {
        mActionBar = getSupportActionBar();
        mViewPager = (ViewPager) findViewById(R.id.fragment_viewPager);
    }

    @Override
    protected void setupAdapter() {
        mFragmentPagerAdapter = new AffairDetailFragmentPagerAdapter(this, getSupportFragmentManager(),
                mAffairList, mIsFinish);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        setCurrentFragment(mAffairId);
    }

    @Override
    protected void setupListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mAffairId = mAffairList.get(position).getId();
                if (mActionBar != null) {
                    mActionBar.setTitle(mAffairList.get(position).getAffairName());
                    updateSubtitle();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.more_option_in_affair_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.displaySubtitle:
                if (!mSubtitleVisible) {
                    displaySubtitle();
                    item.setTitle(R.string.hide_subtitle_count);
                    mSubtitleVisible = true;
                } else {
                    hideSubtitle();
                    item.setTitle(R.string.display_subtitle_count);
                    mSubtitleVisible = false;
                }
                return true;
            case R.id.deleteAffair:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.message_delete_affair)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAffair(mViewPager.getCurrentItem());
                            }})
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return true;
            case R.id.exportAffair:
                if (!PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    PermissionUtil.requestPermission(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_PERMISSION_CODE);
                } else {
                    exportExcel();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void deleteAffair(int current) {
        Log.i(TAG, "deleteCurrent: " + current);
        Affair affair = mAffairList.get(current);
        if (mAffairList.size() == 1) {
            deleteAffair(affair);
            finish();
        } else {
            removeCurrentFragment(current);
            deleteAffair(affair);
        }
    }

    private void deleteAffair(Affair affair) {
        Log.i(TAG, "delete: " + affair.getAffairName());
        mAffairLab.deleteAffair(affair);
    }

    private void removeCurrentFragment(int current) {

        mAffairList.remove(current);
        Log.i(TAG, "affairListSize: " + mAffairList.size());
        mFragmentPagerAdapter.notifyDataSetChanged();

        updateCurrentAffairId(current);

        setCurrentFragment(mAffairId);
    }

    private void updateCurrentAffairId(int current) {

        if (current < mAffairList.size() - 1) {
            mAffairId = mAffairList.get(current + 1).getId();
        } else if (mAffairList.size() == 1) {
            mAffairId = mAffairList.get(0).getId();
        } else {
            mAffairId = mAffairList.get(current - 1).getId();
        }
    }

    private void displaySubtitle() {
        int count = ClassmateInfoLab.touch(this).queryClassmateInfoList().size();
        int incompleteCount = 0;
        boolean[] stateArray = mAffairLab.queryAffair(mAffairId).getStateArray();
        for (boolean state : stateArray) {
            if (!state) {
                incompleteCount++;
            }
        }
        String subtitle = getString(R.string.subtitle_count, count, incompleteCount);
        Log.i(TAG, "subtitle: " + subtitle);
        if (mActionBar != null) {
            mActionBar.setSubtitle(subtitle);
        }
    }

    private void hideSubtitle() {
        if (mActionBar != null) {
            mActionBar.setSubtitle(null);
        }
    }

    private void updateSubtitle() {
        if (mSubtitleVisible) {
            displaySubtitle();
        }
    }


    private void setCurrentFragment(UUID affairId) {
        for (int i = 0; i < mAffairList.size(); i++) {
            if (mAffairList.get(i).getId().equals(affairId)) {
                mViewPager.setCurrentItem(i);
                if (mActionBar != null) {
                    mActionBar.setTitle(mAffairList.get(i).getAffairName());
                    mActionBar.setDisplayHomeAsUpEnabled(true);
                }
                break;
            }
        }
    }

    private void exportExcel() {

        final Affair affair = AffairLab.touch(this).queryAffair(mAffairId);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    ExcelUtil.writeExcel(affair);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AffairDetailActivity.this,
                                "已导出", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }

//    private boolean checkPermission() {
//        return (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED);
//    }
//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(this,
//                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                REQUEST_WRITE_PERMISSION_CODE);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    exportExcel();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_affair_detail;
    }

    @Override
    public void onAffairContentChanged() {
        updateSubtitle();
    }
}
