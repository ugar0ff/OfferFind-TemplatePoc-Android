package co.mrktplaces.android.core.api.strongloop;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 04.03.16.
 */
public class OpportunityPutRepository extends com.strongloop.android.loopback.ModelRepository<Opportunities> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl(), "PUT"), getClassName() + ".opportunities");
        return contract;
    }

    public OpportunityPutRepository() {
        super("Opportunity", null, Opportunities.class);
    }

    public void opportunities(int id, final OpportunityCallback callback) {
        invokeStaticMethod("opportunities", ImmutableMap.of("id", id), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
                Timber.i("onError t=%s", t.toString());
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess(new Opportunities.ModelOpportunity(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public interface OpportunityCallback {
        void onSuccess(Opportunities.ModelOpportunity response);
        void onError(Throwable t);
    }
}
