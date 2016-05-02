package co.mrktplaces.android.core.api.strongloop;

import com.google.common.collect.ImmutableMap;
import co.mrktplaces.android.utils.PreferencesUtils;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 19.02.16.
 */
public class OpportunityGetRepository extends com.strongloop.android.loopback.ModelRepository<Opportunities> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/" + PreferencesUtils.getUserId(getApplicationContext()) + "/opportunities", "GET"), getClassName() + ".opportunities");
        return contract;
    }

    public OpportunityGetRepository() {
        super("Opportunities", "Accounts", Opportunities.class);
    }

    public void opportunities(final OpportunityCallback callback) {
        invokeStaticMethod("opportunities", ImmutableMap.of("filter", "{\"include\": {\"bids\": [\"owner\", \"messages\"]}}"), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
//                Timber.i("onError t=%s", t.toString());
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess(new Opportunities(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public interface OpportunityCallback {
        void onSuccess(Opportunities response);
        void onError(Throwable t);
    }
}