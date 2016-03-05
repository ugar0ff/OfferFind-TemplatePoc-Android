package com.dddev.market.place.ui.fragment;

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
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by ugar on 11.02.16.
 */
public class ProposalFragment extends BaseFragment implements View.OnClickListener{

    private final static String PROPOSAL_MODEL = "proposal_model";
    private final static String PROPOSAL_STATUS = "proposal_status";
    private Bids.ModelBids itemModel;
    private int status;

    public static ProposalFragment newInstance(Bids.ModelBids itemModel, int status) {
        ProposalFragment fragment = new ProposalFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROPOSAL_MODEL, itemModel);
        bundle.putInt(PROPOSAL_STATUS, status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemModel = getArguments().getParcelable(PROPOSAL_MODEL);
            status = getArguments().getInt(PROPOSAL_STATUS);
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

        TextView accept = (TextView) view.findViewById(R.id.accept);
        accept.setOnClickListener(this);
        if (status == 2) {
            accept.setVisibility(View.GONE);
        } else
        if (status == 1) {
            if (itemModel.getState() == status) {
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
        setChatFragment(itemModel.getId());
        return view;
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
            boolean accessToWriteMessage = status != 2;
            if (accessToWriteMessage && status == 1) {
                accessToWriteMessage = itemModel.getState() == status;
            }
            tr.replace(R.id.container, ChatFragment.newInstance(id, accessToWriteMessage));
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
}
