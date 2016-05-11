package co.mrktplaces.android.ui.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import co.mrktplaces.android.R;

import co.mrktplaces.android.ui.fragment.AccountRootFragment;
import co.mrktplaces.android.ui.fragment.MessagingFragment;
import co.mrktplaces.android.ui.fragment.NewOrdersFragment;
import co.mrktplaces.android.ui.fragment.OrdersFragment;
import co.mrktplaces.android.ui.model.TabModel;

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
    private boolean isOrdersFragment;
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

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
            if (!isOrdersFragment) {
                return NewOrdersFragment.newInstance(true);
            } else {
                return OrdersFragment.newInstance();
            }
        } else if (position == 1) {
            return MessagingFragment.newInstance();
        } else {
            return AccountRootFragment.newInstance();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
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

    public void setOrdersList(boolean isOrdersFragment) {
        this.isOrdersFragment = isOrdersFragment;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
