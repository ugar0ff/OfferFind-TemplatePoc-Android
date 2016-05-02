package co.mrktplaces.android.ui.model;

import com.bricolsoftconsulting.geocoderplus.Address;

/**
 * Created by ugar on 09.03.16.
 */
public class GeoSearchResult {

    private Address address;

    public GeoSearchResult(Address address) {
        this.address = address;
    }

    public String getAddress() {
        String display_address = "";
        display_address += address.getFormattedAddress();
        return display_address;
    }

    public double getLatitude() {
        return address.getLatitude();
    }

    public double getLongitude() {
        return address.getLongitude();
    }

}
