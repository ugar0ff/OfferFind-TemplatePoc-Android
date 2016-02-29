package com.dddev.market.place.ui.activity;

import android.os.Bundle;

import com.dddev.market.place.R;
import com.dddev.market.place.ui.activity.base.BaseActivity;
import com.dddev.market.place.ui.fragment.CropFragment;

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
            switchFragment(CropFragment.newInstance(), false, null);
        }
    }

}
