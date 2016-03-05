package com.dddev.market.place.ui.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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
public class ProposalListFragment extends BaseFragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public final static String OPPORTUNITIES_ID = "opportunities_id";
    private ArrayList<Bids.ModelBids> adapterList;
    private ProposalListAdapter adapter;
    private long opportunitiesId;
    private EventSource eventSource;
    private int status;

    public static ProposalListFragment newInstance(long opportunitiesId) {
        ProposalListFragment listFragment = new ProposalListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(OPPORTUNITIES_ID, opportunitiesId);
        listFragment.setArguments(bundle);
        return listFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterList = new ArrayList<>();
        if (getArguments() != null) {
            opportunitiesId = getArguments().getLong(OPPORTUNITIES_ID);
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

//        listView.post(new Runnable() {
//            @Override
//            public void run() {
//                if (adapterList.size() == 0) {
//                    Bids.ModelBids modelBids = new Bids.ModelBids(-1, "", "", "", 0, 0, 0, "", 0);
//                    adapterList.add(modelBids);
//                }
//                try {
//                    float itemsHeight = ((adapterList.size() - 1) * (getResources().getDimension(R.dimen.proposal_item_height) + 2 * getResources().getDimension(R.dimen.proposal_list_divider_height))) + 2 * getResources().getDimension(R.dimen.proposal_list_divider_height);
//                    itemsHeight = itemsHeight % 0 == 0 ? itemsHeight : itemsHeight - 1;
//                    adapterList.get(adapterList.size() - 1).setFooterHeight(getFooterHeight((int) itemsHeight));
//                    adapter.notifyDataSetChanged();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        getActivity().getLoaderManager().restartLoader(StaticKeys.LoaderId.BIDS_LOADER, null, this);
        return view;
    }

//    private int getFooterHeight(int itemsHeight) {
//        int footerMinHeight = (int) Utilities.convertDpToPixel(100, getActivity());
//        if (getView() != null) {
//            int listHeight = getView().getHeight();
//            if (adapterList.size() > 0) {
//                if (itemsHeight < listHeight && footerMinHeight < listHeight - itemsHeight) {
//                    footerMinHeight = listHeight - itemsHeight;
//                }
//            } else {
//                footerMinHeight = listHeight;
//            }
//        }
//        return footerMinHeight;
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (Bids.ModelBids itemModel : adapterList) {
            if (itemModel.getId() == id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Transition changeTransform = TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_transform);
                    Transition explodeTransform = TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade);
                    setSharedElementReturnTransition(changeTransform);
                    setExitTransition(explodeTransform);

                    ProposalFragment proposalFragment = ProposalFragment.newInstance(itemModel, status);

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
                    switchFragmentListener.switchFragment(ProposalFragment.newInstance(itemModel, status), true, null);
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
                        if (bidStatus > status) {
                            status = bidStatus;
                        }
                        adapterList.add(model);
                    } while (cursor.moveToNext());
                }
                adapter.setStatus(status);
//                Bids.ModelBids modelBids = new Bids.ModelBids(-1, "", "", "", 0, 0, 0, "", 0);
//                adapterList.add(modelBids);
                adapter.notifyDataSetChanged();
                //TODO: add after change server request url
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
    public void onResume() {
        super.onResume();
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
}
