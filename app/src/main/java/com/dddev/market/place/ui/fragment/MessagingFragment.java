package com.dddev.market.place.ui.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.core.loader.MessagingAsyncTaskLoader;
import com.dddev.market.place.ui.activity.NewOrdersActivity;
import com.dddev.market.place.ui.activity.ProposalActivity;
import com.dddev.market.place.ui.adapter.MessagingAdapter;
import com.dddev.market.place.ui.fragment.base.UpdateReceiverFragment;
import com.dddev.market.place.ui.views.eventsource_android.MessageEvent;
import com.dddev.market.place.utils.StaticKeys;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 09.02.16.
 */
public class MessagingFragment extends UpdateReceiverFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<ArrayList<Bids.ModelBids>>, AdapterView.OnItemClickListener {

    private List<Bids.ModelBids> adapterList;
    private MessagingAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static MessagingFragment newInstance() {
        return new MessagingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messaging, container, false);
        view.findViewById(R.id.new_orders).setOnClickListener(this);
        ListView listView = (ListView) view.findViewById(R.id.list);
        adapter = new MessagingAdapter(getActivity(), adapterList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        getActivity().getLoaderManager().initLoader(StaticKeys.LoaderId.ALL_BIDS_LOADER, null, this).forceLoad();
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
    public Loader<ArrayList<Bids.ModelBids>> onCreateLoader(int id, Bundle args) {
        Timber.i("onCreateLoader");
        switch (id) {
            case StaticKeys.LoaderId.ALL_BIDS_LOADER:
                return new MessagingAsyncTaskLoader(getActivity());
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Bids.ModelBids>> loader, ArrayList<Bids.ModelBids> data) {
        Timber.i("onLoadFinished, loader.getId() = %s, data.size = %s", loader.getId(), data.size());
        switch (loader.getId()) {
            case StaticKeys.LoaderId.ALL_BIDS_LOADER:
                adapterList.clear();
                if (data != null) {
                    for (Bids.ModelBids modelBids : data) {
                        adapterList.add(modelBids);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Bids.ModelBids>> loader) {
        Timber.i("onLoaderReset");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (Bids.ModelBids modelBids : adapterList) {
            if (modelBids.getId() == id) {
                ProposalActivity.launch(getActivity(), modelBids);
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onStreamMessage(Messages.ModelMessages message) {

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
}
