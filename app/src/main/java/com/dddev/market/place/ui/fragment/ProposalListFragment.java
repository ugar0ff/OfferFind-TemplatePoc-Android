package com.dddev.market.place.ui.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.dddev.market.place.core.cache.CacheContentProvider;
import com.dddev.market.place.core.cache.CacheHelper;
import com.dddev.market.place.ui.adapter.ProposalListAdapter;
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.dddev.market.place.ui.fragment.base.UpdateReceiverFragment;
import com.dddev.market.place.ui.views.eventsource_android.EventSource;
import com.dddev.market.place.ui.views.eventsource_android.EventSourceHandler;
import com.dddev.market.place.ui.views.eventsource_android.MessageEvent;
import com.dddev.market.place.utils.PreferencesUtils;
import com.dddev.market.place.utils.StaticKeys;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by ugar on 10.02.16.
 */
public class ProposalListFragment extends UpdateReceiverFragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public final static String OPPORTUNITIES_ID = "opportunities_id";
    public final static String OPPORTUNITIES_NAME = "opportunities_name";
    private ArrayList<Bids.ModelBids> adapterList;
    private ProposalListAdapter adapter;
    private long opportunitiesId;
    private EventSource eventSource;
    private int statusOpportunities;
    private String title;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static ProposalListFragment newInstance(long opportunitiesId, String opportunitiesName) {
        ProposalListFragment listFragment = new ProposalListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(OPPORTUNITIES_ID, opportunitiesId);
        bundle.putString(OPPORTUNITIES_NAME, opportunitiesName);
        listFragment.setArguments(bundle);
        return listFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterList = new ArrayList<>();
        if (getArguments() != null) {
            opportunitiesId = getArguments().getLong(OPPORTUNITIES_ID);
            title = getArguments().getString(OPPORTUNITIES_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_proposal_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list);
        adapter = new ProposalListAdapter(getActivity(), adapterList, acceptClickListener);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setEmptyView(view.findViewById(R.id.empty));
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        getActivity().getLoaderManager().restartLoader(StaticKeys.LoaderId.BIDS_LOADER, null, this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (Bids.ModelBids itemModel : adapterList) {
            if (itemModel.getId() == id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Transition changeTransform = TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_transform);
                    Transition explodeTransform = TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade);
                    setSharedElementReturnTransition(changeTransform);
                    setExitTransition(explodeTransform);

                    ProposalFragment proposalFragment = ProposalFragment.newInstance(itemModel, statusOpportunities);

                    proposalFragment.setSharedElementEnterTransition(changeTransform);
                    proposalFragment.setEnterTransition(explodeTransform);

                    ImageView picture = (ImageView) view.findViewById(R.id.picture);
                    TextView title = (TextView) view.findViewById(R.id.title);
                    TextView price = (TextView) view.findViewById(R.id.price);
                    TextView accept = (TextView) view.findViewById(R.id.accept);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack("transaction");
                    ft.replace(R.id.container, proposalFragment);
                    ft.addSharedElement(picture, String.format("picture%s", itemModel.getId()));
                    ft.addSharedElement(title, String.format("title%s", itemModel.getId()));
                    ft.addSharedElement(price, String.format("price%s", itemModel.getId()));
                    ft.addSharedElement(accept, String.format("accept%s", itemModel.getId()));
                    ft.commit();
                } else {
                    switchFragmentListener.switchFragment(ProposalFragment.newInstance(itemModel, statusOpportunities), true, null);
                }
                break;
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.i("onCreateLoader");
        switch (id) {
            case StaticKeys.LoaderId.BIDS_LOADER:
                String[] projection = new String[]{CacheHelper.BIDS_ID + " as _id ",
                        CacheHelper.BIDS_TITLE,
                        CacheHelper.BIDS_DESCRIPTION,
                        CacheHelper.BIDS_URL,
                        CacheHelper.BIDS_STATUS,
                        CacheHelper.BIDS_PRICE};
                String selection = CacheHelper.BIDS_OPPORTUNITIES_ID + " = ?";
                String[] selectionArg = new String[]{String.valueOf(opportunitiesId)};
                return new CursorLoader(getActivity(), CacheContentProvider.BIDS_URI, projection, selection, selectionArg, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Timber.i("onLoadFinished, loader.getId() = %s", loader.getId());
        switch (loader.getId()) {
            case StaticKeys.LoaderId.BIDS_LOADER:
                adapterList.clear();
                if (cursor.moveToFirst()) {
                    do {
                        Bids.ModelBids model = new Bids.ModelBids();
                        model.setId(cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                        model.setTitle(cursor.getString(cursor.getColumnIndex(CacheHelper.BIDS_TITLE)));
                        model.setDescription(cursor.getString(cursor.getColumnIndex(CacheHelper.BIDS_DESCRIPTION)));
                        model.setPrice(cursor.getFloat(cursor.getColumnIndex(CacheHelper.BIDS_PRICE)));
                        model.setUrl(cursor.getString(cursor.getColumnIndex(CacheHelper.BIDS_URL)));
                        int bidStatus = cursor.getInt(cursor.getColumnIndex(CacheHelper.BIDS_STATUS));
                        model.setState(bidStatus);
                        if (bidStatus > statusOpportunities) {
                            statusOpportunities = bidStatus;
                        }
                        adapterList.add(model);
                    } while (cursor.moveToNext());
                }
                adapter.setStatus(statusOpportunities);
                adapter.notifyDataSetChanged();

                Cursor cursor1 = getActivity().getContentResolver().query(CacheContentProvider.BIDS_URI, null, null, null, null);
                if (cursor1 != null) {
                    if (cursor1.moveToFirst()) {
                        do {
                            Timber.w("id %S, statusOpportunities %s, opportunity_id %s", cursor1.getString(cursor1.getColumnIndex(CacheHelper.BIDS_ID)),
                                    cursor1.getInt(cursor1.getColumnIndex(CacheHelper.BIDS_STATUS)),
                                    cursor1.getInt(cursor1.getColumnIndex(CacheHelper.BIDS_OPPORTUNITIES_ID)));
                        } while (cursor1.moveToNext());
                    }
                    cursor1.close();
                }

                //ыцшTODO: add after change server request url
//                streamMessages();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.i("onLoaderReset");
    }

    private void streamMessages() {
        Thread eventThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    eventSource = new EventSource(URI.create(AppOfferFind.API + "Bids/streamUpdates?_format=event-stream&access_token=" + PreferencesUtils.getUserToken(getActivity())), new SSEHandler(), null, true);
                    eventSource.connect();
                }
            }
        });
        eventThread.start();
    }

    private class SSEHandler implements EventSourceHandler {

        public SSEHandler() {
        }

        @Override
        public void onConnect() {
            Timber.v("SSE Connected");
        }

        @Override
        public void onMessage(String event, MessageEvent message) {
            Timber.v("SSE Message %s", event);
            Timber.v("SSE Message: %s", message.lastEventId);
            Timber.v("SSE Message: %s", message.data);
        }

        @Override
        public void onComment(String comment) {
            //comments only received if exposeComments turned on
            Timber.v("SSE Comment %s", comment);
        }

        @Override
        public void onError(Throwable t) {
            Timber.v("SSE Error");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            Timber.v("SSE Stacktrace %s", sw.toString());

        }

        @Override
        public void onClosed(boolean willReconnect) {
            Timber.v("SSE Closed reconnect? %s", willReconnect);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (eventSource != null && eventSource.isConnected()) {
            eventSource.close();
        }
    }

    private View.OnClickListener acceptClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (Bids.ModelBids bid : adapterList) {
                if (bid.getId() == (int) v.getTag()) {
                    Timber.i("bid.getId() = %s", bid.getId());
                    if (((TextView)v).getText().equals(getString(R.string.complete))) {
                        startCompleteBidsService(bid.getId());
                    } else {
                        startAcceptBidsService(bid.getId());
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(false);
        if (title != null) {
            toolbarTitleController.setToolbarTitle(title);
        }
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
