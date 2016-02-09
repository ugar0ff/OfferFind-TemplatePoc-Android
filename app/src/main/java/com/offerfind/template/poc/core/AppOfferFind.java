package com.offerfind.template.poc.core;

import android.app.Application;

import com.offerfind.template.poc.BuildConfig;

import timber.log.Timber;

/**
 * Created by ugar on 09.02.16.
 */
public class AppOfferFind extends Application {
    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {

        }
    }

}
