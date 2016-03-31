package com.dddev.market.place.ui.fragment;

import android.animation.Animator;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.core.cache.CacheContentProvider;
import com.dddev.market.place.core.cache.CacheHelper;
import com.dddev.market.place.ui.adapter.ProposalListAdapter;
import com.dddev.market.place.ui.fragment.base.UpdateReceiverFragment;
import com.dddev.market.place.utils.StaticKeys;

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
    private String statusOpportunities;
    private String title;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView listView;
    private int lastPositionY;
    private int lastFirstVisibleItem;
    private Toolbar toolbar;
    private boolean isAnimated;

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
        listView = (ListView) view.findViewById(R.id.list);
        adapter = new ProposalListAdapter(getActivity(), adapterList, acceptClickListener);
        if (toolbar == null) {
            toolbar = toolbarController.getToolbar();
        }
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                View header = new View(getActivity());
                header.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, toolbar.getHeight()));
                listView.addHeaderView(header);
                listView.setAdapter(adapter);
            }
        });
        listView.setOnItemClickListener(this);
        listView.setEmptyView(view.findViewById(R.id.empty));
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getChildAt(firstVisibleItem) != null) {
                    if (lastFirstVisibleItem != firstVisibleItem) {
                        lastFirstVisibleItem = firstVisibleItem;
                        lastPositionY = view.getChildAt(firstVisibleItem).getTop();
                        return;
                    }

                    if (!isAnimated) {
                        if (lastPositionY < view.getChildAt(firstVisibleItem).getTop() && (toolbar.getY() < 0)) {
                            toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).setListener(animatorListener).start();
                        } else if (lastPositionY > view.getChildAt(firstVisibleItem).getTop() && (toolbar.getY() == 0)) {
                            toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2)).setListener(animatorListener).start();
                        }
                    }
                    lastPositionY = view.getChildAt(firstVisibleItem).getTop();
                }
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        getActivity().getLoaderManager().restartLoader(StaticKeys.LoaderId.BIDS_LOADER, null, this);
        return view;
    }

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isAnimated = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimated = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isAnimated = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (Bids.ModelBids itemModel : adapterList) {
            if (itemModel.getId() == id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Transition changeTransform = TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_transform);
                    Transition explodeTransform = TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade);
                    setSharedElementReturnTransition(changeTransform);
                    setExitTransition(explodeTransform);

                    ProposalFragment proposalFragment = ProposalFragment.newInstance(itemModel, statusOpportunities, title);

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
                    switchFragmentListener.switchFragment(ProposalFragment.newInstance(itemModel, statusOpportunities, title), true, null);
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
                        String bidStatus = cursor.getString(cursor.getColumnIndex(CacheHelper.BIDS_STATUS));
                        model.setState(bidStatus);

                        checkStatus(bidStatus);
                        adapterList.add(model);
                    } while (cursor.moveToNext());
                }
                adapter.setStatus(statusOpportunities);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.i("onLoaderReset");
    }

    @Override
    public void onStreamMessage(Messages.ModelMessages message) {
    }

    private void checkStatus(String bidStatus) {
        switch (bidStatus) {
            case StaticKeys.State.PUBLISHED:
                if (statusOpportunities == null || statusOpportunities.length() == 0) {
                    statusOpportunities = bidStatus;
                }
                break;
            case StaticKeys.State.CLOSED:
                statusOpportunities = bidStatus;
                break;
            case StaticKeys.State.ACCEPTED:
                if (statusOpportunities == null || !statusOpportunities.equals(StaticKeys.State.CLOSED)) {
                    statusOpportunities = bidStatus;
                }
                break;
        }
    }

    private View.OnClickListener acceptClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (Bids.ModelBids bid : adapterList) {
                if (bid.getId() == (int) v.getTag()) {
                    Timber.i("bid.getId() = %s", bid.getId());
                    if (((TextView) v).getText().equals(getString(R.string.complete))) {
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
            toolbarController.setToolbarTitle(title);
        }
        toolbar.setY(0);
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

//    @Override
//    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
//        Toolbar toolbar = toolbarController.getToolbar();
//        if (toolbar == null) {
//            return;
//        }
//        if (scrollState == ScrollState.UP) {
//            if (toolbar.isShown()) {
//                toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
//            }
//        } else if (scrollState == ScrollState.DOWN) {
//            if (!toolbar.isShown() && !isLastItem()) {
//                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
//            }
//        }
//    }

    private boolean isLastItem() {
        return listView.getLastVisiblePosition() == (adapterList.size() - 1);
    }
}
