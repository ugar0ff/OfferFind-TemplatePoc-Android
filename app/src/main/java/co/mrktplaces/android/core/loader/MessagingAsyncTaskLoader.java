package co.mrktplaces.android.core.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import co.mrktplaces.android.core.api.strongloop.Bids;
import co.mrktplaces.android.core.api.strongloop.Messages;
import co.mrktplaces.android.core.api.strongloop.Owner;
import co.mrktplaces.android.core.cache.CacheContentProvider;
import co.mrktplaces.android.core.cache.CacheHelper;

import java.util.ArrayList;

/**
 * Created by ugar on 05.03.16.
 */
public class MessagingAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Bids.ModelBids>> {

    public MessagingAsyncTaskLoader(Context context) {
        super(context);
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
//        cancelLoad();
    }

    @Override
    public ArrayList<Bids.ModelBids> loadInBackground() {
        ArrayList<Bids.ModelBids> list = new ArrayList<>();
        Cursor cursorOpportunities = getContext().getContentResolver().query(CacheContentProvider.OPPORTUNITIES_URI, null, null, null, null);
        if (cursorOpportunities != null) {
            if (cursorOpportunities.moveToFirst()) {
                do {
                    int opportunitiesId = cursorOpportunities.getInt(cursorOpportunities.getColumnIndex(CacheHelper.OPPORTUNITIES_ID));
                    String opportunitiesStatus = cursorOpportunities.getString(cursorOpportunities.getColumnIndex(CacheHelper.OPPORTUNITIES_STATUS));
                    String[] projection = new String[]{CacheHelper.BIDS_ID + " as _id ",
                            CacheHelper.BIDS_TITLE,
                            CacheHelper.BIDS_DESCRIPTION,
                            CacheHelper.OWNER_ID,
                            CacheHelper.OWNER_NAME,
                            CacheHelper.OWNER_AVATAR,
                            CacheHelper.BIDS_PRICE,
                            CacheHelper.BIDS_OPPORTUNITIES_ID,
                            CacheHelper.BIDS_CREATE_AT,
                            CacheHelper.BIDS_OWNER_ID,
                            CacheHelper.BIDS_STATUS};
                    String selection = CacheHelper.BIDS_OPPORTUNITIES_ID + " = ? ";
                    String[] selectionArg = new String[]{String.valueOf(opportunitiesId)};
                    Cursor cursorBids = getContext().getContentResolver().query(CacheContentProvider.BIDS_URI, projection, selection, selectionArg, null);
                    if (cursorBids != null) {
                        if (cursorBids.moveToFirst()) {
                            do {
                                String bidStatus = cursorBids.getString(cursorBids.getColumnIndex(CacheHelper.BIDS_STATUS));
                                if (opportunitiesStatus.equals(bidStatus)) {
                                    ArrayList<Messages> messagesList = getMessagesList(cursorBids.getInt(cursorBids.getColumnIndex(CacheHelper._ID)));
                                    list.add(new Bids.ModelBids(cursorBids.getInt(cursorBids.getColumnIndex(CacheHelper._ID)),
                                            cursorBids.getString(cursorBids.getColumnIndex(CacheHelper.BIDS_TITLE)),
                                            cursorBids.getString(cursorBids.getColumnIndex(CacheHelper.BIDS_DESCRIPTION)),
                                            cursorBids.getFloat(cursorBids.getColumnIndex(CacheHelper.BIDS_PRICE)),
                                            cursorBids.getInt(cursorBids.getColumnIndex(CacheHelper.BIDS_OPPORTUNITIES_ID)),
                                            cursorBids.getString(cursorBids.getColumnIndex(CacheHelper.BIDS_CREATE_AT)),
                                            opportunitiesStatus,
                                            cursorBids.getInt(cursorBids.getColumnIndex(CacheHelper.BIDS_OWNER_ID)),
                                            new Owner(cursorBids.getInt(cursorBids.getColumnIndex(CacheHelper.OWNER_ID)),
                                                    cursorBids.getString(cursorBids.getColumnIndex(CacheHelper.OWNER_NAME)),
                                                    cursorBids.getString(cursorBids.getColumnIndex(CacheHelper.OWNER_AVATAR))),
                                            messagesList));
                                }
                            } while (cursorBids.moveToNext());
                        }
                        cursorBids.close();
                    }
                } while (cursorOpportunities.moveToNext());
            }
            cursorOpportunities.close();
        }
        return list;
    }

    private ArrayList<Messages> getMessagesList(int bidId) {
        ArrayList<Messages> messagesList = new ArrayList<>();
        String[] projection = new String[]{CacheHelper.MESSAGE_ID + " as " + CacheHelper._ID, CacheHelper.MESSAGE_RECEIVER_ID, CacheHelper.MESSAGE_READ};
        String selection = CacheHelper.MESSAGE_BID_ID + " = ?";
        String[] selectionArg = new String[]{String.valueOf(bidId)};
        Cursor cursor = getContext().getContentResolver().query(CacheContentProvider.MESSAGE_URI, projection, selection, selectionArg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Messages messages = new Messages();
                    messages.setId(cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                    messages.setReceiverId(cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_RECEIVER_ID)));
                    messages.setRead(cursor.getInt(cursor.getColumnIndex(CacheHelper.MESSAGE_READ)) == 1);
                    messagesList.add(messages);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return messagesList;
    }
}
