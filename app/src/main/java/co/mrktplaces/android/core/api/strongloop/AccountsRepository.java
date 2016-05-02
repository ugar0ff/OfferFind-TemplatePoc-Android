package co.mrktplaces.android.core.api.strongloop;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 16.02.16.
 */
public class AccountsRepository extends com.strongloop.android.loopback.ModelRepository<Accounts> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/Accounts/login", "POST"), getClassName() + ".accounts");
        return contract;
    }

    public AccountsRepository() {
        super("Accounts", null, Accounts.class);
    }

    public void accounts(String email, String password, final UserCallback callback) {
        invokeStaticMethod("login", ImmutableMap.of("email", email, "password", password), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
                Timber.i("onError t=%s", t.toString());
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess(new Accounts(response));
                Timber.i("onSuccess response=%s", response);
            }
        });
    }

    public interface UserCallback {
        void onSuccess(Accounts response);
        void onError(Throwable t);
    }
}
