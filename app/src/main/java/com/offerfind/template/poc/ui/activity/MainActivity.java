package com.offerfind.template.poc.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.activity.base.BaseActivity;
import com.offerfind.template.poc.ui.fragment.AccountFragment;
import com.offerfind.template.poc.ui.fragment.MessagingFragment;
import com.offerfind.template.poc.ui.fragment.NewOrdersFragment;
import com.offerfind.template.poc.ui.fragment.OrdersFragment;

import timber.log.Timber;

/**
 * Created by ugar on 10.02.16.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            swichOrdersFragment();
        }
        findViewById(R.id.btn_orders).setOnClickListener(this);
        findViewById(R.id.btn_messaging).setOnClickListener(this);
        findViewById(R.id.btn_account).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_orders:
                swichOrdersFragment();
                break;
            case R.id.btn_messaging:
                switchFragment(MessagingFragment.newInstance(), false, null);
                break;
            case R.id.btn_account:
                switchFragment(AccountFragment.newInstance(), false, null);
                break;
        }
    }

    private void swichOrdersFragment() {
        int size = 1; //TODO: orders count
        if (size > 0) {
            switchFragment(OrdersFragment.newInstance(), false, null);
        } else {
            switchFragment(NewOrdersFragment.newInstance(), false, null);
        }
    }
}
