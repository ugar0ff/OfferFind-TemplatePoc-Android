package co.mrktplaces.clients.android.ui.fragment.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import co.mrktplaces.clients.android.R;
import co.mrktplaces.clients.android.core.receiver.AddressResultReceiver;
import co.mrktplaces.clients.android.core.service.FetchAddressIntentService;
import co.mrktplaces.clients.android.utils.PreferencesUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import timber.log.Timber;

/**
 * Created by ugar on 09.03.16.
 */
public abstract class BaseLocationFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, AddressResultReceiver.AddressReceiveResult {

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected AddressResultReceiver mResultReceiver;
    protected static final int REQUEST_FINE_LOCATION = 0;

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    protected void startIntentService() {
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        mResultReceiver = new AddressResultReceiver(new Handler());
        mResultReceiver.setAddressReceiveResult(this);
        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, mLastLocation);
        getActivity().startService(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Timber.i("onConnected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Timber.i("mLastLocation == null ? %s", mLastLocation == null);
        if (mLastLocation == null) {
            LocationRequest mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)
                    .setFastestInterval(1 * 1000);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Timber.i("onLocationChanged location = %s", location.toString());
                }
            });
        } else {
            if (!Geocoder.isPresent()) {
                Toast.makeText(getActivity(), R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
                return;
            }
        }
        startIntentService();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.i("onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Timber.i("onConnectionFailed");
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    protected void getAddress() {
        if (PreferencesUtils.isLocaleCheckBoxEnable(getActivity())) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mLastLocation != null) {
                    startIntentService();
                } else {
                    buildGoogleApiClient();
                }
            } else {
                noLocation();
            }
        } else {
            Location location = new Location("");
            location.setLatitude(PreferencesUtils.getUserLatitude(getActivity()));
            location.setLongitude(PreferencesUtils.getUserLongitude(getActivity()));
            addressReceiveResult(PreferencesUtils.getUserAddress(getActivity()), location);
        }
    }

    protected void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{perm}, requestCode);
        }
    }

    protected abstract void noLocation();
}
