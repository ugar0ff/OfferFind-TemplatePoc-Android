package co.mrktplaces.clients.android.core.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

import co.mrktplaces.clients.android.core.AppOfferFind;
import co.mrktplaces.clients.android.core.api.strongloop.Bids;
import co.mrktplaces.clients.android.core.api.strongloop.StateCloseRepository;
import co.mrktplaces.clients.android.utils.StaticKeys;

import timber.log.Timber;

/**
 * Created by ugar on 04.03.16.
 */
public class CompleteBidsService extends IntentService {

    public CompleteBidsService() {
        super("CompleteBidsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int id = intent.getIntExtra(StaticKeys.COMPLETE_BIDS_ID, 0);
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
                final StateCloseRepository repository = AppOfferFind.getRestAdapter(getApplicationContext()).createRepository(StateCloseRepository.class);
                repository.createContract();
                repository.bids(id, new StateCloseRepository.BidsCallback() {
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
