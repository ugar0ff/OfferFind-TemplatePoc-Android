package com.offerfind.template.poc.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.fragment.base.BaseFragment;
import com.offerfind.template.poc.ui.model.ProposalItemModel;
import com.squareup.picasso.Picasso;

/**
 * Created by ugar on 11.02.16.
 */
public class ProposalFragment extends BaseFragment {

    private final static String PROPOSAL_MODEL = "proposal_model";
    private ProposalItemModel itemModel;

    public static ProposalFragment newInstance(ProposalItemModel itemModel) {
        ProposalFragment fragment = new ProposalFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROPOSAL_MODEL, itemModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemModel = getArguments().getParcelable(PROPOSAL_MODEL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposal, container, false);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(itemModel.getTitle());
        TextView price = (TextView) view.findViewById(R.id.price);
        price.setText(String.format("$ %s", itemModel.getPrice()));
        TextView accept = (TextView) view.findViewById(R.id.accept);
        ImageView picture = (ImageView) view.findViewById(R.id.picture);
        if (itemModel.getTitleUrl() != null) {
            Picasso.with(getActivity()).load(itemModel.getTitleUrl()).into(picture);
        } else {
            Picasso.with(getActivity()).load(R.drawable.placeholder_proposal_item).into(picture);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            title.setTransitionName(String.format("title%s", itemModel.getId()));
            price.setTransitionName(String.format("price%s", itemModel.getId()));
            accept.setTransitionName(String.format("accept%s", itemModel.getId()));
            picture.setTransitionName(String.format("picture%s", itemModel.getId()));
        }
        return view;
    }
}
