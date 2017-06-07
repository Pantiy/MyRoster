package cn.pantiy.myroster.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.List;
import cn.pantiy.myroster.MyApplication;
import cn.pantiy.myroster.R;
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

    private static final int REQUEST_CHOOSE_EXCEL = 0;

    private static final String ROSTER_IMPORT_KEY = "rosterImport";
    private static final String EMPTY_INCOMPLETE_KEY = "emptyIncompleteList";

    private static boolean sRosterHaveImport;
    private static boolean sHaveIncompleteList;

    private ImageButton mAddImgBtn;
    private ListView mIncompleteLv;

    @Override
    public void onPause() {
        super.onPause();
        ((AffairDetailAdapter)mIncompleteLv.getAdapter()).updateAffair();
    }

    @Override
    protected void initData() {
        sRosterHaveImport = SharedPreferencesUtil.getBoolean(ROSTER_IMPORT_KEY);
        sHaveIncompleteList = SharedPreferencesUtil.getBoolean(EMPTY_INCOMPLETE_KEY);
    }

    @Override
    protected void initViews(View view) {

        mAddImgBtn = (ImageButton) view.findViewById(R.id.add_imgBtn);
        mIncompleteLv = (ListView) view.findViewById(R.id.incomplete_lv);

        if (!sHaveIncompleteList) {
            mAddImgBtn.setVisibility(View.VISIBLE);
        } else {
            mIncompleteLv.setVisibility(View.VISIBLE);
            Affair affair = AffairLab.touch(mContext).getAffairList().get(0);
            AffairDetailAdapter adapter = new AffairDetailAdapter(mContext, affair);
            mIncompleteLv.setAdapter(adapter);
        }
    }

    @Override
    protected void setupAdapter() {
    }

    @Override
    protected void setupListener() {

        mAddImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sRosterHaveImport) {
                    showImportRosterDialog();
                } else if (!sHaveIncompleteList) {
                    test();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult()" + " " + resultCode);
        switch (requestCode) {
            case REQUEST_CHOOSE_EXCEL:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    Uri uri = data.getData();
                    Log.i(TAG, uri.getPath());
                    ImportExcelAsyncTask excelAsyncTask = new ImportExcelAsyncTask();
                    excelAsyncTask.execute(uri.getPath());
                } else {
                    Log.i(TAG, "resultCode " + resultCode);
                    Toast.makeText(mContext, "导入失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showImportRosterDialog() {
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

    private void test() {
        Affair affair = new Affair("test",
                ClassmateInfoLab.touch(mContext).getClassmateInfoList());
        AffairLab.touch(mContext).addAffair(affair);
        AffairDetailAdapter adapter = new AffairDetailAdapter(mContext,
                affair);
        mIncompleteLv.setAdapter(adapter);
        mAddImgBtn.setVisibility(View.GONE);
        mIncompleteLv.setVisibility(View.VISIBLE);
        sHaveIncompleteList = SharedPreferencesUtil.putBoolean(EMPTY_INCOMPLETE_KEY, true);
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
                sRosterHaveImport = SharedPreferencesUtil.putBoolean(ROSTER_IMPORT_KEY, true);
                Log.i(TAG, "roster import: " + SharedPreferencesUtil.getBoolean(ROSTER_IMPORT_KEY));
            }
        }
    }
}