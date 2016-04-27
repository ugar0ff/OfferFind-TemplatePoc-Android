package com.dddev.market.place.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.ui.activity.MapsActivity;
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.dddev.market.place.ui.model.PagerItemModel;
import com.dddev.market.place.utils.StaticKeys;
import com.squareup.picasso.Picasso;

/**
 * Created by ugar on 10.02.16.
 */
public class PagerItemFragment extends BaseFragment implements View.OnClickListener{

    public final static String ITEM_MODEL = "item_model";
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
        if (getArguments() != null && itemModel == null) {
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
        TextView location = (TextView) view.findViewById(R.id.locationText);
        if (itemModel.getAddress() == null || itemModel.getAddress().length() == 0) {
            location.setVisibility(View.GONE);
        } else {
            location.setText(itemModel.getAddress());
            location.setVisibility(View.VISIBLE);
        }
        ImageButton mapButton = (ImageButton) view.findViewById(R.id.maps);
        Switch switchTest = (Switch) view.findViewById(R.id.switchTest);
        mapButton.setOnClickListener(this);
        switch (itemModel.getType()) {
            case StaticKeys.CategoryType.MAP:
                mapButton.setVisibility(View.VISIBLE);
                switchTest.setVisibility(View.GONE);
                break;
            case StaticKeys.CategoryType.CHECKED:
                mapButton.setVisibility(View.GONE);
                switchTest.setVisibility(View.VISIBLE);
                break;
            default:
                mapButton.setVisibility(View.GONE);
                switchTest.setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maps:
                MapsActivity.launch(getActivity());
                break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (getArguments() != null && itemModel == null) {
//                itemModel = getArguments().getParcelable(ITEM_MODEL);
//            }
//            itemModel.setAddress(data.getExtras().getString(StaticKeys.MAP_ADDRESS));
//            itemModel.setLatitude(data.getExtras().getDouble(StaticKeys.MAP_LATITUDE, 0));
//            itemModel.setLongitude(data.getExtras().getDouble(StaticKeys.MAP_LONGITUDE, 0));
//        }
//    }


}
