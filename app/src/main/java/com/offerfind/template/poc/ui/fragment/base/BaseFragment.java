package com.offerfind.template.poc.ui.fragment.base;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.offerfind.template.poc.R;

/**
 * Created by ugar on 09.02.16.
 */
public class BaseFragment extends Fragment {

    protected void showDialog(String message) {
        if (!isAdded()) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(message)
                        .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).create().show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
