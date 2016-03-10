package com.dddev.market.place.ui.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dddev.market.place.R;
import com.dddev.market.place.ui.model.GeoSearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by ugar on 09.03.16.
 */
public class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 15;
    private Context mContext;
    private List<GeoSearchResult> resultList;
    private LayoutInflater inflater;
    private Location location;

    public GeoAutoCompleteAdapter(Context context, Location location) {
        mContext = context;
        resultList = new ArrayList<>();
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.location = location;
    }

    @Override
    public int getCount() {
        return resultList == null ? 0 : resultList.size();
    }

    @Override
    public GeoSearchResult getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        public TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.geo_search_result, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.geo_search_result_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (getItem(position) != null && getItem(position).getAddress() != null) {
            viewHolder.textView.setText(getItem(position).getAddress());
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List locations = findLocations(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = locations;
                    filterResults.count = locations.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private List<GeoSearchResult> findLocations(Context context, String query_text) {

        List<GeoSearchResult> geo_search_results = new ArrayList<>();

        Geocoder geocoder = new Geocoder(context, context.getResources().getConfiguration().locale);
        List<Address> addresses = null;

        try {
            Timber.i("location == null ? %s", location == null);
            if (location == null) {
                addresses = geocoder.getFromLocationName(query_text, MAX_RESULTS);
            } else {
                addresses = geocoder.getFromLocationName(query_text, MAX_RESULTS, location.getLatitude() - 0.5, location.getLongitude() - 0.5, location.getLatitude() + 0.5, location.getLongitude() + 0.5);
//                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), MAX_RESULTS);
            }

            for (int i = 0; i < addresses.size(); i++) {
                Address address = addresses.get(i);
                if (address.getMaxAddressLineIndex() != -1) {
                    geo_search_results.add(new GeoSearchResult(address));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return geo_search_results;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
