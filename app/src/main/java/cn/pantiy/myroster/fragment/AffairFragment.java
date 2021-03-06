package cn.pantiy.myroster.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import cn.pantiy.myroster.R;
import cn.pantiy.myroster.activity.AffairDetailActivity;
import cn.pantiy.myroster.activity.RosterActivity;
import cn.pantiy.myroster.adapter.AffairAdapter;
import cn.pantiy.myroster.global.MyApplication;
import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.AffairLab;
import cn.pantiy.myroster.model.ClassmateInfo;
import cn.pantiy.myroster.model.ClassmateInfoLab;
import cn.pantiy.myroster.utils.ExcelUtil;
import cn.pantiy.myroster.utils.FileUtil;
import cn.pantiy.myroster.utils.PermissionUtil;
import cn.pantiy.myroster.utils.SharedPreferencesUtil;
import jxl.read.biff.BiffException;

/**
 * MyRoster
 * cn.pantiy.myroster.fragment
 * Created by pantiy on 17-6-9.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public abstract class AffairFragment extends BaseFragment {

    private static final String TAG = "AffairFragment";
    private static final String CREATE_AFFAIR_NAME_DIALOG = "createAffairDialogFragment";

    private static final int REQUEST_AFFAIR_NAME = -1;
    private static final int REQUEST_CHOOSE_EXCEL = 0;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;

    private static final String kEY_ROSTER_IMPORT = "rosterImport";

    private static boolean sRosterImport;

    protected boolean mIsFinish;
    protected boolean mCreateAffairInFinished;

    protected TextView mEmptyTv;
    protected ListView mAffairLv;
    protected AffairAdapter mAffairAdapter;

    private OnCreateAffairCallback mCallback;

    protected abstract boolean setFinish();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAffairList();
        if (mCreateAffairInFinished) {
            mCallback.onCreateAffair();
            mCreateAffairInFinished = false;
        }
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
                    createDialog();
                }
                return true;

            case R.id.display_roster:
                Intent intent = new Intent(mContext, RosterActivity.class);
                startActivity(intent);
                return true;

            case R.id.re_import_roster:
                reImportRoster();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult()" + " " + resultCode);
        switch (requestCode) {
            case REQUEST_CHOOSE_EXCEL:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    Log.i(TAG, "uri: " + uri.toString());
                    ImportExcelAsyncTask excelAsyncTask = new ImportExcelAsyncTask();
                    excelAsyncTask.execute(uri);
                } else {
                    Log.i(TAG, "resultCode " + resultCode);
                    Toast.makeText(mContext, R.string.import_failed, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_AFFAIR_NAME:
                if (resultCode == Activity.RESULT_OK) {
                    createAffair(data.getStringExtra(CreateAffairDialogFragment.EXTRA_AFFAIR_NAME));
                }
                break;
        }
    }

    @Override
    protected void initData() {
        sRosterImport = SharedPreferencesUtil.getBoolean(kEY_ROSTER_IMPORT);
        mCreateAffairInFinished = false;
        mCallback = (OnCreateAffairCallback) getActivity();
        mIsFinish = setFinish();
    }

    @Override
    protected void initViews(View view) {
        mEmptyTv = (TextView) view.findViewById(R.id.empty_tv);
        mAffairLv = (ListView) view.findViewById(R.id.affair_lv);
        checkEmptyAffairList();
    }

    @Override
    protected void setupAdapter() {
        mAffairAdapter = new AffairAdapter(mContext, mIsFinish);
        mAffairLv.setAdapter(mAffairAdapter);
    }

    @Override
    protected void setupListener() {
        mAffairAdapter.setOnAffairListChangeListener(new AffairAdapter.OnAffairListChangeListener() {
            @Override
            public void onAffairListChanged() {
                CompleteAffairFragment.newInstance().updateAffairList();
                IncompleteAffairFragment.newInstance().updateAffairList();
            }
        });
        mAffairLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick()");
                Affair affair = (Affair) mAffairAdapter.getItem(position);
                skipToAffairDetail(mIsFinish, affair.getId());
            }
        });
        mAffairLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = position;
                new AlertDialog.Builder(mContext)
                        .setMessage(R.string.message_delete_affair)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AffairLab.touch(mContext).deleteAffair((Affair)mAffairAdapter.getItem(index));
                                updateAffairList();
                            }})
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return true;
            }
        });
    }

    private void skipToAffairDetail(boolean isFinish, UUID affairId) {
        Intent intent = AffairDetailActivity.newInstance(mContext, affairId, isFinish);
        startActivity(intent);
    }

    protected void updateAffairList() {
        mAffairAdapter.setAffairList(getAffairList());
        mAffairAdapter.notifyDataSetChanged();
        checkEmptyAffairList();
    }

    private void checkEmptyAffairList() {
        if (getAffairList().size() != 0) {
            mEmptyTv.setVisibility(View.GONE);
            mAffairLv.setVisibility(View.VISIBLE);
        } else {
            mEmptyTv.setVisibility(View.VISIBLE);
            mAffairLv.setVisibility(View.GONE);
        }
    }

    private List<Affair> getAffairList() {
        return AffairLab.touch(mContext).queryAffairList(mIsFinish);
    }

    private void createDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        CreateAffairDialogFragment createAffairDialogFragment =
                new CreateAffairDialogFragment();
        createAffairDialogFragment.setTargetFragment(this, REQUEST_AFFAIR_NAME);
        createAffairDialogFragment.show(fragmentManager, CREATE_AFFAIR_NAME_DIALOG);
    }

    private void createAffair(String affairName) {
        Affair affair = new Affair(affairName);
        AffairLab.touch(mContext).insertAffair(affair);
        Log.i(TAG, "UUID:" + affair.getId());
        if (mIsFinish) {
            mCreateAffairInFinished = true;
        }
        skipToAffairDetail(mIsFinish, affair.getId());
    }

    private void importRoster() {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
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

    private void reImportRoster() {

        new AlertDialog.Builder(mContext)
                .setMessage(R.string.message_re_import_roster)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        AffairLab.touch(mContext).deleteForm();
                        ClassmateInfoLab.touch(mContext).deleteForm();
//                        updateAffairList();
                        sRosterImport = SharedPreferencesUtil.putBoolean(kEY_ROSTER_IMPORT, false);
                        chooseExcelFile();
                    }})
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void chooseExcelFile() {
        if (!PermissionUtil.checkPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionUtil.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(intent, REQUEST_CHOOSE_EXCEL);
        } catch (android.content.ActivityNotFoundException ane) {
            Toast.makeText(mContext, R.string.install_file_manager, Toast.LENGTH_SHORT).show();
        }
    }

//    private boolean checkPermission() {
//        return (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED);
//    }
//
//    private void requestPermission() {
//        Log.i(TAG, "requestPermission()");
//        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
//                REQUEST_READ_EXTERNAL_STORAGE);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseExcelFile();
                }
                break;
        }

    }

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_affair;
    }

    public interface OnCreateAffairCallback {
        void onCreateAffair();
    }

    private static class ImportExcelAsyncTask extends AsyncTask<Uri, Void, List<ClassmateInfo>> {

        private ClassmateInfoLab mClassmateInfoLab = ClassmateInfoLab.touch(MyApplication.getContext());

        @Override
        protected List<ClassmateInfo> doInBackground(Uri... params){
            Log.i(TAG, "doInBackground()");
            Uri uri = params[0];
            String path = FileUtil.getPath(MyApplication.getContext(), uri);
            Log.i(TAG, "filePath: " + path);
            File file;
            if (path != null) {
                file = new File(path);
            } else {
                return null;
            }

            try {
                List<String[]> excelContent = ExcelUtil.readExcel(file);
                if (excelContent != null) {
                    mClassmateInfoLab.setClassmateInfoList(excelContent);
                    return mClassmateInfoLab.queryClassmateInfoList();
                }
            } catch (IOException ioe) {
                Log.e(TAG, "error", ioe);
                Toast.makeText(MyApplication.getContext(), R.string.import_failed_check_permission,
                        Toast.LENGTH_SHORT).show();
            } catch (BiffException be) {
                Log.e(TAG, "error", be);
                Toast.makeText(MyApplication.getContext(), R.string.import_failed_check_permission,
                        Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ClassmateInfo> strings) {
            super.onPostExecute(strings);
            if (strings != null) {
                Toast.makeText(MyApplication.getContext(), R.string.import_success,
                        Toast.LENGTH_SHORT).show();
                sRosterImport = SharedPreferencesUtil.putBoolean(kEY_ROSTER_IMPORT, true);
                Log.i(TAG, "roster import: " + SharedPreferencesUtil.getBoolean(kEY_ROSTER_IMPORT));
            } else {
                Toast.makeText(MyApplication.getContext(), R.string.import_failed,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
