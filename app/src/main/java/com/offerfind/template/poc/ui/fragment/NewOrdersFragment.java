package com.offerfind.template.poc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.facebook.FacebookSdk;
import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.activity.ProposalActivity;
import com.offerfind.template.poc.ui.adapter.ViewPagerAdapter;
import com.offerfind.template.poc.ui.fragment.base.BaseFragment;
import com.offerfind.template.poc.ui.model.PagerItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ugar on 09.02.16.
 */
public class NewOrdersFragment extends BaseFragment implements View.OnClickListener {

    private ViewPagerAdapter pagerAdapter;
    private List<PagerItemModel> adapterList;
    private ViewPager viewPager;
    private ViewPager.PageTransformer pageTransformer;

    public static NewOrdersFragment newInstance() {
        return new NewOrdersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterList = new ArrayList<>();
        adapterList.add(new PagerItemModel(1, getString(R.string.construction_clean_up), getString(R.string.when_your_contractor), R.drawable.icon_view_pager));
        adapterList.add(new PagerItemModel(2, getString(R.string.basic_cleaning), getString(R.string.you_like_us_to_clean_for_you), R.drawable.icon_view_pager));
        adapterList.add(new PagerItemModel(3, getString(R.string.move_in_out_cleaning), getString(R.string.move_in_or_move_out), R.drawable.icon_view_pager));
        adapterList.add(new PagerItemModel(4, getString(R.string.construction_clean_up), getString(R.string.when_your_contractor), R.drawable.icon_view_pager));
        adapterList.add(new PagerItemModel(5, getString(R.string.basic_cleaning), getString(R.string.you_like_us_to_clean_for_you), R.drawable.icon_view_pager));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_new_orders, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), adapterList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1, false);
        viewPager.addOnPageChangeListener(pageChangeListener);
        setViewPagerTransform(true);
        view.findViewById(R.id.add_orders).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_orders:
                startActivity(new Intent(getActivity(), ProposalActivity.class));
                break;
        }
    }

    private void setViewPagerTransform(final boolean added) {
        if (added) {
            try {
                if (pageTransformer == null) {
                    pageTransformer = new TransformerItem(StackTransformer.class).clazz.newInstance();
                }
                viewPager.setPageTransformer(true, pageTransformer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(View page, float position) {
                }
            });
        }
    }

    private static final class TransformerItem {

        final String title;
        final Class<? extends ViewPager.PageTransformer> clazz;

        public TransformerItem(Class<? extends ViewPager.PageTransformer> clazz) {
            this.clazz = clazz;
            title = clazz.getSimpleName();
        }

        @Override
        public String toString() {
            return title;
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (viewPager.getCurrentItem() + 1 == adapterList.size()) {
                    setViewPagerTransform(false);
                    viewPager.setCurrentItem(1, false);
                    setViewPagerTransform(true);
                } else if (viewPager.getCurrentItem() == 0 && adapterList.size() > 1) {
                    setViewPagerTransform(false);
                    viewPager.setCurrentItem(adapterList.size() - 2, false);
                    setViewPagerTransform(true);
                }
            }
        }
    };
}
