package com.dddev.market.place.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.dddev.market.place.ui.views.DelayAutoCompleteTextView;
import com.dddev.market.place.ui.views.eventsource_android.MessageEvent;
import com.dddev.market.place.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by ugar on 10.02.16.
 */
public class AccountFragment extends BaseFragment implements View.OnClickListener {

    private TextView name, location, email, bankInfo;
    private ImageView avatar;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        name = (TextView) view.findViewById(R.id.name);
        location = (TextView) view.findViewById(R.id.location);
        email = (TextView) view.findViewById(R.id.email);
        bankInfo = (TextView) view.findViewById(R.id.banking_info);
        avatar = (ImageView) view.findViewById(R.id.avatar);
        view.findViewById(R.id.edit).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                if (getParentFragment() instanceof AccountRootFragment) {
                    ((AccountRootFragment) getParentFragment()).setAccountEditFragment();
                }
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        name.setText(PreferencesUtils.getUserName(getActivity()));
        if (PreferencesUtils.isLocaleCheckBoxEnable(getActivity())) {
            location.setText(getString(R.string.device_location));
        } else {
            String locationText = PreferencesUtils.getUserAddress(getActivity());
            if (locationText.length() == 0) {
                location.setText(getString(R.string.location_is_not_entered));
            } else {
                location.setText(locationText);
            }
        }
        email.setText(PreferencesUtils.getUserEmail(getActivity()));
        String bankInfoText = PreferencesUtils.getUserBankInfo(getActivity());
        if (bankInfoText == null || bankInfoText.length() == 0) {
            bankInfo.setText(getString(R.string.bank_info_is_empty));
        } else {
            bankInfo.setText(bankInfoText);
        }
        Picasso.with(getActivity()).load("http://cdn.superbwallpapers.com/wallpapers/meme/poker-face-41130-1920x1200.jpg").fit().centerCrop().into(avatar);
    }

    @Override
    public void onStreamMessage(Messages.ModelMessages message) {

    }
}
