package com.dddev.market.place.core.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.core.cache.CacheContentProvider;
import com.dddev.market.place.core.cache.CacheHelper;
import com.dddev.market.place.core.receiver.MessageReceiver;
import com.dddev.market.place.ui.views.eventsource_android.EventSource;
import com.dddev.market.place.ui.views.eventsource_android.EventSourceHandler;
import com.dddev.market.place.ui.views.eventsource_android.MessageEvent;
import com.dddev.market.place.utils.PreferencesUtils;
import com.dddev.market.place.utils.StaticKeys;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;

import timber.log.Timber;

/**
 * Created by ugar on 22.03.16.
 */
public class StreamService extends Service {

    private EventSource eventSource;

    public StreamService() {
        Timber.v("StreamService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Timber.v("onBind");
        if (eventSource == null || !eventSource.isConnected()) {
            streamMessagesConnect();
        }
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.v("onStartCommand");
        return Service.START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Timber.v("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Timber.v("onRebind");
        super.onRebind(intent);
    }

    private void streamMessagesConnect() {
        Thread eventThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (getBaseContext() != null) {
                    eventSource = new EventSource(URI.create(AppOfferFind.API + "Accounts/streamUpdates?_format=event-stream&access_token=" + PreferencesUtils.getUserToken(getBaseContext())), new SSEHandler(), null, true);
                    eventSource.connect();
                }
            }
        });
        eventThread.start();
    }

    @Override
    public void onDestroy() {
        Timber.v("onDestroy");
        if (eventSource != null && eventSource.isConnected()) {
            eventSource.close();
        }
        super.onDestroy();
    }

    private class SSEHandler implements EventSourceHandler {

        public SSEHandler() {
        }

        @Override
        public void onConnect() {
            Timber.v("SSE Connected");
        }

        @Override
        public void onMessage(String event, MessageEvent message) {
            if (message == null) {
                return;
            }
            Timber.v("SSE Message %s", event);
            Timber.v("SSE Message: %s", message.lastEventId);
            Timber.v("SSE Message: %s", message.data);
            Timber.v("SSE hashCode: %s, eventSource: %s", hashCode(), eventSource.hashCode());
            if (message.getMessageData() == null || message.getMessageData().getClassName() == null || message.getMessageData().getData() == null) {
                return;
            }
            if (message.getMessageData().getClassName().equals("Opportunity")) {
                updateOpportunities(message);
            } else if (message.getMessageData().getClassName().equals("Bid")) {
                updateBid(message);
            } else if (message.getMessageData().getClassName().equals("Message")) {
                sendMessageCallBack(message.getMessageData().getData());
            }
        }

        @Override
        public void onComment(String comment) {
            //comments only received if exposeComments turned on
            Timber.v("SSE Comment %s", comment);
        }

        @Override
        public void onError(Throwable t) {
            Timber.v("SSE Error");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            Timber.v("SSE Stacktrace %s", sw.toString());

        }

        @Override
        public void onClosed(boolean willReconnect) {
            Timber.v("SSE Closed reconnect? %s", willReconnect);
        }
    }

    private void updateOpportunities(MessageEvent message) {
        ContentValues values = new ContentValues();
        values.put(CacheHelper.OPPORTUNITIES_ID, message.getMessageData().getData().getId());
        values.put(CacheHelper.OPPORTUNITIES_TITLE, message.getMessageData().getData().getTitle());
        values.put(CacheHelper.OPPORTUNITIES_DESCRIPTION, message.getMessageData().getData().getDescription());
        values.put(CacheHelper.OPPORTUNITIES_ACCOUNT_ID, message.getMessageData().getData().getOwnerId());
        values.put(CacheHelper.OPPORTUNITIES_CREATE_AT, message.getMessageData().getData().getCreatedAt());
        values.put(CacheHelper.OPPORTUNITIES_CATEGORY_ID, message.getMessageData().getData().getCategoryId());
        values.put(CacheHelper.OPPORTUNITIES_STATUS, message.getMessageData().getData().getState());
        getBaseContext().getContentResolver().insert(CacheContentProvider.OPPORTUNITIES_URI, values);
    }

    private void updateBid(MessageEvent message) {
        ContentValues values = new ContentValues();
        values.put(CacheHelper.BIDS_ID, message.getMessageData().getData().getId());
        values.put(CacheHelper.BIDS_TITLE, message.getMessageData().getData().getTitle());
        values.put(CacheHelper.BIDS_DESCRIPTION, message.getMessageData().getData().getDescription());
        values.put(CacheHelper.BIDS_OPPORTUNITIES_ID, message.getMessageData().getData().getOpportunityId());
        values.put(CacheHelper.BIDS_PRICE, message.getMessageData().getData().getPrice());
        values.put(CacheHelper.BIDS_URL, message.getMessageData().getData().getUrl());
        values.put(CacheHelper.BIDS_STATUS, message.getMessageData().getData().getState());
        values.put(CacheHelper.BIDS_CREATE_AT, message.getMessageData().getData().getCreatedAt());
        //TODO: addModel provider model
        getBaseContext().getContentResolver().insert(CacheContentProvider.BIDS_URI, values);
    }

    private void sendMessageCallBack(Messages.ModelMessages message) {
        Intent intent = new Intent(MessageReceiver.BROADCAST_ACTION);
        intent.putExtra(StaticKeys.KEY_MESSAGE, message);
        sendBroadcast(intent);
    }

}
