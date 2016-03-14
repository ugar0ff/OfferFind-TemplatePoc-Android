package com.dddev.market.place.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.dddev.market.place.ui.activity.base.BaseActivity;
import com.dddev.market.place.ui.controller.ToolbarTitleController;
import com.dddev.market.place.ui.fragment.ProposalFragment;
import com.dddev.market.place.ui.fragment.ProposalListFragment;

/**
 * Created by ugar on 10.02.16.
 */
public class ProposalActivity extends BaseActivity implements ToolbarTitleController {

    public final static String OPPORTUNITIES_ID = "opportunities_id";
    public final static String OPPORTUNITIES_NAME = "opportunities_name";
    public final static String IS_OPPORTUNITIES = "is_opportunities";
    private long id;
    private Bids.ModelBids itemModel;

    public static void launch(Context context, long id, String title) {
        context.startActivity(new Intent(context, ProposalActivity.class).putExtra(OPPORTUNITIES_ID, id).putExtra(OPPORTUNITIES_NAME, title));
    }

    public static void launch(Context context, long id, Bids.ModelBids itemModel) {
        context.startActivity(new Intent(context, ProposalActivity.class).putExtra(OPPORTUNITIES_ID, id).putExtra(IS_OPPORTUNITIES, itemModel));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String opportunitiesName = "";
        if (getIntent() != null) {
            if (getIntent().hasExtra(OPPORTUNITIES_ID)) {
                id = getIntent().getLongExtra(OPPORTUNITIES_ID, 0);
            }
            if (getIntent().hasExtra(IS_OPPORTUNITIES)) {
                itemModel = getIntent().getParcelableExtra(IS_OPPORTUNITIES);
            }
            if (getIntent().hasExtra(OPPORTUNITIES_NAME)) {
                opportunitiesName = getIntent().getStringExtra(OPPORTUNITIES_NAME);
            }
            setContentView(R.layout.activity_container);
        }
        if (itemModel == null) {
            switchFragment(ProposalListFragment.newInstance(id, opportunitiesName), false, null);
        } else {
            //TODO get status for all bids of opportunities
            switchFragment(ProposalFragment.newInstance(itemModel, itemModel.getState()), false, null);
        }
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

    @Override
    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
