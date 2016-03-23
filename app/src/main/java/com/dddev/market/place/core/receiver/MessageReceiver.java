package com.dddev.market.place.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.utils.StaticKeys;

/**
 * Created by ugar on 22.03.16.
 */
public class MessageReceiver extends BroadcastReceiver {
    public static final String BROADCAST_ACTION = "com.dddev.market.place.core.receiver.message";

    private MessageBroadcastListener messageBroadcastListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Messages.ModelMessages message = intent.getParcelableExtra(StaticKeys.KEY_MESSAGE);
            if (message != null) {
                messageBroadcastListener.onHandleServerRequest(message);
            } else {
                messageBroadcastListener.onHandleServerRequestError();
            }
        }
    }

    public void setMessageBroadcastListener(MessageBroadcastListener messageBroadcastListener) {
        this.messageBroadcastListener = messageBroadcastListener;
    }

    public interface MessageBroadcastListener {
        void onHandleServerRequest(Messages.ModelMessages message);
        void onHandleServerRequestError();
    }
}
