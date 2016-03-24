package com.dddev.market.place.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.utils.PreferencesUtils;
import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.List;


public class ChatAdapter extends ArrayAdapter<Messages.ModelMessages> {

    private LayoutInflater inflater;
    private int currentUserId;

    public ChatAdapter(Activity context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        currentUserId = PreferencesUtils.getUserId(context);
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

        setAlignment(holder, chatMessage.getSenderId() == currentUserId);
        if (holder.txtMessage != null) {
            holder.txtMessage.setText(chatMessage.getText());
        }
        return convertView;
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
