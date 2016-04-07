package com.dddev.market.place.core;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.dddev.market.place.BuildConfig;
import com.dddev.market.place.core.api.strongloop.RestAdapter;
import com.dddev.market.place.core.service.StreamService;
import com.dddev.market.place.utils.PreferencesUtils;

import timber.log.Timber;

/**
 * Created by ugar on 09.02.16.
 */
public class AppOfferFind extends Application {

    public final static String API = "http://178.62.252.200/api/";
    private Intent streamServiceIntent;

    public static RestAdapter getRestAdapter(Context context) {
        final RestAdapter restAdapter = new RestAdapter(context, API);
        restAdapter.getClientAdapter().removeAllHeaders();
        restAdapter.getClientAdapter().addHeader("Accept", "application/json");
        restAdapter.getClientAdapter().addHeader("Content-Type", "application/json; charset=utf-8");
        if (PreferencesUtils.getUserToken(context) != null) {
            restAdapter.setAccessToken(PreferencesUtils.getUserToken(context));
        }
        return restAdapter;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {

        }
    }

    public Intent getStreamServiceIntent() {
        if (streamServiceIntent == null) {
            streamServiceIntent = new Intent(getApplicationContext(), StreamService.class);
        }
        return streamServiceIntent;
    }
}
