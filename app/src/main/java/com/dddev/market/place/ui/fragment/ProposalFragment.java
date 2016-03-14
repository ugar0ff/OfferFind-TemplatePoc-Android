package com.dddev.market.place.ui.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.dddev.market.place.core.cache.CacheContentProvider;
import com.dddev.market.place.core.cache.CacheHelper;
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.dddev.market.place.utils.StaticKeys;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

/**
 * Created by ugar on 11.02.16.
 */
public class ProposalFragment extends BaseFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private final static String PROPOSAL_MODEL = "proposal_model";
    private final static String PROPOSAL_STATUS = "proposal_status";
    private Bids.ModelBids itemModel;
    private int statusOpportunities;
    private TextView accept;
    private ChatFragment chatFragment;

    public static ProposalFragment newInstance(Bids.ModelBids itemModel, int statusOpportunities) {
        ProposalFragment fragment = new ProposalFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROPOSAL_MODEL, itemModel);
        bundle.putInt(PROPOSAL_STATUS, statusOpportunities);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemModel = getArguments().getParcelable(PROPOSAL_MODEL);
            statusOpportunities = getArguments().getInt(PROPOSAL_STATUS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposal, container, false);
        TextView title = (TextView) view.findViewById(R.id.title);
        if (itemModel.getTitle() != null) {
            title.setText(itemModel.getTitle());
        }
        TextView price = (TextView) view.findViewById(R.id.price);
        price.setText(String.format("$ %s", itemModel.getPrice()));

        accept = (TextView) view.findViewById(R.id.accept);
        accept.setOnClickListener(this);
        setAcceptButtonState();

        ImageView picture = (ImageView) view.findViewById(R.id.picture);
        if (itemModel.getUrl() != null && !itemModel.getUrl().isEmpty()) {
            Picasso.with(getActivity()).load(itemModel.getUrl()).into(picture);
        } else {
            Picasso.with(getActivity()).load(R.color.colorGrey).into(picture);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            title.setTransitionName(String.format("title%s", itemModel.getId()));
            price.setTransitionName(String.format("price%s", itemModel.getId()));
            accept.setTransitionName(String.format("accept%s", itemModel.getId()));
            picture.setTransitionName(String.format("picture%s", itemModel.getId()));
        }
        getActivity().getLoaderManager().initLoader(StaticKeys.LoaderId.ACCEPT_STATE_LOADER, null, this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (itemModel.getTitle() != null) {
            toolbarTitleController.setToolbarTitle(itemModel.getTitle());
        }
    }

    private void setChatFragment(int id) {
        try {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction tr = fm.beginTransaction();
            try {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            boolean accessToWriteMessage = statusOpportunities != 2;
            if (accessToWriteMessage && statusOpportunities == 1) {
                accessToWriteMessage = itemModel.getState() == statusOpportunities;
            }
            chatFragment = ChatFragment.newInstance(id, accessToWriteMessage);
            tr.replace(R.id.container, chatFragment);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept:
                if (((TextView)v).getText().equals(getActivity().getString(R.string.complete))) {
                    startCompleteBidsService(itemModel.getId());
                } else {
                    startAcceptBidsService(itemModel.getId());
                }
                break;
        }
    }

    private void setAcceptButtonState() {
        if (itemModel.getState() == 2) {
            statusOpportunities = itemModel.getState();
            accept.setVisibility(View.GONE);
        } else
        if (itemModel.getState() == 1) {
            if (itemModel.getState() >= statusOpportunities) {
                statusOpportunities = itemModel.getState();
                accept.setText(getActivity().getString(R.string.complete));
                accept.setVisibility(View.VISIBLE);
            } else {
                accept.setText(getActivity().getString(R.string.complete));
                accept.setVisibility(View.GONE);
            }
        } else {
            accept.setText(getActivity().getString(R.string.accept));
            accept.setVisibility(View.VISIBLE);
        }
        if (chatFragment == null) {
            setChatFragment(itemModel.getId());
        } else {
            chatFragment.setAccessToWriteMessage(accept.getVisibility() == View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.i("onCreateLoader");
        switch (id) {
            case StaticKeys.LoaderId.ACCEPT_STATE_LOADER:
                String[] projection = new String[]{CacheHelper.BIDS_ID + " as _id ",
                        CacheHelper.BIDS_STATUS};
                String selection = CacheHelper.BIDS_ID + " = ?";
                String[] selectionArg = new String[]{String.valueOf(itemModel.getId())};
                return new CursorLoader(getActivity(), CacheContentProvider.BIDS_URI, projection, selection, selectionArg, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Timber.i("onLoadFinished, loader.getId() = %s", loader.getId());
        switch (loader.getId()) {
            case StaticKeys.LoaderId.ACCEPT_STATE_LOADER:
                if (cursor.moveToFirst()) {
                    itemModel.setState(cursor.getInt(cursor.getColumnIndex(CacheHelper.BIDS_STATUS)));
                    setAcceptButtonState();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.i("onLoaderReset");
    }

}
