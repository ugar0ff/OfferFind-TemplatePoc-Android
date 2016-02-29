package com.dddev.market.place.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ugar on 22.02.16.
 */
public class MessagingAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private List<Bids.ModelBids> list;
    private Context context;

    public MessagingAdapter(Context context, List<Bids.ModelBids> list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
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
        public TextView title, createAt, state, provider;
        public ImageView picture;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_message, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.createAt = (TextView) convertView.findViewById(R.id.createAt);
            viewHolder.state = (TextView) convertView.findViewById(R.id.text_state);
            viewHolder.provider = (TextView) convertView.findViewById(R.id.provider);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.image_state);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (list != null) {
            if (list.get(position).getTitle() != null) {
                viewHolder.title.setText(list.get(position).getTitle());
            }
            viewHolder.createAt.setText(String.valueOf(list.get(position).getDate()));
            if (list.get(position).getProvider() != null) {
                viewHolder.provider.setText(list.get(position).getProvider());
            }
            switch (list.get(position).getState()) {
                case 0:
                    viewHolder.state.setText(context.getString(R.string.awarded));
                    viewHolder.state.setTextColor(ContextCompat.getColor(context, R.color.colorStateRed));
                    Picasso.with(context).load(R.drawable.icon_proposal_awarded).into(viewHolder.picture);
                    break;
                case 1:
                    viewHolder.state.setText(context.getString(R.string.complete));
                    viewHolder.state.setTextColor(ContextCompat.getColor(context, R.color.colorStateYellow));
                    Picasso.with(context).load(R.drawable.icon_proposal_complite).into(viewHolder.picture);
                    break;
                case 2:
                    viewHolder.state.setText(context.getString(R.string.selecting));
                    viewHolder.state.setTextColor(ContextCompat.getColor(context, R.color.colorStateGreen));
                    Picasso.with(context).load(R.drawable.icon_proposal_selecting).into(viewHolder.picture);
                    break;
                default:
                    viewHolder.state.setText(context.getString(R.string.selecting));
                    viewHolder.state.setTextColor(ContextCompat.getColor(context, R.color.colorStateGreen));
                    Picasso.with(context).load(R.drawable.icon_proposal_selecting).into(viewHolder.picture);
            }
        }
        return convertView;
    }
}
