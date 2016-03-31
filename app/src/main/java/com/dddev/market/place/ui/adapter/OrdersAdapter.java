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
import com.dddev.market.place.core.api.strongloop.Opportunities;
import com.dddev.market.place.utils.StaticKeys;
import com.dddev.market.place.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by ugar on 22.02.16.
 */
public class OrdersAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private List<Opportunities.ModelOpportunity> list;
    private Context context;

    public OrdersAdapter(Context context, List<Opportunities.ModelOpportunity> list) {
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
        public TextView title, date, state;
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
                viewHolder.date.setText(Utilities.output.format(date));
            } else {
                viewHolder.date.setText("");
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
