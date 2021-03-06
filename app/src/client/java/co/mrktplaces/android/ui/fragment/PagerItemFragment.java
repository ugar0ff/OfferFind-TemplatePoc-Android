package co.mrktplaces.android.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.mrktplaces.android.R;
import co.mrktplaces.android.ui.activity.MapsActivity;
import co.mrktplaces.android.ui.fragment.base.BaseFragment;
import co.mrktplaces.android.ui.model.PagerItemModel;
import co.mrktplaces.android.utils.StaticKeys;
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
            location.setVisibility(View.INVISIBLE);
        } else {
            location.setText(itemModel.getAddress());
            location.setVisibility(View.VISIBLE);
        }
        LinearLayout mapLayout = (LinearLayout) view.findViewById(R.id.mapsLayout);
        LinearLayout switchTestLayout = (LinearLayout) view.findViewById(R.id.switchTestLayout);
        view.findViewById(R.id.maps).setOnClickListener(this);
        switch (itemModel.getType()) {
            case StaticKeys.CategoryType.MAP:
                mapLayout.setVisibility(View.VISIBLE);
                switchTestLayout.setVisibility(View.GONE);
                break;
            case StaticKeys.CategoryType.CHECKED:
                mapLayout.setVisibility(View.GONE);
                switchTestLayout.setVisibility(View.VISIBLE);
                break;
            default:
                mapLayout.setVisibility(View.GONE);
                switchTestLayout.setVisibility(View.GONE);

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
