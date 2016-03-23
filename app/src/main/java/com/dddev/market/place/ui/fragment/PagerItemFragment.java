package com.dddev.market.place.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.dddev.market.place.ui.model.PagerItemModel;
import com.dddev.market.place.ui.views.eventsource_android.MessageEvent;
import com.squareup.picasso.Picasso;

/**
 * Created by ugar on 10.02.16.
 */
public class PagerItemFragment extends BaseFragment {

    private final static String ITEM_MODEL = "item_model";
    private PagerItemModel itemModel;

    public static PagerItemFragment newInstance(PagerItemModel itemModel) {
        PagerItemFragment fragment = new PagerItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEM_MODEL, itemModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemModel = getArguments().getParcelable(ITEM_MODEL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager_item, container, false);
        setViewData(view);
        return view;
    }

    private void setViewData(View view) {
        ((TextView) view.findViewById(R.id.title)).setText(itemModel.getTitle());
        ((TextView) view.findViewById(R.id.description)).setText(itemModel.getDescription());
        ImageView picture = (ImageView) view.findViewById(R.id.picture);
        if (itemModel.getImageUrl() != null && itemModel.getImageUrl().length() > 5) {
            Picasso.with(getActivity()).load(itemModel.getImageUrl()).into(picture);
        } else {
            Picasso.with(getActivity()).load(R.drawable.icon_view_pager).into(picture);
        }
    }

    @Override
    public void onStreamMessage(Messages.ModelMessages message) {

    }
}
