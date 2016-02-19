package com.offerfind.template.poc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.core.AppOfferFind;
import com.offerfind.template.poc.core.api.strongloop.Opportunities;
import com.offerfind.template.poc.core.api.strongloop.OpportunityRepository;
import com.offerfind.template.poc.ui.activity.base.BaseActivity;
import com.offerfind.template.poc.ui.adapter.TabAdapter;
import com.offerfind.template.poc.ui.fragment.AccountRootFragment;
import com.offerfind.template.poc.ui.model.TabModel;
import com.offerfind.template.poc.ui.view.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 10.02.16.
 */
public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    private List<Opportunities.ModelOpportunity> opportunityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        setContentView(R.layout.activity_main);
        opportunityList = new ArrayList<>();
        List<TabModel> pageList = new ArrayList<>();
        pageList.add(new TabModel(1, R.drawable.icon_orders_press));
        pageList.add(new TabModel(2, R.drawable.icon_messaging));
        pageList.add(new TabModel(3, R.drawable.icon_accounts));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), pageList, opportunityList.size()); //TODO: orders count
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

        getOpportunityList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (viewPager.getCurrentItem() == 2) {
            AccountRootFragment fragment = (AccountRootFragment)tabAdapter.getItem(2);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getOpportunityList() {
        final OpportunityRepository repository = AppOfferFind.getRestAdapter(getApplicationContext()).createRepository(OpportunityRepository.class);
        repository.createContract();
        repository.opportunities(new OpportunityRepository.OpportunityCallback() {
            @Override
            public void onSuccess(Opportunities opportunity) {
                Timber.i("onSuccess response=%s", opportunity.toString());
            }

            @Override
            public void onError(Throwable t) {
                Timber.e("onError Throwable: %s", t.toString());
            }
        });
    }

}
