package co.mrktplaces.android.ui.activity;

import android.os.Bundle;

import co.mrktplaces.android.R;
import co.mrktplaces.android.ui.activity.base.BaseActivity;
import co.mrktplaces.android.ui.fragment.CropFragment;

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
