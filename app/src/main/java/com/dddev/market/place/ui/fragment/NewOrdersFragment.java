package com.dddev.market.place.ui.fragment;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.dddev.market.place.R;
import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.Opportunities;
import com.dddev.market.place.core.api.strongloop.OpportunityPostRepository;
import com.dddev.market.place.core.cache.CacheContentProvider;
import com.dddev.market.place.core.cache.CacheHelper;
import com.dddev.market.place.ui.activity.NewOrdersActivity;
import com.dddev.market.place.ui.activity.ProposalActivity;
import com.dddev.market.place.ui.adapter.ViewPagerAdapter;
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.dddev.market.place.ui.fragment.base.BaseLocationFragment;
import com.dddev.market.place.ui.model.PagerItemModel;
import com.dddev.market.place.utils.StaticKeys;
import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 09.02.16.
 */
public class NewOrdersFragment extends BaseLocationFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

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
        getActivity().getLoaderManager().restartLoader(StaticKeys.LoaderId.CATEGORY_LOADER, null, this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_orders:
                getAddress();
                break;
        }
    }

    private void setViewPagerTransform(final boolean added) {
        if (added) {
            try {
                if (pageTransformer == null) {
                    pageTransformer = new TransformerItem(DepthPageTransformer.class).clazz.newInstance();
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.i("onCreateLoader");
        switch (id) {
            case StaticKeys.LoaderId.CATEGORY_LOADER:
                String[] projection = new String[]{CacheHelper.CATEGORY_ID + " as _id ",
                        CacheHelper.CATEGORY_TITLE,
                        CacheHelper.CATEGORY_DESCRIPTION,
                        CacheHelper.CATEGORY_IMAGE_URL};
                return new CursorLoader(getActivity(), CacheContentProvider.CATEGORY_URI, projection, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Timber.i("onLoadFinished, loader.getId() = %s", loader.getId());
        switch (loader.getId()) {
            case StaticKeys.LoaderId.CATEGORY_LOADER:
                adapterList.clear();
                if (cursor.moveToFirst()) {
                    do {
                        PagerItemModel model = new PagerItemModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                        model.setTitle(cursor.getString(cursor.getColumnIndex(CacheHelper.CATEGORY_TITLE)));
                        model.setDescription(cursor.getString(cursor.getColumnIndex(CacheHelper.CATEGORY_DESCRIPTION)));
                        model.setImageUrl(cursor.getString(cursor.getColumnIndex(CacheHelper.CATEGORY_IMAGE_URL)));
                        adapterList.add(model);
                    } while (cursor.moveToNext());
                    //copy last element to first position
                    cursor.moveToLast();
                    PagerItemModel modelLast = new PagerItemModel();
                    modelLast.setId(cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                    modelLast.setTitle(cursor.getString(cursor.getColumnIndex(CacheHelper.CATEGORY_TITLE)));
                    modelLast.setDescription(cursor.getString(cursor.getColumnIndex(CacheHelper.CATEGORY_DESCRIPTION)));
                    modelLast.setImageUrl(cursor.getString(cursor.getColumnIndex(CacheHelper.CATEGORY_IMAGE_URL)));
                    adapterList.add(0, modelLast);
                    //copy first element to last position
                    cursor.moveToFirst();
                    PagerItemModel modelFirst = new PagerItemModel();
                    modelFirst.setId(cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                    modelFirst.setTitle(cursor.getString(cursor.getColumnIndex(CacheHelper.CATEGORY_TITLE)));
                    modelFirst.setDescription(cursor.getString(cursor.getColumnIndex(CacheHelper.CATEGORY_DESCRIPTION)));
                    modelFirst.setImageUrl(cursor.getString(cursor.getColumnIndex(CacheHelper.CATEGORY_IMAGE_URL)));
                    adapterList.add(modelFirst);
                }

                pagerAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.i("onLoaderReset");
    }

    private void createNewOrders(String address) {
        final OpportunityPostRepository repository = AppOfferFind.getRestAdapter(getActivity()).createRepository(OpportunityPostRepository.class);
        repository.createContract();
        repository.opportunities(adapterList.get(viewPager.getCurrentItem()).getTitle(),
                adapterList.get(viewPager.getCurrentItem()).getDescription(), address,
                new OpportunityPostRepository.OpportunityCallback() {
                    @Override
                    public void onSuccess(Opportunities.ModelOpportunity opportunity) {
                        Timber.i("onSuccess response=%s", opportunity.toString());
                        if (opportunity != null) {
                            ContentValues values = new ContentValues();
                            values.put(CacheHelper.OPPORTUNITIES_ID, opportunity.getId());
                            values.put(CacheHelper.OPPORTUNITIES_TITLE, opportunity.getTitle());
                            values.put(CacheHelper.OPPORTUNITIES_DESCRIPTION, opportunity.getDescription());
                            values.put(CacheHelper.OPPORTUNITIES_ACCOUNT_ID, opportunity.getOwnerId());
                            values.put(CacheHelper.OPPORTUNITIES_CREATE_AT, opportunity.getCreatedAt());
                            values.put(CacheHelper.OPPORTUNITIES_STATUS, opportunity.getStatus());
                            values.put(CacheHelper.OPPORTUNITIES_CATEGORY_ID, opportunity.getCategoryId());
                            getActivity().getContentResolver().insert(CacheContentProvider.OPPORTUNITIES_URI, values);
                            ProposalActivity.launch(getActivity());
                            if (getActivity() instanceof NewOrdersActivity) {
                                getActivity().finish();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e("onError Throwable: %s", t.toString());
                        showDialog(getString(R.string.server_connect_failure));
                    }
                });
    }


    @Override
    public void addressReceiveResult(String result) {
        Timber.i("addressReceiveResult = %s", result);
        createNewOrders(result);
    }
}
