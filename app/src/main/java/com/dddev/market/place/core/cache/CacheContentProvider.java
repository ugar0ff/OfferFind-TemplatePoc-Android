package com.dddev.market.place.core.cache;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by ugar on 24.02.16.
 */
public class CacheContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.dddev.market.place.core.cache";

    private static final String OPPORTUNITIES_PATCH = "opportunities";
    public static final Uri OPPORTUNITIES_URI = Uri.parse("content://" + AUTHORITY + "/" + OPPORTUNITIES_PATCH);
    private static final int OPPORTUNITIES = 1;
    private static final int OPPORTUNITIES_ID = 2;

    private static final String BIDS_PATCH = "bids";
    public static final Uri BIDS_URI = Uri.parse("content://" + AUTHORITY + "/" + BIDS_PATCH);
    private static final int BIDS = 3;
    private static final int BIDS_ID = 4;

    private static final String CATEGORY_PATCH = "category";
    public static final Uri CATEGORY_URI = Uri.parse("content://" + AUTHORITY + "/" + CATEGORY_PATCH);
    private static final int CATEGORY = 5;
    private static final int CATEGORY_ID = 6;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, OPPORTUNITIES_PATCH, OPPORTUNITIES);
        uriMatcher.addURI(AUTHORITY, OPPORTUNITIES_PATCH + "/#", OPPORTUNITIES_ID);
        uriMatcher.addURI(AUTHORITY, BIDS_PATCH, BIDS);
        uriMatcher.addURI(AUTHORITY, BIDS_PATCH + "/#", BIDS_ID);
        uriMatcher.addURI(AUTHORITY, CATEGORY_PATCH, CATEGORY);
        uriMatcher.addURI(AUTHORITY, CATEGORY_PATCH + "/#", CATEGORY_ID);
    }

    private CacheHelper cacheHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public boolean onCreate() {
        cacheHelper = new CacheHelper(getContext(), CacheHelper.DATABASE_NAME, null, CacheHelper.DB_VERSION);
        sqLiteDatabase = cacheHelper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String table = null;
        switch (uriMatcher.match(uri)) {
            case OPPORTUNITIES:
                table = CacheHelper.TABLE_OPPORTUNITIES;
                break;
            case OPPORTUNITIES_ID:
                table = CacheHelper.TABLE_OPPORTUNITIES;
                selection = CacheHelper.OPPORTUNITIES_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case BIDS:
                table = CacheHelper.TABLE_BIDS;
                break;
            case BIDS_ID:
                table = CacheHelper.TABLE_BIDS;
                selection = CacheHelper.BIDS_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case CATEGORY:
                table = CacheHelper.TABLE_CATEGORY;
                break;
            case CATEGORY_ID:
                table = CacheHelper.TABLE_CATEGORY;
                selection = CacheHelper.CATEGORY_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        Cursor cursor;
        if (table != null) {
            cursor = sqLiteDatabase.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        } else {
            return null;
        }
        if (cursor != null && getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        Uri currentUri = null;
        long id = -1;
        switch (uriMatcher.match(uri)) {
            case OPPORTUNITIES:
                id = sqLiteDatabase.replace(CacheHelper.TABLE_OPPORTUNITIES, null, values);
                if (id >= 0) {
                    _uri = ContentUris.withAppendedId(OPPORTUNITIES_URI, id);
                    currentUri = OPPORTUNITIES_URI;
                }
                break;
            case BIDS:
                id = sqLiteDatabase.replace(CacheHelper.TABLE_BIDS, null, values);
                if (id >= 0) {
                    _uri = ContentUris.withAppendedId(BIDS_URI, id);
                    currentUri = BIDS_URI;
                }
                break;
            case CATEGORY:
                id = sqLiteDatabase.replace(CacheHelper.TABLE_CATEGORY, null, values);
                if (id >= 0) {
                    _uri = ContentUris.withAppendedId(CATEGORY_URI, id);
                    currentUri = CATEGORY_URI;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        if (_uri != null && getContext() != null) {
            getContext().getContentResolver().notifyChange(_uri, null);
            getContext().getContentResolver().notifyChange(currentUri, null);
        }
        return _uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        String id;
        switch (uriMatcher.match(uri)) {
            case OPPORTUNITIES:
                rowsDeleted = sqLiteDatabase.delete(CacheHelper.TABLE_OPPORTUNITIES, selection, selectionArgs);
                break;
            case OPPORTUNITIES_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqLiteDatabase.delete(CacheHelper.TABLE_OPPORTUNITIES,
                            CacheHelper.OPPORTUNITIES_ID + " = " + id, null);
                } else {
                    rowsDeleted = sqLiteDatabase.delete(CacheHelper.TABLE_OPPORTUNITIES,
                            CacheHelper.OPPORTUNITIES_ID + " = " + id + " and " + selection, selectionArgs);
                }
                break;
            case BIDS:
                rowsDeleted = sqLiteDatabase.delete(CacheHelper.TABLE_BIDS, selection, selectionArgs);
                break;
            case BIDS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqLiteDatabase.delete(CacheHelper.TABLE_BIDS,
                            CacheHelper.BIDS_ID + " = " + id, null);
                } else {
                    rowsDeleted = sqLiteDatabase.delete(CacheHelper.TABLE_BIDS,
                            CacheHelper.BIDS_ID + " = " + id + " and " + selection, selectionArgs);
                }
                break;
            case CATEGORY:
                rowsDeleted = sqLiteDatabase.delete(CacheHelper.TABLE_CATEGORY, selection, selectionArgs);
                break;
            case CATEGORY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqLiteDatabase.delete(CacheHelper.TABLE_CATEGORY,
                            CacheHelper.CATEGORY_ID + " = " + id, null);
                } else {
                    rowsDeleted = sqLiteDatabase.delete(CacheHelper.TABLE_CATEGORY,
                            CacheHelper.CATEGORY_ID + " = " + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;
        Uri _uri = null;
        String id;
        switch (uriMatcher.match(uri)) {
            case OPPORTUNITIES:
                rowsUpdated = sqLiteDatabase.update(CacheHelper.TABLE_OPPORTUNITIES, values, selection, selectionArgs);
                break;
            case OPPORTUNITIES_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqLiteDatabase.update(CacheHelper.TABLE_OPPORTUNITIES, values,
                            CacheHelper.OPPORTUNITIES_ID + " = " + id, null);
                } else {
                    rowsUpdated = sqLiteDatabase.update(CacheHelper.TABLE_OPPORTUNITIES, values,
                            CacheHelper.OPPORTUNITIES_ID + " = " + id + " and " + selection, selectionArgs);
                }
                break;
            case BIDS:
                rowsUpdated = sqLiteDatabase.update(CacheHelper.TABLE_BIDS, values, selection, selectionArgs);
                break;
            case BIDS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqLiteDatabase.update(CacheHelper.TABLE_BIDS, values,
                            CacheHelper.BIDS_ID + " = " + id, null);
                } else {
                    rowsUpdated = sqLiteDatabase.update(CacheHelper.TABLE_BIDS, values,
                            CacheHelper.BIDS_ID + " = " + id + " and " + selection, selectionArgs);
                }
                break;
            case CATEGORY:
                rowsUpdated = sqLiteDatabase.update(CacheHelper.TABLE_CATEGORY, values, selection, selectionArgs);
                break;
            case CATEGORY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqLiteDatabase.update(CacheHelper.TABLE_CATEGORY, values,
                            CacheHelper.CATEGORY_ID + " = " + id, null);
                } else {
                    rowsUpdated = sqLiteDatabase.update(CacheHelper.TABLE_CATEGORY, values,
                            CacheHelper.CATEGORY_ID + " = " + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
            if (_uri != null) {
                getContext().getContentResolver().notifyChange(_uri, null);
            }
        }
        return rowsUpdated;
    }

}
