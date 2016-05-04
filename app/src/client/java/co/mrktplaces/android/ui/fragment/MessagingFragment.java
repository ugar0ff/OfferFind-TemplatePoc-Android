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
import co.mrktplaces.android.core.api.strongloop.Bids;
import co.mrktplaces.android.core.cache.CacheContentProvider;
import co.mrktplaces.android.core.cache.CacheHelper;
import co.mrktplaces.android.core.loader.MessagingAsyncTaskLoader;
import co.mrktplaces.android.ui.activity.NewOrdersActivity;
import co.mrktplaces.android.ui.activity.ProposalActivity;
import co.mrktplaces.android.ui.adapter.MessagingAdapter;
import co.mrktplaces.android.ui.fragment.base.UpdateReceiverFragment;
import co.mrktplaces.android.utils.StaticKeys;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 09.02.16.
 */
public class MessagingFragment extends UpdateReceiverFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

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
        getActivity().getLoaderManager().restartLoader(StaticKeys.LoaderId.CHANGE_MESSAGE_LOADER, null, changeMessageLoader);
        getActivity().getLoaderManager().restartLoader(StaticKeys.LoaderId.CHANGE_BIDS_LOADER, null, changeMessageLoader);
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

    private LoaderManager.LoaderCallbacks<ArrayList<Bids.ModelBids>> allBidsLoader = new LoaderManager.LoaderCallbacks<ArrayList<Bids.ModelBids>>() {
        @Override
        public Loader<ArrayList<Bids.ModelBids>> onCreateLoader(int id, Bundle args) {
            Timber.i("onCreateLoader id = %s", id);
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
                        int messageCount = 0;
                        for (int i = 0; i < data.size(); i++) {
                            if (!data.get(i).isRead(getActivity())) {
                                messageCount++;
                            }
                        }
                        if (messageCountController != null) {
                            messageCountController.setMessageCount(messageCount);
                        }
                        adapterList.addAll(data);
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Bids.ModelBids>> loader) {
            Timber.i("onLoaderReset");
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> changeMessageLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Timber.i("onCreateLoader id = %s", id);
            switch (id) {
                case StaticKeys.LoaderId.CHANGE_MESSAGE_LOADER:
                    return new CursorLoader(getActivity(), CacheContentProvider.MESSAGE_URI, null, null, null, null);
                case StaticKeys.LoaderId.CHANGE_BIDS_LOADER:
                    return new CursorLoader(getActivity(), CacheContentProvider.BIDS_URI, null, null, null, null);
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Timber.i("onLoadFinished loader.getId() = %s", loader.getId());
            switch (loader.getId()) {
                case StaticKeys.LoaderId.CHANGE_MESSAGE_LOADER:
                case StaticKeys.LoaderId.CHANGE_BIDS_LOADER:
                    if (getActivity() != null) {
                        getActivity().getLoaderManager().restartLoader(StaticKeys.LoaderId.ALL_BIDS_LOADER, null, allBidsLoader);
                    }
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Timber.i("onLoaderReset");
        }
    };

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
