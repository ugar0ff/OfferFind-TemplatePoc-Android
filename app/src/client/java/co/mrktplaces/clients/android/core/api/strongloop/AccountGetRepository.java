package co.mrktplaces.clients.android.core.api.strongloop;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import timber.log.Timber;

/**
 * Created by ugar on 10.03.16.
 */
public class AccountGetRepository extends com.strongloop.android.loopback.ModelRepository<Account> {

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:id", "GET"), getClassName() + ".accounts");
        return contract;
    }

    public AccountGetRepository() {
        super("Account", null, Account.class);
    }

    public void accounts(int id, final UserCallback callback) {
        invokeStaticMethod("accounts", ImmutableMap.of("id", id), new Adapter.Callback() {

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
