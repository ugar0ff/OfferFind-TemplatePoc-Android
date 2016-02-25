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
import com.dddev.market.place.ui.model.MessagingItemModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ugar on 22.02.16.
 */
public class MessagingAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private List<MessagingItemModel> list;
    private Context context;

    public MessagingAdapter(Context context, List<MessagingItemModel> list) {
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
        public TextView title, date, state, provider;
        public ImageView picture;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_orders, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.state = (TextView) convertView.findViewById(R.id.text_state);
            viewHolder.provider = (TextView) convertView.findViewById(R.id.provider);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.image_state);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (list != null) {
            viewHolder.title.setText(list.get(position).getTitle());
            viewHolder.date.setText(list.get(position).getDate());
            viewHolder.provider.setText(list.get(position).getProvider());
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
