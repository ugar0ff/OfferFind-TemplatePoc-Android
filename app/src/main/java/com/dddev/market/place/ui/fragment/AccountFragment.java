package com.dddev.market.place.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dddev.market.place.R;
import com.dddev.market.place.ui.fragment.base.BaseFragment;

/**
 * Created by ugar on 10.02.16.
 */
public class AccountFragment extends BaseFragment implements View.OnClickListener{

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
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

}
