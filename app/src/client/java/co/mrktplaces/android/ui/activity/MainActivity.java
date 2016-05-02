package co.mrktplaces.android.ui.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import co.mrktplaces.android.R;
import co.mrktplaces.android.core.cache.CacheContentProvider;
import co.mrktplaces.android.core.cache.CacheHelper;
import co.mrktplaces.android.ui.activity.base.BaseActivity;
import co.mrktplaces.android.ui.adapter.TabAdapter;
import co.mrktplaces.android.ui.controller.MessageCountController;
import co.mrktplaces.android.ui.fragment.AccountRootFragment;
import co.mrktplaces.android.ui.model.TabModel;
import co.mrktplaces.android.ui.views.smarttablayout.SmartTabLayout;
import co.mrktplaces.android.utils.PermissionHelper;
import co.mrktplaces.android.utils.StaticKeys;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 10.02.16.
 */
public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, MessageCountController {

    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    private SmartTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<TabModel> pageList = new ArrayList<>();
        pageList.add(new TabModel(1, R.drawable.icon_orders_press));
        pageList.add(new TabModel(2, R.drawable.icon_messaging));
        pageList.add(new TabModel(3, R.drawable.icon_accounts));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), pageList);
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = (SmartTabLayout) findViewById(R.id.viewpager_tab);
        assert tabLayout != null;
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
        PermissionHelper.verifyStoragePermissions(this);
        PermissionHelper.verifyMapPermissions(this);

        getLoaderManager().restartLoader(StaticKeys.LoaderId.TAB_LOADER, null, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Timber.i("onRequestPermissionsResult");
        if (viewPager.getCurrentItem() == 2) {
            AccountRootFragment fragment = (AccountRootFragment) tabAdapter.getItem(2);
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            case StaticKeys.LoaderId.TAB_LOADER:
                String[] projection = new String[]{CacheHelper.OPPORTUNITIES_ID + " as _id "};
                return new CursorLoader(this, CacheContentProvider.OPPORTUNITIES_URI, projection, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Timber.i("onLoadFinished, loader.getId() = %s", loader.getId());
        switch (loader.getId()) {
            case StaticKeys.LoaderId.TAB_LOADER:
                tabAdapter.setOrdersList(cursor.getCount() > 0);
                if (cursor.getCount() > 0) {
                    getLoaderManager().destroyLoader(StaticKeys.LoaderId.TAB_LOADER);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.i("onLoaderReset");
    }

    @Override
    public void setMessageCount(int messageCount) {
        tabLayout.setMessageCount(messageCount);
    }
}
