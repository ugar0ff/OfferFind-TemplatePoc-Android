package com.offerfind.template.poc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.fragment.base.BaseFragment;

/**
 * Created by ugar on 10.02.16.
 */
public class OrdersFragment extends BaseFragment implements View.OnClickListener{

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        view.findViewById(R.id.new_orders).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_orders:
                switchFragmentListener.switchFragment(NewOrdersFragment.newInstance(), true, null);
                break;
        }
    }
}
