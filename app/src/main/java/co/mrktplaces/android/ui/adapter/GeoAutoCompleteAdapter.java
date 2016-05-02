package co.mrktplaces.android.ui.adapter;

import android.content.Context;
import com.bricolsoftconsulting.geocoderplus.Address;
import com.bricolsoftconsulting.geocoderplus.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import co.mrktplaces.android.R;
import co.mrktplaces.android.ui.model.GeoSearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        Geocoder geocoder = new Geocoder(context.getResources().getConfiguration().locale);
        geocoder.setUseRegionBias(false);
        List<Address> addresses;

        try {
            Timber.i("location == null ? %s", location == null);
                addresses = geocoder.getFromLocationName(query_text);

            if (addresses != null) {
                for (int i = 0; i < addresses.size(); i++) {
                    Address address = addresses.get(i);
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
