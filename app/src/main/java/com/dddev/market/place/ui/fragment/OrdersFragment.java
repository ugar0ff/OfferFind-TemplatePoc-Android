package com.dddev.market.place.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Opportunities;
import com.dddev.market.place.ui.activity.NewOrdersActivity;
import com.dddev.market.place.ui.activity.ProposalActivity;
import com.dddev.market.place.ui.adapter.OrdersAdapter;
import com.dddev.market.place.ui.fragment.base.BaseFragment;
import com.dddev.market.place.ui.model.OrdersItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ugar on 10.02.16.
 */
public class OrdersFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<Opportunities.ModelOpportunity> adapterList;
    private OrdersAdapter adapter;
    private final static String OPPORTUNITIES_LIST = "opportunities_list";

    public static OrdersFragment newInstance(ArrayList<Opportunities.ModelOpportunity> opportunitiesList) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(OPPORTUNITIES_LIST, opportunitiesList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterList = new ArrayList<>();
        if (getArguments() != null) {
            if (getArguments().getParcelableArrayList(OPPORTUNITIES_LIST) != null) {
                ArrayList<Opportunities.ModelOpportunity> opportunitiesList = getArguments().getParcelableArrayList(OPPORTUNITIES_LIST);
                if (opportunitiesList != null) {
                    for (Opportunities.ModelOpportunity modelOpportunity : opportunitiesList) {
                        adapterList.add(modelOpportunity);
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        view.findViewById(R.id.new_orders).setOnClickListener(this);
        ListView listView = (ListView) view.findViewById(R.id.list);
        adapter = new OrdersAdapter(getActivity(), adapterList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProposalActivity.launch(getActivity(), id);
    }
}
