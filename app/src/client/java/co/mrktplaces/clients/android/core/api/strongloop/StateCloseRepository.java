package co.mrktplaces.clients.android.core.api.strongloop;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 29.03.16.
 */
public class StateCloseRepository extends com.strongloop.android.loopback.ModelRepository<Bids> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:id/state/withdraw", "POST"), getClassName() + ".Bids");
        return contract;
    }

    public StateCloseRepository() {
        super("Bid", null, Bids.class);
    }

    public void bids(int id, final BidsCallback callback) {
        invokeStaticMethod("Bids", ImmutableMap.of("id", id), new Adapter.Callback() {

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
