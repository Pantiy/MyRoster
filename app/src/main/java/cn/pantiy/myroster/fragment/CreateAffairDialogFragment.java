package cn.pantiy.myroster.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import cn.pantiy.myroster.R;

/**
 * MyRoster
 * cn.pantiy.myroster.fragment
 * Created by pantiy on 17-6-8.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class CreateAffairDialogFragment extends DialogFragment {

    public static final String EXTRA_AFFAIR_NAME = "affairName";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_create_affair,
                null, false);

        final EditText editText = (EditText) view.findViewById(R.id.inputAffairName_et);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK, editText.getText().toString());
                    }})
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private void sendResult(int resultCode, String affairName) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_AFFAIR_NAME, affairName);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, data);
    }
}
