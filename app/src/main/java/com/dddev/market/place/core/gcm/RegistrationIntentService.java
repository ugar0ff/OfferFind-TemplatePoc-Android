package com.dddev.market.place.core.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import com.dddev.market.place.R;
import com.dddev.market.place.core.AppOfferFind;
import com.dddev.market.place.core.api.strongloop.Installation;
import com.dddev.market.place.core.api.strongloop.InstallationRepository;
import com.dddev.market.place.utils.PreferencesUtils;
import com.dddev.market.place.utils.StaticKeys;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import timber.log.Timber;

/**
 * Created by ugar on 22.02.16.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Timber.i("GCM Registration Token: %s", token);
            sendRegistrationToServer(token);
            subscribeTopics(token);
        } catch (Exception e) {
            Timber.e("Failed to complete token refresh " + e);
            PreferencesUtils.setSendToken(getApplicationContext(), false);
            sendRegistrationComplete();
        }
    }

    private void sendRegistrationComplete() {
        Intent registrationComplete = new Intent(StaticKeys.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final InstallationRepository installationRepository = AppOfferFind.getRestAdapter(getApplicationContext()).createRepository(InstallationRepository.class);
                installationRepository.createContract();
                installationRepository.install(token, new InstallationRepository.InstallationCallback() {
                    @Override
                    public void onSuccess(Installation install) {
                        Timber.i("onSuccess response=%s", install.toString());
                        PreferencesUtils.setSendToken(getApplicationContext(), true);
                        // Subscribe to topic channels
                        sendRegistrationComplete();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e("onError Throwable: %s", t.toString());
                        PreferencesUtils.setSendToken(getApplicationContext(), false);
                        sendRegistrationComplete();
                    }
                });
            }
        });
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            try {
                pubSub.subscribe(token, "/topics/" + topic, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // [END subscribe_topics]
}
