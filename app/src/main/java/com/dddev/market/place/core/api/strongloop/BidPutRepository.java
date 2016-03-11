package com.dddev.market.place.core.api.strongloop;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 11.03.16.
 */
public class BidPutRepository extends com.strongloop.android.loopback.ModelRepository<Bids> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:id", "PUT"), getClassName() + ".Bids");
        return contract;
    }

    public BidPutRepository() {
        super("Bid", null, Bids.class);
    }

    public void bids(int id, int status, final BidsCallback callback) {
        invokeStaticMethod("Bids", ImmutableMap.of("id", id, "status", status), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
                Timber.i("onError t=%s", t.toString());
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess(new Bids.ModelBids(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public interface BidsCallback {
        void onSuccess(Bids.ModelBids response);

        void onError(Throwable t);

    }
}