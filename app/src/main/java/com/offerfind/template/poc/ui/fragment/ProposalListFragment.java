package com.offerfind.template.poc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.adapter.ProposalListAdapter;
import com.offerfind.template.poc.ui.fragment.base.BaseFragment;
import com.offerfind.template.poc.ui.model.ProposalItemModel;
import com.offerfind.template.poc.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ugar on 10.02.16.
 */
public class ProposalListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private List<ProposalItemModel> adapterList;
    private ProposalListAdapter adapter;
    private ListView listView;

    public static ProposalListFragment newInstance() {
        return new ProposalListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_proposal_list, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        adapter = new ProposalListAdapter(getActivity(), adapterList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        if (adapterList.size() == 0) {
            listView.post(new Runnable() {
                @Override
                public void run() {
                    adapterList.add(new ProposalItemModel(1, "reree", "", view.getBottom() - view.getTop()));
                    adapter.notifyDataSetChanged();
                }
            });
        }
        view.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float itemsHeight = (adapterList.size() * (getResources().getDimension(R.dimen.proposal_item_height) + listView.getDividerHeight()));
                itemsHeight = itemsHeight % 0 == 0 ? itemsHeight : itemsHeight - 1;
                adapterList.add(new ProposalItemModel(1, "reree", "", getFooterHeight((int) itemsHeight)));
                adapter.notifyDataSetChanged();
            }
        });
        view.findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterList.size() > 1) {
                    adapterList.remove(0);
                }
                float itemsHeight = ((adapterList.size() - 1) * (getResources().getDimension(R.dimen.proposal_item_height) + listView.getDividerHeight()));
                itemsHeight = itemsHeight % 0 == 0 ? itemsHeight : itemsHeight - 1;
                adapterList.get(adapterList.size() - 1).setFooterHeight(getFooterHeight((int) itemsHeight));
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    private int getFooterHeight(int itemsHeight) {
        int footerMinHeight = (int) Utilities.convertDpToPixel(100, getActivity());
        if (getView() != null) {
            int listHeight = getView().getHeight();
            if (adapterList.size() > 0) {
                if (itemsHeight < listHeight && footerMinHeight < listHeight - itemsHeight) {
                    footerMinHeight = listHeight - itemsHeight;
                }
            } else {
                footerMinHeight = listHeight;
            }
        }
        return footerMinHeight;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (ProposalItemModel itemModel : adapterList) {
            if (itemModel.getId() == id) {
                switchFragmentListener.switchFragment(ProposalFragment.newInstance(itemModel), true, null);
                break;
            }
        }
    }
}
