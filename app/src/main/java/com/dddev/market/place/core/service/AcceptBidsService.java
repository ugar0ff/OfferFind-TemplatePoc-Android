package com.dddev.market.place.core.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.StateAcceptRepository;
import com.dddev.market.place.core.api.strongloop.Bids;
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
