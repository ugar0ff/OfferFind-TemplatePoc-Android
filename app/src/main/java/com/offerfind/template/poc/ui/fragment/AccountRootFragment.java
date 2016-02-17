package com.offerfind.template.poc.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.fragment.base.BaseFragment;

/**
 * Created by ugar on 17.02.16.
 */
public class AccountRootFragment extends BaseFragment {

    public static AccountRootFragment newInstance() {
        return new AccountRootFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_root, container, false);
        setAccountFragment();
        return view;
    }

    public void setAccountFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition changeTransform = TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_transform);
            Transition explodeTransform = TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade);
            setSharedElementReturnTransition(changeTransform);
            setExitTransition(explodeTransform);

            AccountFragment accountFragment = AccountFragment.newInstance();

            accountFragment.setSharedElementEnterTransition(changeTransform);
            accountFragment.setEnterTransition(explodeTransform);

            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.container, accountFragment);

            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.container);
            if (fragment != null && fragment.getView() != null) {
                ImageView avatar = (ImageView) fragment.getView().findViewById(R.id.avatar);
                TextView name = (TextView) fragment.getView().findViewById(R.id.name);
                TextView locationHint = (TextView) fragment.getView().findViewById(R.id.location_hint);
                TextView location = (TextView) fragment.getView().findViewById(R.id.location);
                TextView emailHint = (TextView) fragment.getView().findViewById(R.id.email_hint);
                TextView email = (TextView) fragment.getView().findViewById(R.id.email);
                TextView bankingInfoHint = (TextView) fragment.getView().findViewById(R.id.banking_info_hint);
                TextView bankingInfo = (TextView) fragment.getView().findViewById(R.id.banking_info);
                ft.addSharedElement(avatar, "avatar");
                ft.addSharedElement(name, "name");
                ft.addSharedElement(locationHint, "location_hint");
                ft.addSharedElement(location, "location");
                ft.addSharedElement(emailHint, "email_hint");
                ft.addSharedElement(email, "email");
                ft.addSharedElement(bankingInfoHint, "banking_info_hint");
                ft.addSharedElement(bankingInfo, "banking_info");
            }
            ft.commit();
        } else {
            try {
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction tr = fm.beginTransaction();
                try {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tr.replace(R.id.container, AccountFragment.newInstance());
                tr.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAccountEditFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition changeTransform = TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_transform);
            Transition explodeTransform = TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade);
            setSharedElementReturnTransition(changeTransform);
            setExitTransition(explodeTransform);

            AccountEditFragment accountEditFragment = AccountEditFragment.newInstance();

            accountEditFragment.setSharedElementEnterTransition(changeTransform);
            accountEditFragment.setEnterTransition(explodeTransform);

            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.container, accountEditFragment);

            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.container);
            if (fragment != null && fragment.getView() != null) {
                ImageView avatar = (ImageView) fragment.getView().findViewById(R.id.avatar);
                TextView name = (TextView) fragment.getView().findViewById(R.id.name);
                TextView locationHint = (TextView) fragment.getView().findViewById(R.id.location_hint);
                TextView location = (TextView) fragment.getView().findViewById(R.id.location);
                TextView emailHint = (TextView) fragment.getView().findViewById(R.id.email_hint);
                TextView email = (TextView) fragment.getView().findViewById(R.id.email);
                TextView bankingInfoHint = (TextView) fragment.getView().findViewById(R.id.banking_info_hint);
                TextView bankingInfo = (TextView) fragment.getView().findViewById(R.id.banking_info);
                ft.addSharedElement(avatar, "avatar");
                ft.addSharedElement(name, "name");
                ft.addSharedElement(locationHint, "location_hint");
                ft.addSharedElement(location, "location");
                ft.addSharedElement(emailHint, "email_hint");
                ft.addSharedElement(email, "email");
                ft.addSharedElement(bankingInfoHint, "banking_info_hint");
                ft.addSharedElement(bankingInfo, "banking_info");
            }
            ft.commit();
        } else {
            try {
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction tr = fm.beginTransaction();
                tr.addToBackStack(null);
                tr.replace(R.id.container, AccountEditFragment.newInstance());
                tr.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
