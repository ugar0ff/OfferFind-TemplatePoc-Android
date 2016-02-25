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
public class InstallationRepository extends com.strongloop.android.loopback.ModelRepository<Installation> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl(), "POST"), getClassName() + ".install");
        return contract;
    }

    public InstallationRepository() {
        super("Installation", null, Installation.class);
    }

    public void install(String token, final InstallationCallback callback) {
        invokeStaticMethod("install", ImmutableMap.of("userId", PreferencesUtils.getUserEmail(getApplicationContext()),
                "deviceToken", token,
                "deviceType", "android",
                "appId", PreferencesUtils.getAppId(getApplicationContext()),
                "access_token", PreferencesUtils.getUserToken(getApplicationContext())), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
                Timber.i("onError t=%s", t.toString());
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess(new Installation(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public interface InstallationCallback {
        void onSuccess(Installation response);
        void onError(Throwable t);
    }
}
