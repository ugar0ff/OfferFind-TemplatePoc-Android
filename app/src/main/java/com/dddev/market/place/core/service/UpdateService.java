package com.dddev.market.place.core.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;

import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.retrofit.ApiRetrofit;
import com.dddev.market.place.core.api.strongloop.Account;
import com.dddev.market.place.core.api.strongloop.AccountGetRepository;
import com.dddev.market.place.core.api.strongloop.Bids;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.dddev.market.place.core.api.strongloop.Opportunities;
import com.dddev.market.place.core.api.strongloop.OpportunityGetRepository;
import com.dddev.market.place.core.api.strongloop.Owner;
import com.dddev.market.place.core.cache.CacheContentProvider;
import com.dddev.market.place.core.cache.CacheHelper;
import com.dddev.market.place.core.receiver.UpdateReceiver;
import com.dddev.market.place.utils.PreferencesUtils;
import com.dddev.market.place.utils.StaticKeys;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
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
                updateAccountData();
                updateOpportunities();
            } else if (intent.getStringExtra(StaticKeys.KEY_REQUEST).equals(StaticKeys.REQUEST_CHECK)) {
                sendMessageCallBack(RequestStatus.TASK_OK);
            } else {
                sendMessageCallBack(RequestStatus.TASK_ERROR);
            }
        }
    }

    private void updateOpportunities() {
        RequestStatus requestStatus;
        List<Opportunities.ModelOpportunity> opportunities = null;
        try {
            opportunities = ApiRetrofit.getOpportunities(PreferencesUtils.getUserId(getApplicationContext()), PreferencesUtils.getUserToken(getApplicationContext())).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (opportunities != null ) {
            Timber.i("onSuccess response=s");
            getContentResolver().delete(CacheContentProvider.BIDS_URI, null, null);
            getContentResolver().delete(CacheContentProvider.MESSAGE_URI, null, null);
            ContentValues[] opportunitiesValues = new ContentValues[opportunities.size()];
            for (int i = 0; i < opportunities.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(CacheHelper.OPPORTUNITIES_ID, opportunities.get(i).getId());
                values.put(CacheHelper.OPPORTUNITIES_TITLE, opportunities.get(i).getTitle());
                values.put(CacheHelper.OPPORTUNITIES_DESCRIPTION, opportunities.get(i).getDescription());
                values.put(CacheHelper.OPPORTUNITIES_ACCOUNT_ID, opportunities.get(i).getOwnerId());
                values.put(CacheHelper.OPPORTUNITIES_CREATE_AT, opportunities.get(i).getCreatedAt());
                values.put(CacheHelper.OPPORTUNITIES_CATEGORY_ID, opportunities.get(i).getCategoryId());
                values.put(CacheHelper.OPPORTUNITIES_STATUS, opportunities.get(i).getState());

                updateBids(opportunities.get(i).getBids(), opportunities.get(i).getTitle());

                opportunitiesValues[i] = values;
            }
            getContentResolver().delete(CacheContentProvider.OPPORTUNITIES_URI, null, null);
            getContentResolver().bulkInsert(CacheContentProvider.OPPORTUNITIES_URI, opportunitiesValues);
            requestStatus = RequestStatus.TASK_OK;
        } else {
            requestStatus = RequestStatus.TASK_ERROR;
        }
        sendMessageCallBack(requestStatus);
    }

    private void updateBids(List<Bids.ModelBids> modelBids, String title) {
        ContentValues[] bidsContentValues = new ContentValues[modelBids.size()];
        for (int j = 0; j < modelBids.size(); j++) {
            ContentValues bidsValues = new ContentValues();
            bidsValues.put(CacheHelper.BIDS_ID, modelBids.get(j).getId());
            bidsValues.put(CacheHelper.BIDS_TITLE, title);
            bidsValues.put(CacheHelper.BIDS_DESCRIPTION, modelBids.get(j).getDescription());
            bidsValues.put(CacheHelper.BIDS_OPPORTUNITIES_ID, modelBids.get(j).getOpportunityId());
            bidsValues.put(CacheHelper.BIDS_PRICE, modelBids.get(j).getPrice());
            bidsValues.put(CacheHelper.BIDS_STATUS, modelBids.get(j).getState());
            bidsValues.put(CacheHelper.BIDS_CREATE_AT, modelBids.get(j).getCreatedAt());
            bidsValues.put(CacheHelper.BIDS_OWNER_ID, modelBids.get(j).getOwnerId());
            bidsContentValues[j] = bidsValues;
            updateOwner(modelBids.get(j).getOwner());
            updateMessages(modelBids.get(j).getMessages());
        }
        getContentResolver().bulkInsert(CacheContentProvider.BIDS_URI, bidsContentValues);
    }

    private void updateOwner(Owner owner) {
        if (owner == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(CacheHelper.OWNER_ID, owner.getId());
        values.put(CacheHelper.OWNER_AVATAR, owner.getAvatar());
        values.put(CacheHelper.OWNER_NAME, owner.getName());
        getBaseContext().getContentResolver().insert(CacheContentProvider.OWNER_URI, values);
    }

    private void updateMessages(ArrayList<Messages> messages) {
        if (messages == null) {
            return;
        }
        ContentValues[] contentValues = new ContentValues[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(CacheHelper.MESSAGE_ID, messages.get(i).getId());
            values.put(CacheHelper.MESSAGE_TEXT, messages.get(i).getText());
            values.put(CacheHelper.MESSAGE_CREATE_AT, messages.get(i).getCreatedAt());
            values.put(CacheHelper.MESSAGE_OWNER_ID, messages.get(i).getOwnerId());
            values.put(CacheHelper.MESSAGE_SENDER_ID, messages.get(i).getSenderId());
            values.put(CacheHelper.MESSAGE_BID_ID, messages.get(i).getBidId());
            values.put(CacheHelper.MESSAGE_READ, messages.get(i).isRead());
            contentValues[i] = values;
        }
        getBaseContext().getContentResolver().bulkInsert(CacheContentProvider.MESSAGE_URI, contentValues);
    }

    private void updateAccountData() {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final AccountGetRepository repository = AppOfferFind.getRestAdapter(getApplicationContext()).createRepository(AccountGetRepository.class);
                repository.createContract();
                repository.accounts(PreferencesUtils.getUserId(getApplicationContext()), new AccountGetRepository.UserCallback() {
                    @Override
                    public void onSuccess(Account account) {
                        if (account != null) {
                            Timber.i("onSuccess response=%s", account.toString());
                            PreferencesUtils.setUserName(getApplicationContext(), account.getName());
                            PreferencesUtils.setUserBankInfo(getApplicationContext(), account.getBankInfo());
                            PreferencesUtils.setUserEmail(getApplicationContext(), account.getEmail());
                            PreferencesUtils.setUserAddress(getApplicationContext(), account.getAddress());
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

    private void sendMessageCallBack(RequestStatus requestStatus) {
        Intent intent = new Intent(UpdateReceiver.BROADCAST_ACTION);
        intent.putExtra(StaticKeys.KEY_REQUEST, requestStatus);
        sendBroadcast(intent);
    }

    public enum RequestStatus {
        TASK_OK, TASK_ERROR
    }
}
