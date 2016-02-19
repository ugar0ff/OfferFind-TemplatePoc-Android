package com.offerfind.template.poc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.offerfind.template.poc.R;
import com.offerfind.template.poc.core.AppOfferFind;
import com.offerfind.template.poc.core.api.strongloop.RestAdapter;
import com.offerfind.template.poc.core.api.strongloop.User;
import com.offerfind.template.poc.core.api.strongloop.UserRepository;
import com.offerfind.template.poc.ui.activity.base.BaseActivity;
import com.offerfind.template.poc.utils.PreferencesUtils;
import com.strongloop.android.loopback.AccessToken;

import org.json.JSONObject;

import java.util.Arrays;

import timber.log.Timber;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        if (savedInstanceState == null) {
            if (PreferencesUtils.getFbId(this) == null) {
            } else {
                startMainActivity();
            }
        }
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        findViewById(R.id.facebook_button).setOnClickListener(this);
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
                            strongLoop();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name");
                request.setParameters(parameters);
                request.executeAsync();
                startMainActivity();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.i("onActivityResult");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void startMainActivity() {
        strongLoop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facebook_button:
                authFacebook();
                break;
        }
    }

    private void strongLoop() {
        final UserRepository userRepo = AppOfferFind.getRestAdapter(getApplicationContext()).createRepository(UserRepository.class);
        userRepo.createContract();
        userRepo.user("q@example.com", "q", new UserRepository.UserCallback() {
            @Override
            public void onSuccess(User user) {
                Timber.i("onSuccess response=%s", user.toString());
                PreferencesUtils.setUserId(LoginActivity.this, user.getUserId());
                PreferencesUtils.setUserToken(LoginActivity.this, user.getId());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(Throwable t) {
                Timber.e("onError Throwable: %s", t.toString());
            }
        });
    }
}

