package com.offerfind.template.poc.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.model.ProposalItemModel;

import java.util.List;

/**
 * Created by ugar on 11.02.16.
 */
public class ProposalListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private List<ProposalItemModel> list;

    public ProposalListAdapter(Context context, List<ProposalItemModel> list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public class ViewHolder {
        public TextView title;
    }

    public class FooterViewHolder {
        public ProgressBar progressBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0) {
            ViewHolder viewHolder;
            if (convertView == null || convertView.getTag() instanceof FooterViewHolder) {
                convertView = mInflater.inflate(R.layout.item_proposal, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (list != null) {
                viewHolder.title.setText(list.get(position).getTitle());
            }
        } else {
            FooterViewHolder viewHolder;
            if (convertView == null || convertView.getTag() instanceof ViewHolder) {
                convertView = mInflater.inflate(R.layout.footer_proposal_list, parent, false);
                viewHolder = new FooterViewHolder();
                viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (FooterViewHolder) convertView.getTag();
            }
            if (list != null) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, list.get(position).getFooterHeight());
                params.gravity = Gravity.CENTER;
                viewHolder.progressBar.setLayoutParams(params);
                convertView.setOnClickListener(null);
            }
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != list.size() - 1) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
}
