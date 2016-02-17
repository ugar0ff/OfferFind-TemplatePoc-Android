package com.offerfind.template.poc.ui.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.fragment.AccountFragment;
import com.offerfind.template.poc.ui.fragment.AccountRootFragment;
import com.offerfind.template.poc.ui.fragment.MessagingFragment;
import com.offerfind.template.poc.ui.fragment.NewOrdersFragment;
import com.offerfind.template.poc.ui.fragment.OrdersFragment;
import com.offerfind.template.poc.ui.model.TabModel;

import java.util.List;

/**
 * Created by ugar on 12.02.16.
 */
public class TabAdapter extends FragmentStatePagerAdapter {

    private List<TabModel> list;
    private int orderSize;
    private static int[] ICONS = new int[] {
            R.drawable.selector_orders,
            R.drawable.selector_messaging,
            R.drawable.selector_account
    };

    public TabAdapter(FragmentManager fm, List<TabModel> list, int orderSize) {
        super(fm);
        this.list = list;
        this.orderSize = orderSize;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (orderSize > 0) {
                return OrdersFragment.newInstance();
            } else {
                return NewOrdersFragment.newInstance();
            }
        } else if (position == 1) {
            return MessagingFragment.newInstance();
        } else {
            return AccountRootFragment.newInstance();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public int getDrawableId(int position) {
        return ICONS[position];
    }
}
