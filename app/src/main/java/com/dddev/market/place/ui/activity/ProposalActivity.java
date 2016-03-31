package com.dddev.market.place.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.dddev.market.place.core.api.strongloop.Opportunities;
import com.dddev.market.place.core.cache.CacheContentProvider;
import com.dddev.market.place.core.cache.CacheHelper;
import com.dddev.market.place.ui.activity.base.BaseActivity;
import com.dddev.market.place.ui.controller.ToolbarController;
import com.dddev.market.place.ui.fragment.ProposalFragment;
import com.dddev.market.place.ui.fragment.ProposalListFragment;

/**
 * Created by ugar on 10.02.16.
 */
public class ProposalActivity extends BaseActivity implements ToolbarController {

    public final static String OPPORTUNITIES_ID = "opportunities_id";
    public final static String OPPORTUNITIES_NAME = "opportunities_name";
    public final static String IS_OPPORTUNITIES = "is_opportunities";
    private int id;
    private Bids.ModelBids itemModel;
    private Toolbar toolbar;

    public static void launch(Context context, int id, String title) {
        context.startActivity(new Intent(context, ProposalActivity.class).putExtra(OPPORTUNITIES_ID, id).putExtra(OPPORTUNITIES_NAME, title));
    }

    public static void launch(Context context, Bids.ModelBids itemModel) {
        context.startActivity(new Intent(context, ProposalActivity.class).putExtra(IS_OPPORTUNITIES, itemModel));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        String opportunitiesName = "";
        if (getIntent() != null) {
            if (getIntent().hasExtra(OPPORTUNITIES_ID)) {
                id = getIntent().getIntExtra(OPPORTUNITIES_ID, 0);
            }
            if (getIntent().hasExtra(IS_OPPORTUNITIES)) {
                itemModel = getIntent().getParcelableExtra(IS_OPPORTUNITIES);
            }
            if (getIntent().hasExtra(OPPORTUNITIES_NAME)) {
                opportunitiesName = getIntent().getStringExtra(OPPORTUNITIES_NAME);
            }
        }

        if (itemModel == null) {
            switchFragment(ProposalListFragment.newInstance(id, opportunitiesName), false, null);
        } else {
            Opportunities.ModelOpportunity opportunities = getStatus(itemModel.getOpportunityId());
            switchFragment(ProposalFragment.newInstance(itemModel, opportunities.getState(), opportunities.getTitle()), false, null);
        }
        onBackStackChanged();
    }

    private Opportunities.ModelOpportunity getStatus(int opportunitiesId) {
        Opportunities.ModelOpportunity opportunities = new Opportunities.ModelOpportunity();
        String[] projection = new String[] {CacheHelper.OPPORTUNITIES_ID + " as " + CacheHelper._ID, CacheHelper.OPPORTUNITIES_STATUS, CacheHelper.OPPORTUNITIES_TITLE};
        String selection = CacheHelper.OPPORTUNITIES_ID + " = ?";
        String[] selectionArg = new String[] {String.valueOf(opportunitiesId)};
        Cursor cursor = getContentResolver().query(CacheContentProvider.OPPORTUNITIES_URI, projection, selection, selectionArg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                opportunities.setState(cursor.getString(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_STATUS)));
                opportunities.setTitle(cursor.getString(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_TITLE)));
            }
            cursor.close();
        }
        return opportunities;
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

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

}
