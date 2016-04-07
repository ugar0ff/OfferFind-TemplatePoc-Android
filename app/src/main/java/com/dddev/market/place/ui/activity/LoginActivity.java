package com.dddev.market.place.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.dddev.market.place.core.api.strongloop.FindOne;
import com.dddev.market.place.core.api.strongloop.FindOneRepository;
import com.dddev.market.place.core.gcm.RegistrationIntentService;
import com.dddev.market.place.core.receiver.UpdateReceiver;
import com.dddev.market.place.core.service.UpdateService;
import com.dddev.market.place.utils.StaticKeys;
import com.dddev.market.place.utils.Utilities;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.dddev.market.place.R;
import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.Accounts;
import com.dddev.market.place.core.api.strongloop.AccountsRepository;
import com.dddev.market.place.ui.activity.base.BaseActivity;
import com.dddev.market.place.utils.PreferencesUtils;

import org.json.JSONObject;

import java.util.Arrays;

import timber.log.Timber;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, UpdateReceiver.UpdateBroadcastListener {

    private UpdateReceiver mUpdateReceiver;
    private CallbackManager callbackManager;
    private FrameLayout progressBar;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        mUpdateReceiver = new UpdateReceiver();
        mUpdateReceiver.setUpdaterBroadcastListener(this);
        progressBar = (FrameLayout) findViewById(R.id.progress_bar);
        findViewById(R.id.facebook_button).setOnClickListener(this);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (PreferencesUtils.isSendToken(LoginActivity.this)) {
                    downloadData();
                } else {
                    progressBar.setVisibility(ProgressBar.GONE);
                    showDialog(getString(R.string.token_error_message));
                }
            }
        };
        if (PreferencesUtils.getFbId(this) != null && Utilities.isValidEmail(PreferencesUtils.getUserEmail(this))) {
            authStrongLoop();
        }
    }

    private void authFacebook() {
        Timber.i("authFacebook");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.i("onSuccess");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        String id = null;
                        try {
                            JSONObject graphObject = response.getJSONObject();
                            if (graphObject != null) {
                                if (graphObject.has("id")) {
                                    id = graphObject.getString("id");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (id != null) {
                            Timber.i("id = '%s%s%s", id, ", androidId = ", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                            PreferencesUtils.setFbId(LoginActivity.this, id);
                            PreferencesUtils.setUserEmail(LoginActivity.this, "q@mail.com");
                            authStrongLoop();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Timber.w("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Timber.e("onError");
                showDialog(getResources().getString(R.string.facebook_authentication_error));
            }
        });
    }

    private void startRegistrationService() {
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(LoginActivity.this, RegistrationIntentService.class);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.i("onActivityResult");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(StaticKeys.REGISTRATION_COMPLETE));
        registerReceiver(mUpdateReceiver, new IntentFilter(UpdateReceiver.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        unregisterReceiver(mUpdateReceiver);
        super.onPause();
    }

    private void downloadData() {
        if ((PreferencesUtils.getTimeUpdate(this) + StaticKeys.TIME_UPDATE) < System.currentTimeMillis()) {
            sendRequest(StaticKeys.REQUEST_START);
        } else {
            sendRequest(StaticKeys.REQUEST_CHECK);
        }
    }

    private void sendRequest(String request) {
        Intent intent = new Intent(this, UpdateService.class);
        intent.putExtra(StaticKeys.KEY_REQUEST, request);
        startService(intent);
    }

    @Override
    public void onHandleServerRequest() {
        Timber.i("onHandleServerRequest");
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onHandleServerRequestError() {
        showDialog(getString(R.string.server_connect_failure));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facebook_button:
                authFacebook();
                break;
        }
    }

    private void authStrongLoop() {
        progressBar.setVisibility(View.VISIBLE);
        //TODO: make one request with getAppId()
        final AccountsRepository userRepo = AppOfferFind.getRestAdapter(getApplicationContext()).createRepository(AccountsRepository.class);
        userRepo.createContract();
        userRepo.accounts(PreferencesUtils.getUserEmail(this), "q", new AccountsRepository.UserCallback() {
            @Override
            public void onSuccess(Accounts user) {
                Timber.i("onSuccess response=%s", user.toString());
                PreferencesUtils.setUserId(LoginActivity.this, user.getUserId());
                PreferencesUtils.setUserToken(LoginActivity.this, user.getId());
                getAppId();
            }

            @Override
            public void onError(Throwable t) {
                Timber.e("onError Throwable: %s", t.toString());
                showDialog(t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getAppId() {
        progressBar.setVisibility(View.VISIBLE);
        final FindOneRepository findOneRepository = AppOfferFind.getRestAdapter(getApplicationContext()).createRepository(FindOneRepository.class);
        findOneRepository.createContract();
        findOneRepository.findOne(new FindOneRepository.FindOneCallback() {
            @Override
            public void onSuccess(FindOne user) {
                Timber.i("onSuccess response=%s", user.toString());
                PreferencesUtils.setAppId(LoginActivity.this, user.getId());
                if (PreferencesUtils.isSendToken(LoginActivity.this)) {
                    downloadData();
                } else {
                    startRegistrationService();
                }
            }

            @Override
            public void onError(Throwable t) {
                Timber.e("onError Throwable: %s", t.toString());
                showDialog(t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}

