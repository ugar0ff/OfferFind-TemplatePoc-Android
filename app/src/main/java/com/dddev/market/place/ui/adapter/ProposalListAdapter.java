package com.dddev.market.place.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ugar on 11.02.16.
 */
public class ProposalListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private List<Bids.ModelBids> list;
    private Context context;
    private View.OnClickListener clickListener;
    private int status;

    public ProposalListAdapter(Context context, List<Bids.ModelBids> list, View.OnClickListener clickListener) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.clickListener = clickListener;
        status = 0;
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

    public void setStatus(int status) {
        this.status = status;
    }

    public class ViewHolder {
        public TextView title, description, price, accept;
        public ImageView picture;
    }

//    public class FooterViewHolder {
//        public ProgressBar progressBar;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (getItemViewType(position) == 0) {
            ViewHolder viewHolder;
            if (convertView == null) {// || convertView.getTag() instanceof FooterViewHolder) {
                convertView = mInflater.inflate(R.layout.item_proposal, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.description = (TextView) convertView.findViewById(R.id.description);
                viewHolder.price = (TextView) convertView.findViewById(R.id.price);
                viewHolder.picture = (ImageView) convertView.findViewById(R.id.picture);
                viewHolder.accept = (TextView) convertView.findViewById(R.id.accept);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (list != null) {
                if (list.get(position).getTitle() != null) {
                    viewHolder.title.setText(list.get(position).getTitle());
                } else {
                    viewHolder.title.setText("");
                }
                if (list.get(position).getDescription() != null) {
                    viewHolder.description.setText(list.get(position).getDescription());
                } else {
                    viewHolder.description.setText("");
                }

                String priceText;
                if (list.get(position).getPrice() == 0) {
                    priceText = "$ --";
                } else {
                    priceText = String.format("$ %s", list.get(position).getPrice());
                }
                if (status == 2) {
                    viewHolder.accept.setVisibility(View.GONE);
                    if (list.get(position).getState() == status) {
                        viewHolder.price.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                        priceText = priceText + " " + context.getString(R.string.complete);
                    } else {
                        viewHolder.price.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                    }
                } else
                if (status == 1) {
                    if (list.get(position).getState() == status) {
                        viewHolder.price.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                        priceText = priceText + " " + context.getString(R.string.awarded);
                        viewHolder.accept.setText(context.getString(R.string.complete));
                        viewHolder.accept.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.price.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                        viewHolder.accept.setText(context.getString(R.string.complete));
                        viewHolder.accept.setVisibility(View.GONE);
                    }
                } else {
                    viewHolder.price.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                    viewHolder.accept.setText(context.getString(R.string.accept));
                    viewHolder.accept.setTag(R.string.tag_status, list.get(position).getId());
                    viewHolder.accept.setVisibility(View.VISIBLE);
                }
                viewHolder.price.setText(priceText);

                if (list.get(position).getUrl() != null && !list.get(position).getUrl().isEmpty()) {
                    Picasso.with(context).load(list.get(position).getUrl()).into(viewHolder.picture);
                } else {
                    Picasso.with(context).load(R.drawable.placeholder_proposal_item).into(viewHolder.picture);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.title.setTransitionName(String.format("title%s", list.get(position).getId()));
                    viewHolder.price.setTransitionName(String.format("price%s", list.get(position).getId()));
                    viewHolder.accept.setTransitionName(String.format("accept%s", list.get(position).getId()));
                    viewHolder.picture.setTransitionName(String.format("picture%s", list.get(position).getId()));
                }
                viewHolder.accept.setOnClickListener(clickListener);
                viewHolder.accept.setTag(list.get(position).getId());
            }
//        } else {
//            FooterViewHolder viewHolder;
//            if (convertView == null || convertView.getTag() instanceof ViewHolder) {
//                convertView = mInflater.inflate(R.layout.footer_proposal_list, parent, false);
//                viewHolder = new FooterViewHolder();
//                viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (FooterViewHolder) convertView.getTag();
//            }
//            if (list != null) {
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, list.get(position).getFooterHeight());
//                params.gravity = Gravity.CENTER;
//                viewHolder.progressBar.setLayoutParams(params);
//                convertView.setOnClickListener(null);
//            }
//        }
        return convertView;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position != list.size() - 1) {
//            return 0;
//        } else {
//            return 1;
//        }
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }
}
