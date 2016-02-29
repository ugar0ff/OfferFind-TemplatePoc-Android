package com.dddev.market.place.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.utils.PreferencesUtils;

import java.util.List;


public class ChatAdapter extends BaseAdapter {

    private final List<Messages.ModelMessages> chatMessages;
    private LayoutInflater inflater;
    private int currentUserId;

    public ChatAdapter(Activity context, List<Messages.ModelMessages> chatMessages) {
        this.chatMessages = chatMessages;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        currentUserId = PreferencesUtils.getUserId(context);
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public Messages.ModelMessages getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Messages.ModelMessages chatMessage = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_message, parent, false);
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

    public void add(Messages.ModelMessages message) {
        chatMessages.add(message);
    }

    public void add(List<Messages.ModelMessages> messages) {
        chatMessages.addAll(messages);
    }

    private void setAlignment(ViewHolder holder, boolean isOutgoing) {
        if (!isOutgoing) {
            if (holder.txtMessage != null) {
                holder.txtMessage.setBackgroundResource(R.drawable.dialog_white);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtMessage.setLayoutParams(layoutParams);
            }
        } else {
            if (holder.txtMessage != null) {
                holder.txtMessage.setBackgroundResource(R.drawable.dialog_gray);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
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
