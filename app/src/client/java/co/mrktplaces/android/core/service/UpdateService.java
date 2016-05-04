package co.mrktplaces.android.core.service;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Handler;
import android.os.RemoteException;

import co.mrktplaces.android.core.AppOfferFind;
import co.mrktplaces.android.core.api.retrofit.ApiRetrofit;
import co.mrktplaces.android.core.api.strongloop.Account;
import co.mrktplaces.android.core.api.strongloop.AccountGetRepository;
import co.mrktplaces.android.core.api.strongloop.Bids;
import co.mrktplaces.android.core.api.strongloop.Messages;
import co.mrktplaces.android.core.api.strongloop.Opportunities;
import co.mrktplaces.android.core.api.strongloop.Owner;
import co.mrktplaces.android.core.cache.CacheContentProvider;
import co.mrktplaces.android.core.cache.CacheHelper;
import co.mrktplaces.android.core.receiver.UpdateReceiver;
import co.mrktplaces.android.utils.PreferencesUtils;
import co.mrktplaces.android.utils.StaticKeys;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 24.02.16.
 */
public class UpdateService extends IntentService {

    private ArrayList<ContentProviderOperation> providerOperations;

    public UpdateService() {
        super("UpdateService");
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
        if (opportunities != null) {
            Timber.i("onSuccess response=s");
            long currentTime = System.currentTimeMillis();
            providerOperations = new ArrayList<>();
            for (int i = 0; i < opportunities.size(); i++) {
                providerOperations.add(ContentProviderOperation.newInsert(CacheContentProvider.OPPORTUNITIES_URI)
                        .withValue(CacheHelper.OPPORTUNITIES_ID, opportunities.get(i).getId())
                        .withValue(CacheHelper.OPPORTUNITIES_TITLE, opportunities.get(i).getTitle())
                        .withValue(CacheHelper.OPPORTUNITIES_DESCRIPTION, opportunities.get(i).getDescription())
                        .withValue(CacheHelper.OPPORTUNITIES_ACCOUNT_ID, opportunities.get(i).getOwnerId())
                        .withValue(CacheHelper.OPPORTUNITIES_CREATE_AT, opportunities.get(i).getCreatedAt())
                        .withValue(CacheHelper.OPPORTUNITIES_CATEGORY_ID, opportunities.get(i).getCategoryId())
                        .withValue(CacheHelper.OPPORTUNITIES_STATUS, opportunities.get(i).getState())
                        .withValue(CacheHelper.OPPORTUNITIES_ADDRESS, opportunities.get(i).getLocation() == null ? null : opportunities.get(i).getLocation().getAddress())
                        .withValue(CacheHelper.OPPORTUNITIES_LATITUDE, opportunities.get(i).getLocation() == null ? null : opportunities.get(i).getLocation().getLatitude())
                        .withValue(CacheHelper.OPPORTUNITIES_LONGITUDE, opportunities.get(i).getLocation() == null ? null : opportunities.get(i).getLocation().getLongitude())
                        .withValue(CacheHelper.TIMESTAMP, currentTime)
                        .withYieldAllowed(true)
                        .build());
                updateBids(opportunities.get(i).getBids(), opportunities.get(i).getTitle(), currentTime);
            }

            try {
                getContentResolver().applyBatch(CacheContentProvider.AUTHORITY, providerOperations);
                deleteOldData(currentTime);
                requestStatus = RequestStatus.TASK_OK;
            } catch (RemoteException e) {
                e.printStackTrace();
                requestStatus = RequestStatus.TASK_ERROR;
            } catch (OperationApplicationException e) {
                e.printStackTrace();
                requestStatus = RequestStatus.TASK_ERROR;
            }
        } else {
            requestStatus = RequestStatus.TASK_ERROR;
        }
        sendMessageCallBack(requestStatus);
    }

