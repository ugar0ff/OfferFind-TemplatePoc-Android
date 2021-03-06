package co.mrktplaces.android.core.api.strongloop;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 25.02.16.
 */
public class OpportunityPostRepository extends com.strongloop.android.loopback.ModelRepository<Opportunities> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl(), "POST"), getClassName() + ".opportunities");
        return contract;
    }

    public OpportunityPostRepository() {
        super("Opportunity", null, Opportunities.class);
    }

    public void opportunities(String title, String description, Location location, final OpportunityCallback callback) {
        invokeStaticMethod("opportunities", ImmutableMap.of("title", title,
                "description", description,
                "location", ImmutableMap.of("address", location.getAddress(),
                        "latitude", location.getLatitude(),
                        "longitude", location.getLongitude())), new Adapter.Callback() {

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
