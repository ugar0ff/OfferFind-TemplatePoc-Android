package co.mrktplaces.android.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import co.mrktplaces.android.R;
import co.mrktplaces.android.core.api.strongloop.Opportunities;
import co.mrktplaces.android.utils.Utilities;

/**
 * Created by ugar on 29.04.16.
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
        public TextView name, date, address, description;
        public ImageView picture;
        public EditText price, message;
        public Button apply, skip;
        public TextWatcher messageWatcher, priceWatcher;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_orders, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.picture);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.price = (EditText) convertView.findViewById(R.id.price);
            viewHolder.message = (EditText) convertView.findViewById(R.id.message);
            viewHolder.apply = (Button) convertView.findViewById(R.id.btnApply);
            viewHolder.skip = (Button) convertView.findViewById(R.id.btnSkip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (list != null) {
            if (list.get(position).getAccounts().getName() != null) {
                viewHolder.name.setText(list.get(position).getAccounts().getName());
            } else {
                viewHolder.name.setText("");
            }
            if (list.get(position).getDescription() != null) {
                viewHolder.description.setText(list.get(position).getDescription());
            } else {
                viewHolder.description.setText("");
            }
            if (list.get(position).getLocation().getAddress() != null) {
                viewHolder.address.setText(list.get(position).getLocation().getAddress());
            } else {
                viewHolder.address.setText("");
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
            if (list.get(position).getAccounts().getAvatar() != null && list.get(position).getAccounts().getAvatar().length() > 3) {
                Picasso.with(context).load(list.get(position).getAccounts().getAvatar()).placeholder(R.drawable.placeholder_user_photo).into(viewHolder.picture);
            } else {
                Picasso.with(context).load(R.drawable.placeholder_user_photo).into(viewHolder.picture);
            }

            viewHolder.message.setTag(position);
            if (viewHolder.messageWatcher != null) {
                viewHolder.message.removeTextChangedListener(viewHolder.messageWatcher);
            }
            viewHolder.messageWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(position).setMessage(String.valueOf(s));
                }
            };
            if (list.get(position).getMessage() != null) {
                viewHolder.message.setText(list.get(position).getMessage());
            } else {
                viewHolder.message.setText("");
            }
            viewHolder.message.addTextChangedListener(viewHolder.messageWatcher);

            viewHolder.price.setTag(position);
            if (viewHolder.priceWatcher != null) {
                viewHolder.price.removeTextChangedListener(viewHolder.priceWatcher);
            }
            viewHolder.priceWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(position).setPrice(String.valueOf(s));
                }
            };
            if (list.get(position).getPrice() != null) {
                viewHolder.price.setText(list.get(position).getPrice());
            } else {
                viewHolder.price.setText("");
            }
            viewHolder.price.addTextChangedListener(viewHolder.priceWatcher);
        }
        return convertView;
    }
}
