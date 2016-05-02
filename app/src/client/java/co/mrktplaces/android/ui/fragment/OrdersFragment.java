package co.mrktplaces.android.ui.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import co.mrktplaces.android.R;
import co.mrktplaces.android.core.api.strongloop.Opportunities;
import co.mrktplaces.android.core.cache.CacheContentProvider;
import co.mrktplaces.android.core.cache.CacheHelper;
import co.mrktplaces.android.ui.activity.NewOrdersActivity;
import co.mrktplaces.android.ui.activity.ProposalActivity;
import co.mrktplaces.android.ui.adapter.OrdersAdapter;
import co.mrktplaces.android.ui.fragment.base.UpdateReceiverFragment;
import co.mrktplaces.android.utils.StaticKeys;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 10.02.16.
 */
public class OrdersFragment extends UpdateReceiverFragment implements View.OnClickListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private List<Opportunities.ModelOpportunity> adapterList;
    private OrdersAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        view.findViewById(R.id.new_orders).setOnClickListener(this);
        ListView listView = (ListView) view.findViewById(R.id.list);
        adapter = new OrdersAdapter(getActivity(), adapterList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        getActivity().getLoaderManager().initLoader(StaticKeys.LoaderId.OPPORTUNITIES_LOADER, null, this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_orders:
                NewOrdersActivity.launch(getActivity());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProposalActivity.launch(getActivity(), adapterList.get(position).getId(), adapterList.get(position).getTitle());
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        startUpdateService();
    }

    @Override
    public void onHandleServerRequest() {
        if (getActivity() != null && mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onHandleServerRequestError() {
        if (getActivity() != null && mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.i("onCreateLoader");
        switch (id) {
            case StaticKeys.LoaderId.OPPORTUNITIES_LOADER:
                String[] projection = new String[]{CacheHelper.OPPORTUNITIES_ID + " as " + CacheHelper._ID,
                        CacheHelper.OPPORTUNITIES_TITLE,
                        CacheHelper.OPPORTUNITIES_CREATE_AT,
                        CacheHelper.OPPORTUNITIES_STATUS};
                return new CursorLoader(getActivity(), CacheContentProvider.OPPORTUNITIES_URI, projection, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Timber.i("onLoadFinished, loader.getId() = %s", loader.getId());
        switch (loader.getId()) {
            case StaticKeys.LoaderId.OPPORTUNITIES_LOADER:
                adapterList.clear();
                if (cursor.moveToFirst()) {
                    do {
                        Opportunities.ModelOpportunity model = new Opportunities.ModelOpportunity();
                        model.setId(cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                        model.setTitle(cursor.getString(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_TITLE)));
                        model.setCreatedAt(cursor.getString(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_CREATE_AT)));
                        model.setState(cursor.getString(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_STATUS)));
                        adapterList.add(model);
                    } while (cursor.moveToNext());
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.i("onLoaderReset");
    }
}
