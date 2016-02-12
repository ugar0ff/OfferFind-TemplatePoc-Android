package com.offerfind.template.poc.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.activity.base.BaseActivity;
import com.offerfind.template.poc.ui.adapter.TabAdapter;
import com.offerfind.template.poc.ui.model.TabModel;
import com.offerfind.template.poc.ui.view.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 10.02.16.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        setContentView(R.layout.activity_main);
        List<TabModel> pageList = new ArrayList<>();
//        pageList.add(new TabModel(3, R.drawable.icon_accounts));
        pageList.add(new TabModel(1, R.drawable.icon_orders_press));
        pageList.add(new TabModel(2, R.drawable.icon_messaging));
        pageList.add(new TabModel(3, R.drawable.icon_accounts));
//        pageList.add(new TabModel(1, R.drawable.icon_orders_press));
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), pageList, 0); //TODO: orders count
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

    }
}
