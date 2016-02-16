package com.offerfind.template.poc.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
                    adapterList.add(new ProposalItemModel(1, "Reree", "asrfewagqgeqw", view.getBottom() - view.getTop() - (int)(2*getResources().getDimension(R.dimen.proposal_list_divider_height)), null, 100));
                    adapter.notifyDataSetChanged();
                }
            });
        }
        view.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float itemsHeight = (adapterList.size() * (getResources().getDimension(R.dimen.proposal_item_height) + 2*getResources().getDimension(R.dimen.proposal_list_divider_height)))
                        + 2*getResources().getDimension(R.dimen.proposal_list_divider_height);
                itemsHeight = itemsHeight % 0 == 0 ? itemsHeight : itemsHeight - 1;
                adapterList.add(0, new ProposalItemModel(adapterList.size() + 1, "Geree" + (adapterList.size() + 1), "qerfgdsgshtjtreaqetf afgeqfvefvqefqev efqeqfreqrsdafa", getFooterHeight((int) itemsHeight), null, 100));
                adapterList.get(adapterList.size() - 1).setFooterHeight(getFooterHeight((int) itemsHeight));
                adapter.notifyDataSetChanged();
            }
        });
        view.findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterList.size() > 1) {
                    adapterList.remove(0);
                }
                float itemsHeight = ((adapterList.size() - 1) * (getResources().getDimension(R.dimen.proposal_item_height) + 2*getResources().getDimension(R.dimen.proposal_list_divider_height)))
                        + 2*getResources().getDimension(R.dimen.proposal_list_divider_height);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Transition changeTransform = TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_transform);
                    Transition explodeTransform = TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade);
                    setSharedElementReturnTransition(changeTransform);
                    setExitTransition(explodeTransform);

                    ProposalFragment proposalFragment = ProposalFragment.newInstance(itemModel);

                    proposalFragment.setSharedElementEnterTransition(changeTransform);
                    proposalFragment.setEnterTransition(explodeTransform);

                    ImageView picture = (ImageView) view.findViewById(R.id.picture);
                    TextView title = (TextView) view.findViewById(R.id.title);
                    TextView price = (TextView) view.findViewById(R.id.price);
                    TextView accept = (TextView) view.findViewById(R.id.accept);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack("transaction");
                    ft.replace(R.id.container, proposalFragment);
                    ft.addSharedElement(picture, String.format("picture%s", itemModel.getId()));
                    ft.addSharedElement(title, String.format("title%s", itemModel.getId()));
                    ft.addSharedElement(price, String.format("price%s", itemModel.getId()));
                    ft.addSharedElement(accept, String.format("accept%s", itemModel.getId()));
                    ft.commit();
                } else {
                    switchFragmentListener.switchFragment(ProposalFragment.newInstance(itemModel), true, null);
                }
                break;
            }
        }
    }
}
