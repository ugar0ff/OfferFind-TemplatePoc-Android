package co.mrktplaces.android.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.mrktplaces.android.R;
import co.mrktplaces.android.core.api.retrofit.ApiRetrofit;
import co.mrktplaces.android.core.api.retrofit.BidRequest;
import co.mrktplaces.android.core.api.strongloop.Bids;
import co.mrktplaces.android.core.api.strongloop.Opportunities;
import co.mrktplaces.android.core.api.strongloop.StreamModel;
import co.mrktplaces.android.core.cache.CacheContentProvider;
import co.mrktplaces.android.core.cache.CacheHelper;
import co.mrktplaces.android.ui.activity.base.BaseActivity;
import co.mrktplaces.android.ui.adapter.TabAdapter;
import co.mrktplaces.android.ui.controller.MessageCountController;
//import co.mrktplaces.android.ui.fragment.AccountRootFragment;
import co.mrktplaces.android.ui.fragment.AccountRootFragment;
import co.mrktplaces.android.ui.model.TabModel;
import co.mrktplaces.android.ui.views.smarttablayout.SmartTabLayout;
import co.mrktplaces.android.utils.PermissionHelper;
import co.mrktplaces.android.utils.PreferencesUtils;
import co.mrktplaces.android.utils.StaticKeys;
import co.mrktplaces.android.utils.Utilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by ugar on 10.02.16.
 */
public class MainActivity extends BaseActivity implements MessageCountController {

    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    private SmartTabLayout tabLayout;
    public final static String START_INTENT = "start_intent";

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
    public void setMessageCount(int messageCount) {
        tabLayout.setMessageCount(messageCount);
    }

    private void showNewOpportunitiesDialog(final StreamModel.ModelMessages modelMessages) {
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_new_opportunities, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView address = (TextView) dialogView.findViewById(R.id.address);
        if (modelMessages.getLocation() != null && modelMessages.getLocation().getAddress() != null) {
            address.setText(Html.fromHtml("<font color=#666666>" + getString(R.string.address_colon) + "</font> <font color=#000000> " + modelMessages.getLocation().getAddress() + "</font>"));
        }
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        if (modelMessages.getTitle() != null) {
            title.setText(Html.fromHtml("<font color=#666666>" + getString(R.string.title_colon) + "</font> <font color=#000000> " + modelMessages.getTitle() + "</font>"));
        }
        TextView name = (TextView) dialogView.findViewById(R.id.name);
        if (modelMessages.getOwner() != null && modelMessages.getOwner().getName() != null) {
            name.setText(Html.fromHtml("<font color=#666666>" + getString(R.string.name_colon) + "</font> <font color=#000000> " + modelMessages.getOwner().getName() + "</font>"));
        }
        builder.setView(dialogView).create();
        final AlertDialog dialog = builder.show();
        dialogView.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.btnSkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToSkip(modelMessages.getId());
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.btnAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Bids.ModelBids> call = ApiRetrofit.apply(new BidRequest(getString(R.string.i_can), 0, modelMessages.getId(),
                        PreferencesUtils.getUserId(MainActivity.this)), PreferencesUtils.getUserToken(MainActivity.this));
                call.enqueue(new Callback<Bids.ModelBids>() {
                    @Override
                    public void onResponse(Call<Bids.ModelBids> call, Response<Bids.ModelBids> response) {
                        Timber.i("onResponse %s", response.toString());
                        addToSkip(modelMessages.getId());
                    }

                    @Override
                    public void onFailure(Call<Bids.ModelBids> call, Throwable t) {
                        Timber.e("onFailure %s", t.toString());
                    }
                });
                dialog.dismiss();
            }
        });

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        final int width = metrics.widthPixels;
        final int height = metrics.heightPixels;

        dialogView.post(new Runnable() {
            @Override
            public void run() {
                dialogView.setLayoutParams(new FrameLayout.LayoutParams((int) (width - Utilities.convertDpToPixel(32, getApplicationContext())), height));
            }
        });
    }

    private void addToSkip(int id) {
        ContentValues values = new ContentValues();
        values.put(CacheHelper.SKIP_OPPORTUNITIES_ID, id);
        getContentResolver().insert(CacheContentProvider.SKIP_URI, values);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(START_INTENT) && intent.getBundleExtra(START_INTENT) != null) {
            showNewOpportunitiesDialog((StreamModel.ModelMessages) intent.getBundleExtra(START_INTENT).getParcelable("ModelMessages"));
        }
    }
}
