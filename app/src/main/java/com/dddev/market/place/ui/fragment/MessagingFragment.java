package com.dddev.market.place.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dddev.market.place.R;
import com.dddev.market.place.ui.activity.NewOrdersActivity;
import com.dddev.market.place.ui.adapter.MessagingAdapter;
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.dddev.market.place.ui.model.MessagingItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ugar on 09.02.16.
 */
public class MessagingFragment extends BaseFragment implements View.OnClickListener {

    private List<MessagingItemModel> adapterList;
    private MessagingAdapter adapter;

    public static MessagingFragment newInstance() {
        return new MessagingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messaging, container, false);
        view.findViewById(R.id.new_orders).setOnClickListener(this);
        ListView listView = (ListView) view.findViewById(R.id.list);
        adapter = new MessagingAdapter(getActivity(), adapterList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_orders:
                NewOrdersActivity.launch(getActivity());
                break;
        }
    }
}
