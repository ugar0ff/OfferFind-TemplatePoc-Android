package co.mrktplaces.android.core.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import co.mrktplaces.android.core.AppOfferFind;
import co.mrktplaces.android.core.api.strongloop.Account;
import co.mrktplaces.android.core.api.strongloop.AccountGetRepository;
import co.mrktplaces.android.core.cache.CacheContentProvider;
import co.mrktplaces.android.core.cache.CacheHelper;
import co.mrktplaces.android.ui.views.eventsource_android.EventSource;
import co.mrktplaces.android.ui.views.eventsource_android.EventSourceHandler;
import co.mrktplaces.android.ui.views.eventsource_android.MessageEvent;
import co.mrktplaces.android.utils.PreferencesUtils;
import co.mrktplaces.android.utils.StaticKeys;

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
                    eventSource = new EventSource(URI.create(AppOfferFind.API + "Accounts/streamUpdates?&_format=event-stream&access_token="
                            + PreferencesUtils.getUserToken(getBaseContext())), new SSEHandler(), null, true);
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
            Timber.v("SSE SSEHandler");
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
            Timber.v("SSE Messages %s", event);
            Timber.v("SSE Messages: %s", message.lastEventId);
            Timber.v("SSE Messages: %s", message.data);
            Timber.v("SSE hashCode: %s, eventSource: %s", hashCode(), eventSource.hashCode());
            if (message.getMessageData() == null || message.getMessageData().getClassName() == null || message.getMessageData().getData() == null) {
                return;
            }
            if (message.getMessageData().getClassName().equals("Opportunity")) {
                updateOpportunities(message);
            } else if (message.getMessageData().getClassName().equals("Bid")) {
                updateBid(message);
            } else if (message.getMessageData().getClassName().equals("Message")) {
                if (!message.getMessageData().getType().equals(StaticKeys.StreamType.UPDATE)) {
                    updateMessage(message);
                }
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
        values.put(CacheHelper.OPPORTUNITIES_ADDRESS, message.getMessageData().getData().getLocation() == null ? null : message.getMessageData().getData().getLocation().getAddress());
        values.put(CacheHelper.OPPORTUNITIES_LATITUDE, message.getMessageData().getData().getLocation() == null ? null : message.getMessageData().getData().getLocation().getLatitude());
        values.put(CacheHelper.OPPORTUNITIES_LONGITUDE, message.getMessageData().getData().getLocation() == null ? null : message.getMessageData().getData().getLocation().getLongitude());
        getBaseContext().getContentResolver().insert(CacheContentProvider.OPPORTUNITIES_URI, values);
        updateOwner(message.getMessageData().getData().getOwnerId());
    }

    private void updateBid(MessageEvent message) {
        ContentValues values = new ContentValues();
        values.put(CacheHelper.BIDS_ID, message.getMessageData().getData().getId());
        values.put(CacheHelper.BIDS_TITLE, getBidsTitle(message.getMessageData().getData().getOpportunityId()));
        values.put(CacheHelper.BIDS_DESCRIPTION, message.getMessageData().getData().getDescription());
        values.put(CacheHelper.BIDS_OPPORTUNITIES_ID, message.getMessageData().getData().getOpportunityId());
        values.put(CacheHelper.BIDS_PRICE, message.getMessageData().getData().getPrice());
        values.put(CacheHelper.BIDS_STATUS, message.getMessageData().getData().getState());
        values.put(CacheHelper.BIDS_CREATE_AT, message.getMessageData().getData().getCreatedAt());
        values.put(CacheHelper.BIDS_OWNER_ID, message.getMessageData().getData().getOwnerId());
        getBaseContext().getContentResolver().insert(CacheContentProvider.BIDS_URI, values);
        updateOwner(message.getMessageData().getData().getOwnerId());
    }

    private void updateMessage(MessageEvent message) {
        ContentValues values = new ContentValues();
        values.put(CacheHelper.MESSAGE_ID, message.getMessageData().getData().getId());
        values.put(CacheHelper.MESSAGE_TEXT, message.getMessageData().getData().getText());
        values.put(CacheHelper.MESSAGE_CREATE_AT, message.getMessageData().getData().getCreatedAt());
        values.put(CacheHelper.MESSAGE_BID_ID, message.getMessageData().getData().getBidId());
        values.put(CacheHelper.MESSAGE_OWNER_ID, message.getMessageData().getData().getOwnerId());
        values.put(CacheHelper.MESSAGE_SENDER_ID, message.getMessageData().getData().getSenderId());
        values.put(CacheHelper.MESSAGE_READ, message.getMessageData().getData().isRead() ? 1 : 0);
        values.put(CacheHelper.MESSAGE_RECEIVER_ID, message.getMessageData().getData().getReceiverId());
        getBaseContext().getContentResolver().insert(CacheContentProvider.MESSAGE_URI, values);
    }

    private void updateOwner(final int ownerId) {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final AccountGetRepository repository = AppOfferFind.getRestAdapter(getApplicationContext()).createRepository(AccountGetRepository.class);
                repository.createContract();
                repository.accounts(ownerId, new AccountGetRepository.UserCallback() {
                    @Override
                    public void onSuccess(Account account) {
                        if (account != null) {
                            Timber.i("onSuccess response=%s", account.toString());
                            ContentValues values = new ContentValues();
                            values.put(CacheHelper.OWNER_ID, account.getId());
                            values.put(CacheHelper.OWNER_AVATAR, account.getAvatar());
                            values.put(CacheHelper.OWNER_NAME, account.getName());
                            getBaseContext().getContentResolver().insert(CacheContentProvider.OWNER_URI, values);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e("onError Throwable: %s", t.toString());
                    }
                });
            }
        });
    }

    private String getBidsTitle(int opportunitiesId) {
        String title = null;
        String[] projection = new String[]{CacheHelper.OPPORTUNITIES_ID + " as " + CacheHelper._ID, CacheHelper.OPPORTUNITIES_TITLE};
        String selection = CacheHelper.OPPORTUNITIES_ID + " =? ";
        String[] selectionArg = new String[]{String.valueOf(opportunitiesId)};
        Cursor cursor = getContentResolver().query(CacheContentProvider.OPPORTUNITIES_URI, projection, selection, selectionArg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                title = cursor.getString(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_TITLE));
            }
            cursor.close();
        }
        return title;
    }

}
