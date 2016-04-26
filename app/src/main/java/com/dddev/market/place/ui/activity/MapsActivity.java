package com.dddev.market.place.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.ui.views.TouchableWrapper;
import com.dddev.market.place.utils.StaticKeys;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView addressView;
    private Geocoder geocoder;
    private float downX, downY;
    private float previousZoomLevel;
    private boolean needChangeAddress;
    private SupportMapFragment mapFragment;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MapsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        TouchableWrapper touchView = (TouchableWrapper) findViewById(R.id.touchWrapper);
        touchView.setTouchEventListener(touchEvent);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        addressView = (TextView) findViewById(R.id.address);
        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
    }

    private TouchEvent touchEvent = new TouchEvent() {
        @Override
        public void eventTouch(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = ev.getX();
                    downY = ev.getY();
                    needChangeAddress = false;
                    break;
                case MotionEvent.ACTION_UP:
                    if (addressView.getVisibility() == View.GONE) {
                        needChangeAddress = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (addressView.getVisibility() == View.VISIBLE &&
                            (downX > (ev.getX() + StaticKeys.MAP_MOVE_DELTA) ||
                                    downX < (ev.getX() - StaticKeys.MAP_MOVE_DELTA) ||
                                    downY > (ev.getY() + StaticKeys.MAP_MOVE_DELTA) ||
                                    downY < (ev.getY() - StaticKeys.MAP_MOVE_DELTA))) {
                        addressView.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(arg0.getLatitude(),
                            arg0.getLongitude())).zoom(15).build());
                    mMap.moveCamera(myLoc);
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(), arg0.getLongitude())));
                    mMap.setOnMyLocationChangeListener(null);
                    mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition cameraPosition) {
                            if (previousZoomLevel != cameraPosition.zoom || needChangeAddress) {
                                changeAddress();
                                addressView.setVisibility(View.VISIBLE);
                            }
                            previousZoomLevel = cameraPosition.zoom;
                        }
                    });
                    mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            addressView.setVisibility(View.GONE);
                            needChangeAddress = true;
                            return false;
                        }
                    });
                }
            });
        }
    }

    private void changeAddress() {
        LatLng center = mMap.getCameraPosition().target;
        List<Address> addresses;
        String address = null;
        try {
            addresses = geocoder.getFromLocation(center.latitude, center.longitude, 1);
            if (addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address != null && address.length() > 0) {
            addressView.setText(address);
        }
    }

    public interface TouchEvent {
        void eventTouch(MotionEvent ev);
    }
}
