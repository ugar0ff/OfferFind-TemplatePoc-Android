package com.dddev.market.place.ui.fragment.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.inputmethod.InputMethodManager;

import com.dddev.market.place.R;
import com.dddev.market.place.core.service.UpdateService;
import com.dddev.market.place.ui.controller.SwitchFragmentListener;

/**
 * Created by ugar on 09.02.16.
 */
public class BaseFragment extends Fragment {

    protected SwitchFragmentListener switchFragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SwitchFragmentListener) {
            switchFragmentListener = (SwitchFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (switchFragmentListener != null) {
            switchFragmentListener = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyboard();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }

    public void hideKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

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

    protected void startUpdateService() {
        if (getActivity() != null) {
            getActivity().startService(new Intent(getActivity(), UpdateService.class));
        }
    }
}