package com.dddev.market.place.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.utils.PreferencesUtils;
import com.dddev.market.place.utils.Utilities;
import com.nhaarman.listviewanimations.ArrayAdapter;


public class ChatAdapter extends ArrayAdapter<Messages.ModelMessages> {

    private LayoutInflater inflater;
    private int currentUserId;
    private Context context;

    public ChatAdapter(Activity context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        currentUserId = PreferencesUtils.getUserId(context);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Messages.ModelMessages chatMessage = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_message_list, parent, false);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (getItemViewType(position) == 0) {
            View view = new View(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)Utilities.convertDpToPixel(5, context)));
            return view;
        } else {
            setAlignment(holder, chatMessage.getSenderId() == currentUserId);
            if (holder.txtMessage != null) {
                holder.txtMessage.setText(chatMessage.getText());
            }
            return convertView;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    private void setAlignment(ViewHolder holder, boolean isOutgoing) {
        if (holder.txtMessage != null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            if (isOutgoing) {
                holder.txtMessage.setBackgroundResource(R.drawable.background_left_message);
                layoutParams.gravity = Gravity.LEFT;
                holder.txtMessage.setLayoutParams(layoutParams);
            } else {
                holder.txtMessage.setBackgroundResource(R.drawable.background_right_message);
                layoutParams.gravity = Gravity.RIGHT;
                holder.txtMessage.setLayoutParams(layoutParams);
            }
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        return holder;
    }

    private static class ViewHolder {
        public TextView txtMessage;
    }
}
