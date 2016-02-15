package com.offerfind.template.poc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.fragment.base.BaseFragment;
import com.offerfind.template.poc.ui.model.PagerItemModel;

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
        ((ImageView) view.findViewById(R.id.picture)).setImageResource(itemModel.getImageUrl());
    }
}
