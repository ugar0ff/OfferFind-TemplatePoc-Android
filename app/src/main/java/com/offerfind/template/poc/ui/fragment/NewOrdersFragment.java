package com.offerfind.template.poc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.facebook.FacebookSdk;
import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.activity.MainActivity;
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

    public static NewOrdersFragment newInstance() {
        return new NewOrdersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterList = new ArrayList<>();
        adapterList.add(new PagerItemModel(1, "titlt1", "description1", ""));
        adapterList.add(new PagerItemModel(2, "titlt2", "description2", ""));
        adapterList.add(new PagerItemModel(3, "titlt3", "description3", ""));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_new_orders, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), adapterList);
        viewPager.setAdapter(pagerAdapter);
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
}
