package co.mrktplaces.android.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import co.mrktplaces.android.R;
import co.mrktplaces.android.core.api.strongloop.Bids;
import co.mrktplaces.android.utils.StaticKeys;
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
    private String status;

    public ProposalListAdapter(Context context, List<Bids.ModelBids> list, View.OnClickListener clickListener) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.clickListener = clickListener;
        status = StaticKeys.State.PUBLISHED;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public class ViewHolder {
        public TextView title, description, price, accept;
        public ImageView picture;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
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
            if (list.get(position).getOwner().getName() != null) {
                viewHolder.title.setText(list.get(position).getOwner().getName());
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
            switch (status) {
                case StaticKeys.State.CLOSED:
                    viewHolder.accept.setVisibility(View.GONE);
                    if (list.get(position).getState().equals(status)) {
                        viewHolder.price.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                        priceText = priceText + " " + context.getString(R.string.complete);
                    } else {
                        viewHolder.price.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                    }
                    break;
                case StaticKeys.State.ACCEPTED:
                    if (list.get(position).getState().equals(status)) {
                        viewHolder.price.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                        priceText = priceText + " " + context.getString(R.string.awarded);
                        viewHolder.accept.setText(context.getString(R.string.complete));
                        viewHolder.accept.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.price.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                        viewHolder.accept.setText(context.getString(R.string.complete));
                        viewHolder.accept.setVisibility(View.GONE);
                    }
                    break;
                default:
                    viewHolder.price.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                    viewHolder.accept.setText(context.getString(R.string.accept));
                    viewHolder.accept.setTag(R.string.tag_status, list.get(position).getId());
                    viewHolder.accept.setVisibility(View.VISIBLE);
            }
            viewHolder.price.setText(priceText);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.title.setTransitionName(String.format("title%s", list.get(position).getId()));
                viewHolder.price.setTransitionName(String.format("price%s", list.get(position).getId()));
                viewHolder.accept.setTransitionName(String.format("accept%s", list.get(position).getId()));
                viewHolder.picture.setTransitionName(String.format("picture%s", list.get(position).getId()));
            }
            if (list.get(position).getOwner() != null && list.get(position).getOwner().getAvatar() != null && list.get(position).getOwner().getAvatar().length() > 5) {
                Picasso.with(context).load(list.get(position).getOwner().getAvatar()).fit().centerInside().into(viewHolder.picture);
            } else {
                Picasso.with(context).load(R.drawable.placeholder_proposal_item).fit().centerInside().into(viewHolder.picture);
            }
            viewHolder.accept.setOnClickListener(clickListener);
            viewHolder.accept.setTag(list.get(position).getId());
        }
        return convertView;
    }
}
