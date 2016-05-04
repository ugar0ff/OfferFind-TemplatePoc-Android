package co.mrktplaces.android.core.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

import co.mrktplaces.android.core.api.retrofit.Account;
import co.mrktplaces.android.core.api.strongloop.Location;
import co.mrktplaces.android.core.api.strongloop.Opportunities;
import co.mrktplaces.android.core.cache.CacheContentProvider;
import co.mrktplaces.android.core.cache.CacheHelper;

/**
 * Created by ugar on 04.05.16.
 */
public class OpportunitiesAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Opportunities.ModelOpportunity>> {

    private ArrayList<Opportunities.ModelOpportunity> opportunities;

    public OpportunitiesAsyncTaskLoader(Context context, ArrayList<Opportunities.ModelOpportunity> opportunities) {
        super(context);
        this.opportunities = opportunities;
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
//        cancelLoad();
    }

    @Override
    public ArrayList<Opportunities.ModelOpportunity> loadInBackground() {
        for (Opportunities.ModelOpportunity opportunity : opportunities) {
            opportunity.setAccounts(getAccount(opportunity.getOwnerId()));
        }
        return opportunities;
    }

    private Account getAccount(int ownerId) {
        Account account = new Account();
        String[] projection = new String[]{CacheHelper.OWNER_ID + " as " + CacheHelper._ID, CacheHelper.OWNER_NAME, CacheHelper.OWNER_AVATAR};
        String selection = CacheHelper.OWNER_ID + " = ?";
        String[] selectionArg = new String[]{String.valueOf(ownerId)};
        Cursor cursor = getContext().getContentResolver().query(CacheContentProvider.OWNER_URI, projection, selection, selectionArg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                account.setName(cursor.getString(cursor.getColumnIndex(CacheHelper.OWNER_NAME)));
                account.setAvatar(cursor.getString(cursor.getColumnIndex(CacheHelper.OWNER_AVATAR)));
            }
            cursor.close();
        }
        return account;
    }
}
