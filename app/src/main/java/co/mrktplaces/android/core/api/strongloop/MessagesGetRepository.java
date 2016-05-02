package co.mrktplaces.android.core.api.strongloop;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 29.02.16.
 */
public class MessagesGetRepository extends com.strongloop.android.loopback.ModelRepository<StreamModel> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:id/messages", "GET"), getClassName() + ".messages");
        return contract;
    }

    public MessagesGetRepository() {
        super("StreamModel", "Bids", StreamModel.class);
    }

    public void messages(int id, final MessagesCallback callback) {
        invokeStaticMethod("messages", ImmutableMap.of("id", id), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
                Timber.i("onError t=%s", t.toString());
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess(new StreamModel(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public interface MessagesCallback {
        void onSuccess(StreamModel response);
        void onError(Throwable t);
    }
}
