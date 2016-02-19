package com.offerfind.template.poc.core;

import android.app.Application;
import android.content.Context;

import com.offerfind.template.poc.BuildConfig;
import com.offerfind.template.poc.core.api.strongloop.RestAdapter;

import timber.log.Timber;

/**
 * Created by ugar on 09.02.16.
 */
public class AppOfferFind extends Application {

    public static RestAdapter getRestAdapter(Context context) {
        final RestAdapter restAdapter = new RestAdapter(context, "http://178.62.252.200:3000/api/");
        restAdapter.getClientAdapter().removeAllHeaders();
        restAdapter.getClientAdapter().addHeader("Accept", "application/json");
        restAdapter.getClientAdapter().addHeader("Content-Type", "application/json; charset=utf-8");
        return restAdapter;
    }

    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {

        }
    }

}
