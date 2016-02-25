package com.dddev.market.place.ui.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Opportunities;
import com.dddev.market.place.ui.fragment.AccountRootFragment;
import com.dddev.market.place.ui.fragment.MessagingFragment;
import com.dddev.market.place.ui.fragment.NewOrdersFragment;
import com.dddev.market.place.ui.fragment.OrdersFragment;
import com.dddev.market.place.ui.model.TabModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ugar on 12.02.16.
 */
public class TabAdapter extends FragmentStatePagerAdapter {

    private List<TabModel> list;
    private static int[] ICONS = new int[]{
            R.drawable.selector_orders,
            R.drawable.selector_messaging,
            R.drawable.selector_account
    };
    private ArrayList<Opportunities.ModelOpportunity> opportunityList;

    public TabAdapter(FragmentManager fm, List<TabModel> list) {
        super(fm);
        this.list = list;
        opportunityList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (opportunityList.size() == 0) {
                return NewOrdersFragment.newInstance();
            } else {
                return OrdersFragment.newInstance(opportunityList);
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

    public void setOrdersList(ArrayList<Opportunities.ModelOpportunity> opportunityList) {
        this.opportunityList = opportunityList;
        notifyDataSetChanged();
    }
}
