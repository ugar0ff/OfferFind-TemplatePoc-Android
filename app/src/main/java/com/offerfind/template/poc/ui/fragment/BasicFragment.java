package com.offerfind.template.poc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.FacebookSdk;
import com.offerfind.template.poc.R;

/**
 * Created by ugar on 09.02.16.
 */
public class BasicFragment extends Fragment {

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_basic, container, false);
        return view;
    }

}
