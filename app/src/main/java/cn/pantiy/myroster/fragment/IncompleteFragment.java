package cn.pantiy.myroster.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.pantiy.myroster.MyApplication;
import cn.pantiy.myroster.R;
import cn.pantiy.myroster.adapter.AffairAdapter;
import cn.pantiy.myroster.adapter.AffairDetailAdapter;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.AffairLab;
import cn.pantiy.myroster.model.ClassmateInfo;
import cn.pantiy.myroster.model.ClassmateInfoLab;
import cn.pantiy.myroster.utils.ExcelUtil;
import cn.pantiy.myroster.utils.SharedPreferencesUtil;
import jxl.read.biff.BiffException;

/**
 * MyRoster
 * cn.pantiy.myroster.fragment
 * Created by pantiy on 17-6-4.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class IncompleteFragment extends BaseFragment {

    private static final String TAG = "IncompleteFragment";
    private static final String CREATE_AFFAIR_NAME_DIALOG = "createAffairDialogFragment";

    private static final int REQUEST_AFFAIR_NAME = -1;
    private static final int REQUEST_CHOOSE_EXCEL = 0;

    private static final String ROSTER_IMPORT_KEY = "rosterImport";
    private static final String HAVE_INCOMPLETE_KEY = "emptyIncompleteList";

    private static boolean sRosterImport;
    private static boolean sHaveIncomplete;

    private TextView mEmptyTv;
    private ListView mIncompleteLv;

    private AffairAdapter mAffairAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.more_option, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add_affair:
                if (!sRosterImport) {
                    importRoster();
                } else {
                    add();
                }
                return true;

            case R.id.re_import_roster:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void initData() {
        sRosterImport = SharedPreferencesUtil.getBoolean(ROSTER_IMPORT_KEY);
        sHaveIncomplete = SharedPreferencesUtil.getBoolean(HAVE_INCOMPLETE_KEY);
    }

    @Override
    protected void initViews(View view) {

        mEmptyTv = (TextView) view.findViewById(R.id.empty_tv);
        mIncompleteLv = (ListView) view.findViewById(R.id.incomplete_lv);

        if (!sHaveIncomplete) {
            mEmptyTv.setVisibility(View.VISIBLE);
        } else {
            mIncompleteLv.setVisibility(View.VISIBLE);
            Affair affair = AffairLab.touch(mContext).getAffairList().get(0);
            AffairDetailAdapter adapter = new AffairDetailAdapter(mContext, affair);
            mIncompleteLv.setAdapter(adapter);
        }
    }

    @Override
    protected void setupAdapter() {
        mAffairAdapter = new AffairAdapter(mContext);
        mIncompleteLv.setAdapter(mAffairAdapter);
    }

    @Override
    protected void setupListener() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult()" + " " + resultCode);
        switch (requestCode) {
            case REQUEST_CHOOSE_EXCEL:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    Log.i(TAG, uri.getPath());
                    ImportExcelAsyncTask excelAsyncTask = new ImportExcelAsyncTask();
                    excelAsyncTask.execute(uri.getPath());
                } else {
                    Log.i(TAG, "resultCode " + resultCode);
                    Toast.makeText(mContext, "导入失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_AFFAIR_NAME:
                if (resultCode == Activity.RESULT_OK) {
                    addAffair(data.getStringExtra(CreateAffairDialogFragment.EXTRA_AFFAIR_NAME));
                    refresh();
                }
                break;
        }
    }

    private void add() {
        FragmentManager fragmentManager = getFragmentManager();
        CreateAffairDialogFragment createAffairDialogFragment =
                new CreateAffairDialogFragment();
        createAffairDialogFragment.setTargetFragment(this, REQUEST_AFFAIR_NAME);
        createAffairDialogFragment.show(fragmentManager, CREATE_AFFAIR_NAME_DIALOG);
    }

    private void refresh() {
        mAffairAdapter.setAffairList(AffairLab.touch(mContext).getAffairList());
        mAffairAdapter.notifyDataSetChanged();
        checkViews();
    }

    private void addAffair(String affairName) {
        Affair affair = new Affair(affairName,
                ClassmateInfoLab.touch(mContext).getClassmateInfoList());
        AffairLab.touch(mContext).addAffair(affair);
    }

    private void importRoster() {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.title_no_roster)
                .setMessage(R.string.message_import_roster)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chooseExcelFile();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
    }

    private void checkViews() {
        if (mAffairAdapter.getAffairList() == null && sHaveIncomplete) {
            sHaveIncomplete = SharedPreferencesUtil.putBoolean(HAVE_INCOMPLETE_KEY, false);
            setVisibility();
        } else if (mAffairAdapter.getAffairList() != null && !sHaveIncomplete){
            sHaveIncomplete = SharedPreferencesUtil.putBoolean(HAVE_INCOMPLETE_KEY, true);
            setVisibility();
        }
    }

    private void setVisibility() {
        if (sHaveIncomplete) {
            mEmptyTv.setVisibility(View.GONE);
            mIncompleteLv.setVisibility(View.VISIBLE);
        } else {
            mEmptyTv.setVisibility(View.VISIBLE);
            mIncompleteLv.setVisibility(View.GONE);
        }
    }

    private void test() {
        Affair affair = new Affair("test",
                ClassmateInfoLab.touch(mContext).getClassmateInfoList());
        AffairLab.touch(mContext).addAffair(affair);
        AffairDetailAdapter adapter = new AffairDetailAdapter(mContext,
                affair);
        mIncompleteLv.setAdapter(adapter);
        mEmptyTv.setVisibility(View.GONE);
        mIncompleteLv.setVisibility(View.VISIBLE);
        sHaveIncomplete = SharedPreferencesUtil.putBoolean(HAVE_INCOMPLETE_KEY, true);
    }

    private void chooseExcelFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(intent, REQUEST_CHOOSE_EXCEL);
        } catch (android.content.ActivityNotFoundException ane) {
            Toast.makeText(mContext, "请安装文件管理类App", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_incomplete;
    }

    private static class ImportExcelAsyncTask extends AsyncTask<String, Void, List<ClassmateInfo>> {

        private ClassmateInfoLab mClassmateInfoLab = ClassmateInfoLab.touch(MyApplication.getContext());

        @Override
        protected List<ClassmateInfo> doInBackground(String... params){
            Log.i(TAG, "doInBackground()");
            String path = params[0];
            File file = new File(path);

            try {
                List<String[]> excelContent = ExcelUtil.readExcel(file);
                mClassmateInfoLab.setClassmateInfoList(excelContent);
                return mClassmateInfoLab.getClassmateInfoList();
            } catch (IOException ioe) {
                Log.e(TAG, "error", ioe);
            } catch (BiffException be) {
                Log.e(TAG, "error", be);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ClassmateInfo> strings) {
            super.onPostExecute(strings);
            if (strings != null) {
                Toast.makeText(MyApplication.getContext(), "已导入", Toast.LENGTH_SHORT).show();
                sRosterImport = SharedPreferencesUtil.putBoolean(ROSTER_IMPORT_KEY, true);
                Log.i(TAG, "roster import: " + SharedPreferencesUtil.getBoolean(ROSTER_IMPORT_KEY));
            }
        }
    }
}