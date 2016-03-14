package com.dddev.market.place.core.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;

import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.BidPutRepository;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.dddev.market.place.core.cache.CacheContentProvider;
import com.dddev.market.place.core.cache.CacheHelper;
import com.dddev.market.place.utils.StaticKeys;

import timber.log.Timber;

/**
 * Created by ugar on 04.03.16.
 */
public class AcceptBidsService extends IntentService {

    public AcceptBidsService() {
        super("com.dddev.market.place.core.service.AcceptBidsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int id = intent.getIntExtra(StaticKeys.ACCEPT_BIDS_ID, 0);
            if (id > 0) {
                sendBidStatus(id);
            }
        }
    }

    private int getOpportunitiesId(int id) {
        String[] projection = new String[]{CacheHelper.BIDS_ID + " as " + CacheHelper._ID, CacheHelper.BIDS_OPPORTUNITIES_ID};
        String selection = CacheHelper.BIDS_ID + " = ? ";
        String[] selectionArg = new String[]{String.valueOf(id)};
        Cursor cursor = getContentResolver().query(CacheContentProvider.BIDS_URI, projection, selection, selectionArg, null);
        int opportunitiesId = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                opportunitiesId = cursor.getInt(cursor.getColumnIndex(CacheHelper.BIDS_OPPORTUNITIES_ID));
            }
            cursor.close();
        }
        return opportunitiesId;
    }

    private void changeBidsState(int id,int bidsId) {
        String[] projection = new String[]{CacheHelper.BIDS_ID + " as " + CacheHelper._ID, CacheHelper.BIDS_OPPORTUNITIES_ID, CacheHelper.BIDS_CREATE_AT,
                CacheHelper.BIDS_DESCRIPTION, CacheHelper.BIDS_PRICE, CacheHelper.BIDS_STATUS, CacheHelper.BIDS_TITLE, CacheHelper.BIDS_URL};
        String selection = CacheHelper.BIDS_OPPORTUNITIES_ID + " = ? ";
        String[] selectionArg = new String[]{String.valueOf(id)};
        Cursor cursor = getContentResolver().query(CacheContentProvider.BIDS_URI, projection, selection, selectionArg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ContentValues[] contentValues = new ContentValues[cursor.getCount()];
                int i = 0;
                do {
                    ContentValues values = new ContentValues();
                    values.put(CacheHelper.BIDS_ID, cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                    values.put(CacheHelper.BIDS_OPPORTUNITIES_ID, cursor.getInt(cursor.getColumnIndex(CacheHelper.BIDS_OPPORTUNITIES_ID)));
                    values.put(CacheHelper.BIDS_DESCRIPTION, cursor.getString(cursor.getColumnIndex(CacheHelper.BIDS_DESCRIPTION)));
                    values.put(CacheHelper.BIDS_PRICE, cursor.getFloat(cursor.getColumnIndex(CacheHelper.BIDS_PRICE)));
                    values.put(CacheHelper.BIDS_STATUS, cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)) == bidsId ? 1 : 0);
                    values.put(CacheHelper.BIDS_TITLE, cursor.getString(cursor.getColumnIndex(CacheHelper.BIDS_TITLE)));
                    values.put(CacheHelper.BIDS_URL, cursor.getString(cursor.getColumnIndex(CacheHelper.BIDS_URL)));
                    values.put(CacheHelper.BIDS_CREATE_AT, cursor.getString(cursor.getColumnIndex(CacheHelper.BIDS_CREATE_AT)));
                    contentValues[i] = values;
                    i++;
                } while (cursor.moveToNext());
                getContentResolver().bulkInsert(CacheContentProvider.BIDS_URI, contentValues);
            }
            cursor.close();
        }
    }

    private void changeOpportunitiesState(int id) {
        String[] projection = new String[]{CacheHelper.OPPORTUNITIES_ID + " as " + CacheHelper._ID, CacheHelper.OPPORTUNITIES_TITLE, CacheHelper.OPPORTUNITIES_STATUS,
                CacheHelper.OPPORTUNITIES_CREATE_AT, CacheHelper.OPPORTUNITIES_DESCRIPTION, CacheHelper.OPPORTUNITIES_ACCOUNT_ID, CacheHelper.OPPORTUNITIES_CATEGORY_ID};
        String selection = CacheHelper.OPPORTUNITIES_ID + " = ? ";
        String[] selectionArg = new String[]{String.valueOf(id)};
        Cursor cursor = getContentResolver().query(CacheContentProvider.OPPORTUNITIES_URI, projection, selection, selectionArg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(CacheHelper.OPPORTUNITIES_ID, cursor.getInt(cursor.getColumnIndex(CacheHelper._ID)));
                values.put(CacheHelper.OPPORTUNITIES_TITLE, cursor.getString(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_TITLE)));
                values.put(CacheHelper.OPPORTUNITIES_STATUS, 1);
                values.put(CacheHelper.OPPORTUNITIES_CREATE_AT, cursor.getString(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_CREATE_AT)));
                values.put(CacheHelper.OPPORTUNITIES_DESCRIPTION, cursor.getString(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_DESCRIPTION)));
                values.put(CacheHelper.OPPORTUNITIES_ACCOUNT_ID, cursor.getInt(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_ACCOUNT_ID)));
                values.put(CacheHelper.OPPORTUNITIES_CATEGORY_ID, cursor.getInt(cursor.getColumnIndex(CacheHelper.OPPORTUNITIES_CATEGORY_ID)));
                getContentResolver().insert(CacheContentProvider.OPPORTUNITIES_URI, values);
            }
            cursor.close();
        }
    }

    private void sendBidStatus(final int id) {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final BidPutRepository repository = AppOfferFind.getRestAdapter(getApplicationContext()).createRepository(BidPutRepository.class);
                repository.createContract();
                repository.bids(id, 1, new BidPutRepository.BidsCallback() {
                    @Override
                    public void onSuccess(Bids.ModelBids opportunity) {
                        Timber.i("onSuccess response=%s", opportunity.toString());
                        int opportunitiesId = getOpportunitiesId(id);
                        if (opportunitiesId > 0) {
                            changeBidsState(opportunitiesId, id);
                            changeOpportunitiesState(opportunitiesId);
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
}
