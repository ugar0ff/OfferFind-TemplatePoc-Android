package com.dddev.market.place.ui.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Opportunities;
import com.dddev.market.place.core.cache.CacheContentProvider;
import com.dddev.market.place.core.cache.CacheHelper;
import com.dddev.market.place.ui.activity.base.BaseActivity;
import com.dddev.market.place.ui.adapter.TabAdapter;
import com.dddev.market.place.ui.fragment.AccountRootFragment;
import com.dddev.market.place.ui.model.TabModel;
import com.dddev.market.place.ui.view.smarttablayout.SmartTabLayout;
import com.dddev.market.place.utils.StaticKeys;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 10.02.16.
 */
public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    private ArrayList<Opportunities.ModelOpportunity> opportunityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        opportunityList = new ArrayList<>();
        List<TabModel> pageList = new ArrayList<>();
        pageList.add(new TabModel(1, R.drawable.icon_orders_press));
        pageList.add(new TabModel(2, R.drawable.icon_messaging));
        pageList.add(new TabModel(3, R.drawable.icon_accounts));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), pageList);
        viewPager.setAdapter(tabAdapter);

        final SmartTabLayout tabLayout = (SmartTabLayout) findViewById(R.id.viewpager_tab);
        tabLayout.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        getLoaderManager().restartLoader(StaticKeys.LoaderId.OPPORTUNITIES_LOADER, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (viewPager.getCurrentItem() == 2) {
            AccountRootFragment fragment = (AccountRootFragment) tabAdapter.getItem(2);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.i("onCreateLoader");
        switch (id) {
            case StaticKeys.LoaderId.OPPORTUNITIES_LOADER:
                String[] projection = new String[]{CacheHelper.OPPORTUNITIES_ID + " as _id ",
                        CacheHelper.OPPORTUNITIES_TITLE,
                        CacheHelper.OPPORTUNITIES_DATE,
                        CacheHelper.OPPORTUNITIES_STATUS,};
                return new CursorLoader(this, CacheContentProvider.OPPORTUNITIES_URI, projection, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Timber.i("onLoadFinished, loader.getId() = %s", loader.getId());
        switch (loader.getId()) {
            case StaticKeys.LoaderId.OPPORTUNITIES_LOADER:
                opportunityList.clear();
                if (cursor.moveToFirst()) {
                    do {
                        Opportunities.ModelOpportunity model = new Opportunities.ModelOpportunity();
                        model.setId(cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                        model.setTitle(cursor.getString(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_TITLE)));
                        model.setCreateAt(cursor.getLong(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_TITLE)));
                        model.setCreateAt(cursor.getInt(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_STATUS)));
                        opportunityList.add(model);
                    } while (cursor.moveToNext());
                }
                tabAdapter.setOrdersList(opportunityList);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.i("onLoaderReset");
    }
}
