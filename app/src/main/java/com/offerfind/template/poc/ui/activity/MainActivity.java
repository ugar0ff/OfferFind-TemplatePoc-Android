package com.offerfind.template.poc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.activity.base.BaseActivity;
import com.offerfind.template.poc.ui.fragment.BasicFragment;
import com.offerfind.template.poc.ui.fragment.LoginFragment;
import com.offerfind.template.poc.utils.PreferencesUtils;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (PreferencesUtils.getFbId(this) == null) {
            switchFragment(LoginFragment.newInstance(), false, null);
        } else {
            switchFragment(BasicFragment.newInstance(), false, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }


}

