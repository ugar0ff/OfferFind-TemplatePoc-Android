package com.offerfind.template.poc.ui.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.offerfind.template.poc.ui.fragment.PagerItemFragment;
import com.offerfind.template.poc.ui.model.PagerItemModel;

import java.util.List;

/**
 * Created by ugar on 10.02.16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<PagerItemModel> list;

    public ViewPagerAdapter(FragmentManager fm, List<PagerItemModel> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Fragment getItem(int position) {
        return PagerItemFragment.newInstance(list.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
