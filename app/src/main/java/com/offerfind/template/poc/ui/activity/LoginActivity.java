package com.offerfind.template.poc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.activity.base.BaseActivity;
import com.offerfind.template.poc.utils.PreferencesUtils;

import org.json.JSONObject;

import timber.log.Timber;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;

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
        loginButton = (LoginButton) findViewById(R.id.login_button);
        authFacebook();
    }

    private void authFacebook() {
        Timber.i("authFacebook");
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}

