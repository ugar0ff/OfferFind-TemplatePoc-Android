package com.offerfind.template.poc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.offerfind.template.poc.R;
import com.offerfind.template.poc.ui.fragment.base.BaseFragment;
import com.offerfind.template.poc.utils.PreferencesUtils;

import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by ugar on 09.02.16.
 */
public class LoginFragment extends BaseFragment {

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        authFacebook();
        return view;
    }

    private void authFacebook() {
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        callbackManager = CallbackManager.Factory.create();
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
                        if (id != null && getActivity() != null) {
                            Timber.i("id = '%s%s%s", id, ", androidId = ", Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
                            PreferencesUtils.setFbId(getActivity(), id);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
