package com.dddev.market.place.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.dddev.market.place.ui.activity.base.BaseActivity;
import com.dddev.market.place.ui.fragment.ProposalListFragment;

import java.util.ArrayList;

/**
 * Created by ugar on 10.02.16.
 */
public class ProposalActivity extends BaseActivity {

    public final static String OPPORTUNITIES_ID = "opportunities_id";
    private long opportunitiesId;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, ProposalActivity.class));
    }

    public static void launch(Context context, long id) {
        context.startActivity(new Intent(context, ProposalActivity.class).putExtra(OPPORTUNITIES_ID, id));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().hasExtra(OPPORTUNITIES_ID)) {
            opportunitiesId = getIntent().getLongExtra(OPPORTUNITIES_ID, 0);
        }
        setContentView(R.layout.activity_container);
        switchFragment(ProposalListFragment.newInstance(opportunitiesId), false, null);
        onBackStackChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        if (id == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                onBackPressed();
                onBackStackChanged();
                return true;
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
