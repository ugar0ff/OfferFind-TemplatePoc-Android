package com.offerfind.template.poc.ui.activity;

import android.os.Bundle;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.activity.base.BaseActivity;
import com.offerfind.template.poc.ui.fragment.FragmentCrop;

/**
 * Created by ugar on 17.02.16.
 */
public class CropActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crop);
        setResult(RESULT_CANCELED);
        if(savedInstanceState == null) {
            switchFragment(FragmentCrop.newInstance(), false, null);
        }
    }

}
