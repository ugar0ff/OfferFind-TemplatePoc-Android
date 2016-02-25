package com.dddev.market.place.core.api.strongloop;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by ugar on 18.02.16.
 */
public class RestAdapter extends com.strongloop.android.loopback.RestAdapter {

    public RestAdapter(Context context, String url) {
        super(context, url);
    }

    @Override
    protected AsyncHttpClient getClient() {
        return super.getClient();
    }

    public AsyncHttpClient getClientAdapter() {
        return getClient();
    }
}
