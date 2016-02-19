package com.offerfind.template.poc.core.api.strongloop;

import com.google.common.collect.ImmutableMap;
import com.offerfind.template.poc.utils.PreferencesUtils;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 19.02.16.
 */
public class OpportunityRepository  extends com.strongloop.android.loopback.ModelRepository<Opportunities> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/Opportunities/" + String.valueOf(PreferencesUtils.getUserId(getApplicationContext())), "GET"), getClassName() + ".opportunities");
        return contract;
    }

    public OpportunityRepository() {
        super("Opportunities", null, Opportunities.class);
    }

    public void opportunities(final OpportunityCallback callback) {

        invokeStaticMethod("", ImmutableMap.of("access_token", PreferencesUtils.getUserToken(getApplicationContext())), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
                Timber.i("onError t=%s", t.toString());
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess(new Opportunities(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public interface OpportunityCallback {
        public void onSuccess(Opportunities response);
        public void onError(Throwable t);
    }
}