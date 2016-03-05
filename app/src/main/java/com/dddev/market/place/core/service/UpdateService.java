package com.dddev.market.place.core.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;

import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.dddev.market.place.core.api.strongloop.Opportunities;
import com.dddev.market.place.core.api.strongloop.OpportunityGetRepository;
import com.dddev.market.place.core.cache.CacheContentProvider;
import com.dddev.market.place.core.cache.CacheHelper;
import com.dddev.market.place.core.receiver.UpdateReceiver;
import com.dddev.market.place.utils.StaticKeys;

import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 24.02.16.
 */
public class UpdateService extends IntentService {

    public UpdateService() {
        super("com.dddev.market.place.core.service.UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (intent.getStringExtra(StaticKeys.KEY_REQUEST).equals(StaticKeys.REQUEST_START)) {
                updateOpportunities();
            } else if (intent.getStringExtra(StaticKeys.KEY_REQUEST).equals(StaticKeys.REQUEST_CHECK)) {
                sendMessageCallBack(RequestStatus.TASK_OK);
            } else {
                sendMessageCallBack(RequestStatus.TASK_ERROR);
            }
        }
    }

    private void updateOpportunities() {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final OpportunityGetRepository repository = AppOfferFind.getRestAdapter(getApplicationContext()).createRepository(OpportunityGetRepository.class);
                repository.createContract();
                repository.opportunities(new OpportunityGetRepository.OpportunityCallback() {
                    @Override
                    public void onSuccess(Opportunities opportunity) {
                        Timber.i("onSuccess response=%s", opportunity.toString());
                        if (opportunity != null && opportunity.getList() != null) {
                            getContentResolver().delete(CacheContentProvider.OPPORTUNITIES_URI, null, null);
                            getContentResolver().delete(CacheContentProvider.BIDS_URI, null, null);
                            ContentValues[] opportunitiesValues = new ContentValues[opportunity.getList().size()];
                            for (int i = 0; i < opportunity.getList().size(); i++) {
                                ContentValues values = new ContentValues();
                                values.put(CacheHelper.OPPORTUNITIES_ID, opportunity.getList().get(i).getId());
                                values.put(CacheHelper.OPPORTUNITIES_TITLE, opportunity.getList().get(i).getTitle());
                                values.put(CacheHelper.OPPORTUNITIES_DESCRIPTION, opportunity.getList().get(i).getDescription());
                                values.put(CacheHelper.OPPORTUNITIES_ACCOUNT_ID, opportunity.getList().get(i).getOwnerId());
                                values.put(CacheHelper.OPPORTUNITIES_CREATE_AT, opportunity.getList().get(i).getCreatedAt());
                                values.put(CacheHelper.OPPORTUNITIES_CATEGORY_ID, opportunity.getList().get(i).getCategoryId());
                                values.put(CacheHelper.OPPORTUNITIES_STATUS, opportunity.getList().get(i).getStatus());

                                updateBids(opportunity.getList().get(i).getBids());

                                opportunitiesValues[i] = values;
                            }
                            getContentResolver().bulkInsert(CacheContentProvider.OPPORTUNITIES_URI, opportunitiesValues);
                        }
                        sendMessageCallBack(RequestStatus.TASK_OK);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e("onError Throwable: %s", t.toString());
                        sendMessageCallBack(RequestStatus.TASK_ERROR);
                    }
                });
            }
        });
    }

    private void updateBids(List<Bids.ModelBids> modelBids) {
        ContentValues[] bidsContentValues = new ContentValues[modelBids.size()];
        for (int j = 0; j < modelBids.size(); j++) {
            ContentValues bidsValues = new ContentValues();
            bidsValues.put(CacheHelper.BIDS_ID, modelBids.get(j).getId());
            bidsValues.put(CacheHelper.BIDS_TITLE, modelBids.get(j).getTitle());
            bidsValues.put(CacheHelper.BIDS_DESCRIPTION, modelBids.get(j).getDescription());
            bidsValues.put(CacheHelper.BIDS_OPPORTUNITIES_ID, modelBids.get(j).getOpportunityId());
            bidsValues.put(CacheHelper.BIDS_PRICE, modelBids.get(j).getPrice());
            bidsValues.put(CacheHelper.BIDS_URL, modelBids.get(j).getUrl());
            bidsValues.put(CacheHelper.BIDS_STATUS, modelBids.get(j).getState());
            bidsValues.put(CacheHelper.BIDS_CREATE_AT, modelBids.get(j).getCreatedAt());
            //TODO: add provider model
            bidsContentValues[j] = bidsValues;
        }
        getContentResolver().bulkInsert(CacheContentProvider.BIDS_URI, bidsContentValues);
    }

    private void sendMessageCallBack(RequestStatus requestStatus) {
        Intent intent = new Intent(UpdateReceiver.BROADCAST_ACTION);
        intent.putExtra(StaticKeys.KEY_REQUEST, requestStatus);
        sendBroadcast(intent);
    }

    public enum RequestStatus {
        TASK_OK, TASK_ERROR
    }
}
