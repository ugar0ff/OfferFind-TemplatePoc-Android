package com.dddev.market.place.core.api.strongloop;

import com.dddev.market.place.utils.PreferencesUtils;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 29.02.16.
 */
public class MessagesPostRepository extends com.strongloop.android.loopback.ModelRepository<Messages> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:id/messages", "POST"), getClassName() + ".messages");
        return contract;
    }

    public MessagesPostRepository() {
        super("Messages", "Bids", Messages.class);
    }

    public void messages(String text, int bidId, final MessagesCallback callback) {
        invokeStaticMethod("messages", ImmutableMap.of("id", bidId,
                "text", text,
                "senderId", PreferencesUtils.getUserId(getApplicationContext()),
                "bidId", bidId), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
                Timber.i("onError t=%s", t.toString());
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess(new Messages.ModelMessages(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public interface MessagesCallback {
        void onSuccess(Messages.ModelMessages response);
        void onError(Throwable t);
    }
}
