package co.mrktplaces.clients.android.ui.fragment.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.inputmethod.InputMethodManager;

import co.mrktplaces.clients.android.R;
import co.mrktplaces.clients.android.core.service.AcceptBidsService;
import co.mrktplaces.clients.android.core.service.CompleteBidsService;
import co.mrktplaces.clients.android.core.service.UpdateService;
import co.mrktplaces.clients.android.ui.controller.MessageCountController;
import co.mrktplaces.clients.android.ui.controller.SwitchFragmentListener;
import co.mrktplaces.clients.android.ui.controller.ToolbarController;
import co.mrktplaces.clients.android.utils.StaticKeys;

/**
 * Created by ugar on 09.02.16.
 */
public abstract class BaseFragment extends Fragment {

    protected SwitchFragmentListener switchFragmentListener;
    protected ToolbarController toolbarController;
    protected MessageCountController messageCountController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessageCountController) {
            messageCountController = (MessageCountController) context;
        }
        if (context instanceof SwitchFragmentListener) {
            switchFragmentListener = (SwitchFragmentListener) context;
        }
        if (context instanceof ToolbarController) {
            toolbarController = (ToolbarController) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (messageCountController != null) {
            messageCountController = null;
        }
        if (switchFragmentListener != null) {
            switchFragmentListener = null;
        }
        if (toolbarController != null) {
            toolbarController = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyboard();
    }

    @Override
    public void onPause() {
        hideKeyboard();
        super.onPause();
    }

    public void hideKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void showDialog(String message) {
        if (isAdded()) {
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
            getActivity().startService(new Intent(getActivity(), UpdateService.class).putExtra(StaticKeys.KEY_REQUEST, StaticKeys.REQUEST_START));
        }
    }

    protected void startAcceptBidsService(int bidId) {
        if (getActivity() != null) {
            getActivity().startService(new Intent(getActivity(), AcceptBidsService.class).putExtra(StaticKeys.ACCEPT_BIDS_ID, bidId));
        }
    }

    protected void startCompleteBidsService(int bidId) {
        if (getActivity() != null) {
            getActivity().startService(new Intent(getActivity(), CompleteBidsService.class).putExtra(StaticKeys.COMPLETE_BIDS_ID, bidId));
        }
    }
}
