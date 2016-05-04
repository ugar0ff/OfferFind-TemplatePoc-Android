package co.mrktplaces.android.core.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import co.mrktplaces.android.core.api.retrofit.Account;
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
        List<Integer> skipList = skipOpportunities();
        for (int i = opportunities.size() - 1; i >= 0; i--) {
            if (skipList.contains(opportunities.get(i).getId())) {
                opportunities.remove(i);
            } else {
                opportunities.get(i).setAccounts(getAccount(opportunities.get(i).getOwnerId()));
            }
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

    private List<Integer> skipOpportunities() {
        List<Integer> list = new ArrayList<>();
        Cursor cursor = getContext().getContentResolver().query(CacheContentProvider.SKIP_URI, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getInt(cursor.getColumnIndex(CacheHelper.SKIP_OPPORTUNITIES_ID)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }
}
