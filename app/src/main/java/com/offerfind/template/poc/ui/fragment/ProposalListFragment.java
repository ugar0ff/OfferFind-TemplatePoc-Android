package com.offerfind.template.poc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.fragment.base.BaseFragment;

/**
 * Created by ugar on 10.02.16.
 */
public class ProposalListFragment extends BaseFragment {

    public static ProposalListFragment newInstance() {
        return new ProposalListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposal_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list);
//        listView.addFooterView(view.findViewById(R.id.progress_bar));
        return view;
    }
}
