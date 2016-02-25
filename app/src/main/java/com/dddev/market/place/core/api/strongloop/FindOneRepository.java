package com.dddev.market.place.core.api.strongloop;

import com.dddev.market.place.utils.PreferencesUtils;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 23.02.16.
 */
public class FindOneRepository extends com.strongloop.android.loopback.ModelRepository<FindOne> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/findOne", "GET"), getClassName() + ".findOne");
        return contract;
    }

    public FindOneRepository() {
        super("FindOne", "Applications", FindOne.class);
    }

    public void findOne(final FindOneCallback callback) {
        invokeStaticMethod("findOne", ImmutableMap.of("filter", "{\"name\":\"" + getApplicationContext().getPackageName() + "\"}", "access_token", PreferencesUtils.getUserToken(getApplicationContext())), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
                Timber.i("onError t=%s", t.toString());
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess(new FindOne(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public interface FindOneCallback {
        void onSuccess(FindOne response);
        void onError(Throwable t);
    }
}