    private void updateBids(List<Bids.ModelBids> modelBids, String title, long currentTime) {
        for (int j = 0; j < modelBids.size(); j++) {
            providerOperations.add(ContentProviderOperation.newInsert(CacheContentProvider.BIDS_URI)
                    .withValue(CacheHelper.BIDS_ID, modelBids.get(j).getId())
                    .withValue(CacheHelper.BIDS_TITLE, title)
                    .withValue(CacheHelper.BIDS_DESCRIPTION, modelBids.get(j).getDescription())
                    .withValue(CacheHelper.BIDS_OPPORTUNITIES_ID, modelBids.get(j).getOpportunityId())
                    .withValue(CacheHelper.BIDS_PRICE, modelBids.get(j).getPrice())
                    .withValue(CacheHelper.BIDS_STATUS, modelBids.get(j).getState())
                    .withValue(CacheHelper.BIDS_CREATE_AT, modelBids.get(j).getCreatedAt())
                    .withValue(CacheHelper.BIDS_OWNER_ID, modelBids.get(j).getOwnerId())
                    .withValue(CacheHelper.TIMESTAMP, currentTime)
                    .withYieldAllowed(true)
                    .build());
            updateOwner(modelBids.get(j).getOwner(), currentTime);
            updateMessages(modelBids.get(j).getMessages(), currentTime);
        }
    }

    private void updateOwner(Owner owner, long currentTime) {
        if (owner == null) {
            return;
        }
        providerOperations.add(ContentProviderOperation.newInsert(CacheContentProvider.OWNER_URI)
                .withValue(CacheHelper.OWNER_ID, owner.getId())
                .withValue(CacheHelper.OWNER_AVATAR, owner.getAvatar())
                .withValue(CacheHelper.OWNER_NAME, owner.getName())
                .withValue(CacheHelper.TIMESTAMP, currentTime)
                .withYieldAllowed(true)
                .build());
    }

    private void updateMessages(ArrayList<Messages> messages, long currentTime) {
        if (messages == null) {
            return;
        }
        for (int i = 0; i < messages.size(); i++) {
            providerOperations.add(ContentProviderOperation.newInsert(CacheContentProvider.MESSAGE_URI)
                    .withValue(CacheHelper.MESSAGE_ID, messages.get(i).getId())
                    .withValue(CacheHelper.MESSAGE_TEXT, messages.get(i).getText())
                    .withValue(CacheHelper.MESSAGE_CREATE_AT, messages.get(i).getCreatedAt())
                    .withValue(CacheHelper.MESSAGE_OWNER_ID, messages.get(i).getOwnerId())
                    .withValue(CacheHelper.MESSAGE_SENDER_ID, messages.get(i).getSenderId())
                    .withValue(CacheHelper.MESSAGE_BID_ID, messages.get(i).getBidId())
                    .withValue(CacheHelper.MESSAGE_READ, messages.get(i).isRead() ? 1 : 0)
                    .withValue(CacheHelper.MESSAGE_RECEIVER_ID, messages.get(i).getReceiverId())
                    .withValue(CacheHelper.TIMESTAMP, currentTime)
                    .withYieldAllowed(true)
                    .build());
        }
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
                            PreferencesUtils.setUserAddress(getApplicationContext(), account.getLocation().getAddress());
                            PreferencesUtils.setUserLatitude(getApplicationContext(), account.getLocation().getLatitude());
                            PreferencesUtils.setUserLongitude(getApplicationContext(), account.getLocation().getLongitude());
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

    private void deleteOldData(long currentTime) {
        String selection = CacheHelper.TIMESTAMP + " < ?";
        String[] selectionArg = new String[]{String.valueOf(currentTime)};
        getContentResolver().delete(CacheContentProvider.OPPORTUNITIES_URI, selection, selectionArg);
        getContentResolver().delete(CacheContentProvider.BIDS_URI, selection, selectionArg);
        getContentResolver().delete(CacheContentProvider.MESSAGE_URI, selection, selectionArg);
        getContentResolver().delete(CacheContentProvider.OWNER_URI, selection, selectionArg);
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
