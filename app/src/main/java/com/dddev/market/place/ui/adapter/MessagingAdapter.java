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
import com.dddev.market.place.utils.StaticKeys;
import com.dddev.market.place.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
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
            viewHolder.createAt = (TextView) convertView.findViewById(R.id.createdAt);
            viewHolder.state = (TextView) convertView.findViewById(R.id.text_state);
            viewHolder.provider = (TextView) convertView.findViewById(R.id.owner);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.image_state);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (list != null) {
            if (list.get(position).getTitle() != null) {
                viewHolder.title.setText(list.get(position).getTitle());
            }
            Date date = null;
            try {
                date = Utilities.sdf.parse(list.get(position).getCreatedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                viewHolder.createAt.setText(Utilities.output.format(date));
            } else {
                viewHolder.createAt.setText("");
            }
            if (list.get(position).getOwner() != null) {
                viewHolder.provider.setText(list.get(position).getOwner().getName());
            }
            switch (list.get(position).getState()) {
                case StaticKeys.State.PUBLISHED:
                    viewHolder.state.setText(context.getString(R.string.selecting));
                    viewHolder.state.setTextColor(ContextCompat.getColor(context, R.color.colorStateYellow));
                    Picasso.with(context).load(R.drawable.icon_proposal_selecting).into(viewHolder.picture);
                    break;
                case StaticKeys.State.ACCEPTED:
                    viewHolder.state.setText(context.getString(R.string.awarded));
                    viewHolder.state.setTextColor(ContextCompat.getColor(context, R.color.colorStateRed));
                    Picasso.with(context).load(R.drawable.icon_proposal_awarded).into(viewHolder.picture);
                    break;
                case StaticKeys.State.CLOSED:
                    viewHolder.state.setText(context.getString(R.string.complete));
                    viewHolder.state.setTextColor(ContextCompat.getColor(context, R.color.colorStateGreen));
                    Picasso.with(context).load(R.drawable.icon_proposal_complite).into(viewHolder.picture);
                    break;
                default:
                    viewHolder.state.setText(context.getString(R.string.selecting));
                    viewHolder.state.setTextColor(ContextCompat.getColor(context, R.color.colorStateYellow));
                    Picasso.with(context).load(R.drawable.icon_proposal_selecting).into(viewHolder.picture);
            }
        }
        return convertView;
    }
}
