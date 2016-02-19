package com.offerfind.template.poc.core.api.strongloop;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by ugar on 16.02.16.
 */
public class UserRepository extends com.strongloop.android.loopback.UserRepository<User> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/Users/login", "POST"), getClassName() + ".user");
        return contract;
    }

    public UserRepository() {
        super("User", null, User.class);
    }

    public void user(String email, String password, final UserCallback callback) {
        invokeStaticMethod("login", ImmutableMap.of("email", email, "password", password), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
                Timber.i("onError t=%s", t.toString());
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess(new User(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public interface UserCallback {
        public void onSuccess(User response);
        public void onError(Throwable t);
    }
}
