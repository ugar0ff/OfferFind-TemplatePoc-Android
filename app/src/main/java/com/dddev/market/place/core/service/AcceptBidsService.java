package com.dddev.market.place.core.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;

import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.StateAcceptRepository;
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

    private void sendBidStatus(final int id) {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final StateAcceptRepository repository = AppOfferFind.getRestAdapter(getApplicationContext()).createRepository(StateAcceptRepository.class);
                repository.createContract();
                repository.bids(id, new StateAcceptRepository.BidsCallback() {
                    @Override
                    public void onSuccess(Bids.ModelBids opportunity) {
                        Timber.i("onSuccess response=%s", opportunity.toString());
                        ContentValues values = new ContentValues();
                        values.put(CacheHelper.BIDS_ID, id);
                        values.put(CacheHelper.BIDS_STATUS, StaticKeys.State.ACCEPTED);
                        getContentResolver().insert(CacheContentProvider.BIDS_URI, values);

                        String[] projection = new String[]{CacheHelper.BIDS_ID + " as " + CacheHelper._ID, CacheHelper.BIDS_OPPORTUNITIES_ID};
                        String selection = CacheHelper.BIDS_ID + " = ?";
                        String[] selectionArg = new String[]{String.valueOf(id)};
                        Cursor cursor = getContentResolver().query(CacheContentProvider.OPPORTUNITIES_URI, projection, selection, selectionArg, null);
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                values = new ContentValues();
                                values.put(CacheHelper.OPPORTUNITIES_ID, cursor.getInt(cursor.getColumnIndex(CacheHelper.BIDS_OPPORTUNITIES_ID)));
                                values.put(CacheHelper.OPPORTUNITIES_STATUS, StaticKeys.State.ACCEPTED);
                                getContentResolver().insert(CacheContentProvider.OPPORTUNITIES_URI, values);
                            }
                            cursor.close();
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
