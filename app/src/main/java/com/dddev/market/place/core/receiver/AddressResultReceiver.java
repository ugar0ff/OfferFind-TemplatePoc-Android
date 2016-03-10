package com.dddev.market.place.core.receiver;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

import com.dddev.market.place.core.service.FetchAddressIntentService;

import timber.log.Timber;

/**
 * Created by ugar on 09.03.16.
 */
public class AddressResultReceiver extends ResultReceiver {

    private AddressReceiveResult addressReceiveResult;

    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        addressReceiveResult.addressReceiveResult(resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY));
        addressReceiveResult.locationReceiveResult((Location) resultData.getParcelable(FetchAddressIntentService.LOCATION_DATA_EXTRA));
        Timber.i(resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY));
    }

    public void setAddressReceiveResult(AddressReceiveResult addressReceiveResult) {
        this.addressReceiveResult = addressReceiveResult;
    }

    public interface AddressReceiveResult {
        void addressReceiveResult(String result);
        void locationReceiveResult(Location location);
    }
}
