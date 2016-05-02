package co.mrktplaces.android.core.api.strongloop;

import co.mrktplaces.android.utils.PreferencesUtils;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 10.03.16.
 */
public class AccountPutRepository extends com.strongloop.android.loopback.ModelRepository<Account> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:id", "PUT"), getClassName() + ".accounts");
        return contract;
    }

    public AccountPutRepository() {
        super("Account", null, Account.class);
    }

    public void accounts(String name, String bankInfo, String email, Location location, final UserCallback callback) {
        invokeStaticMethod("accounts", ImmutableMap.of("id", PreferencesUtils.getUserId(getApplicationContext()), "name", name, "bankInfo", bankInfo, "email", email,
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
                callback.onSuccess(new Account(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public void accounts(String name, String bankInfo, String email, final UserCallback callback) {
        invokeStaticMethod("accounts", ImmutableMap.of("id", PreferencesUtils.getUserId(getApplicationContext()), "name", name, "bankInfo", bankInfo, "email", email), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
                Timber.i("onError t=%s", t.toString());
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess(new Account(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public interface UserCallback {
        void onSuccess(Account response);
        void onError(Throwable t);
    }
}
