package co.mrktplaces.android.core.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import co.mrktplaces.android.core.api.retrofit.ApiRetrofit;
import co.mrktplaces.android.core.api.strongloop.Messages;
import co.mrktplaces.android.core.cache.CacheContentProvider;
import co.mrktplaces.android.core.cache.CacheHelper;
import co.mrktplaces.android.utils.PreferencesUtils;
import co.mrktplaces.android.utils.StaticKeys;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by ugar on 11.04.16.
 */
public class PutMessageService extends IntentService {

    public PutMessageService() {
        super("PutMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            replaceMessageState(intent.getIntExtra(StaticKeys.BID_ID, 0), intent.getIntExtra(StaticKeys.MESSAGE_ID, 0));
        }
    }

    private void replaceMessageState(int bidId, int messageId) {
        if (bidId != 0) {
            readList(getMessagesList(bidId));
        } else if (messageId != 0) {
            readMessage(getMessage(messageId), PreferencesUtils.getUserId(getApplicationContext()));
        }
    }

    private void readMessage(Messages message, int userId) {
        if (!message.isRead() && message.getReceiverId() == userId) {
            try {
                Messages messagesResponse = ApiRetrofit.putMessage(message.getId(), true, PreferencesUtils.getUserToken(getApplicationContext())).execute().body();
                Timber.i(messagesResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void readList(ArrayList<Messages> messagesList) {
        int userId = PreferencesUtils.getUserId(getApplicationContext());
        if (messagesList != null) {
            for (Messages messages : messagesList) {
                readMessage(messages, userId);
            }
        }
    }

    private ArrayList<Messages> getMessagesList(int bidId) {
        ArrayList<Messages> messagesList = new ArrayList<>();
        String[] projection = new String[]{CacheHelper.MESSAGE_ID + " as " + CacheHelper._ID, CacheHelper.MESSAGE_RECEIVER_ID, CacheHelper.MESSAGE_READ, CacheHelper.MESSAGE_TEXT,
                CacheHelper.MESSAGE_CREATE_AT, CacheHelper.MESSAGE_BID_ID, CacheHelper.MESSAGE_OWNER_ID, CacheHelper.MESSAGE_SENDER_ID};
        String selection = CacheHelper.MESSAGE_BID_ID + " = ?";
        String[] selectionArg = new String[]{String.valueOf(bidId)};
        Cursor cursor = getContentResolver().query(CacheContentProvider.MESSAGE_URI, projection, selection, selectionArg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ContentValues[] contentValues = new ContentValues[cursor.getCount()];
                do {
                    Messages messages = new Messages();
                    messages.setId(cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                    messages.setReceiverId(cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_RECEIVER_ID)));
                    messages.setRead(cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_READ)) == 1);
                    messagesList.add(messages);
                    ContentValues values =  new ContentValues();
                    values.put(CacheHelper.MESSAGE_ID, cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                    values.put(CacheHelper.MESSAGE_READ, 1);
                    values.put(CacheHelper.MESSAGE_TEXT, cursor.getString(cursor.getColumnIndex(CacheHelper.MESSAGE_TEXT)));
                    values.put(CacheHelper.MESSAGE_CREATE_AT, cursor.getString(cursor.getColumnIndex(CacheHelper.MESSAGE_CREATE_AT)));
                    values.put(CacheHelper.MESSAGE_BID_ID, cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_BID_ID)));
                    values.put(CacheHelper.MESSAGE_OWNER_ID, cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_OWNER_ID)));
                    values.put(CacheHelper.MESSAGE_SENDER_ID, cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_SENDER_ID)));
                    values.put(CacheHelper.MESSAGE_RECEIVER_ID, cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_RECEIVER_ID)));
                    contentValues[cursor.getPosition()] = values;
                } while (cursor.moveToNext());
                getContentResolver().bulkInsert(CacheContentProvider.MESSAGE_URI, contentValues);
            }
            cursor.close();
        }
        return messagesList;
    }

    private Messages getMessage(int messageId) {
        Messages messages = null;
        String[] projection = new String[]{CacheHelper.MESSAGE_ID + " as " + CacheHelper._ID, CacheHelper.MESSAGE_RECEIVER_ID, CacheHelper.MESSAGE_READ};
        String selection = CacheHelper.MESSAGE_ID + " = ?";
        String[] selectionArg = new String[]{String.valueOf(messageId)};
        Cursor cursor = getContentResolver().query(CacheContentProvider.MESSAGE_URI, projection, selection, selectionArg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                messages = new Messages();
                messages.setId(cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                messages.setReceiverId(cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_RECEIVER_ID)));
                messages.setRead(cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_READ)) == 1);
            }
            cursor.close();
        }
        return messages;
    }
}
