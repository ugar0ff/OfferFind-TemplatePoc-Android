package com.offerfind.template.poc.ui.activity;

import android.os.Bundle;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.activity.base.BaseActivity;
import com.offerfind.template.poc.ui.fragment.ProposalListFragment;

/**
 * Created by ugar on 10.02.16.
 */
public class ProposalActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal);
        switchFragment(ProposalListFragment.newInstance(), false, null);
    }
}
