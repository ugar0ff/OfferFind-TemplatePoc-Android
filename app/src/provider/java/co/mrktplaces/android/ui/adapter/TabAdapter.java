package co.mrktplaces.android.ui.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import co.mrktplaces.android.R;
import co.mrktplaces.android.ui.fragment.AccountRootFragment;
import co.mrktplaces.android.ui.fragment.MessagingFragment;
import co.mrktplaces.android.ui.fragment.OrdersFragment;
import co.mrktplaces.android.ui.model.TabModel;

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

    public TabAdapter(FragmentManager fm, List<TabModel> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return OrdersFragment.newInstance();
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
