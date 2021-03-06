package co.mrktplaces.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import co.mrktplaces.android.R;
import co.mrktplaces.android.ui.activity.base.BaseActivity;
import co.mrktplaces.android.ui.controller.ToolbarController;
import co.mrktplaces.android.ui.fragment.NewOrdersFragment;

/**
 * Created by ugar on 24.02.16.
 */
public class NewOrdersActivity extends BaseActivity implements ToolbarController {

    private Toolbar toolbar;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, NewOrdersActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        switchFragment(NewOrdersFragment.newInstance(false), false, null);
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

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbarTitle(getString(R.string.new_order));
        toolbar.setY(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof NewOrdersFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
