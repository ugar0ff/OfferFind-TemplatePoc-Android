package com.dddev.market.place.core.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ugar on 24.02.16.
 */
public class CacheHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cache_db";
    public static final int DB_VERSION = 1;

    public static final String _ID = "_id";

    public static final String TABLE_OPPORTUNITIES = "opportunities";
    public static final String OPPORTUNITIES_ID = "opportunities_id";
    public static final String OPPORTUNITIES_TITLE = "opportunities_title";
    public static final String OPPORTUNITIES_DESCRIPTION = "opportunities_description";
    public static final String OPPORTUNITIES_ACCOUNT_ID = "opportunities_account_id";
    public static final String OPPORTUNITIES_DATE = "opportunities_date";
    public static final String OPPORTUNITIES_STATUS = "opportunities_status";
    private final String CREATE_OPPORTUNITIES = "CREATE TABLE " + TABLE_OPPORTUNITIES + " (" + OPPORTUNITIES_ID + " integer primary key, " + OPPORTUNITIES_TITLE + " text, " + OPPORTUNITIES_DESCRIPTION + " text, " +
            OPPORTUNITIES_ACCOUNT_ID + " integer, " + OPPORTUNITIES_DATE + " integer, " + OPPORTUNITIES_STATUS + " integer);";
    private final String DROP_OPPORTUNITIES = "DROP TABLE IF EXISTS " + TABLE_OPPORTUNITIES + ";";

    public static final String TABLE_BIDS = "bids";
    public static final String BIDS_ID = "bids_id";
    public static final String BIDS_TITLE = "bids_title";
    public static final String BIDS_DESCRIPTION = "bids_description";
    public static final String BIDS_URL = "bids_url";
    public static final String BIDS_PRICE = "bids_price";
    public static final String BIDS_OPPORTUNITIES_ID = "bids_opportunities_id";
    private final String CREATE_BIDS = "CREATE TABLE " + TABLE_BIDS + " (" + BIDS_ID + " integer primary key, " + BIDS_TITLE + " text, " + BIDS_DESCRIPTION + " text, " +
            BIDS_URL + " text, " + BIDS_PRICE + " real, " + BIDS_OPPORTUNITIES_ID + " integer);";
    private final String DROP_BIDS = "DROP TABLE IF EXISTS " + TABLE_BIDS + ";";

    public static final String TABLE_CATEGORY = "category";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_TITLE = "category_title";
    public static final String CATEGORY_DESCRIPTION = "category_description";
    public static final String CATEGORY_IMAGE_URL = "category_image_url";
    private final String CREATE_WORK_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " (" + CATEGORY_ID + " integer primary key, " + CATEGORY_TITLE + " text, " +
            CATEGORY_DESCRIPTION + " text, " + CATEGORY_IMAGE_URL + " text);";
    private final String DROP_WORK_CATEGORY = "DROP TABLE IF EXISTS " + TABLE_CATEGORY + ";";

    public CacheHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_OPPORTUNITIES);
        db.execSQL(CREATE_BIDS);
        db.execSQL(CREATE_WORK_CATEGORY);
        setCategory(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        switch (oldVersion) {
//            case 1:
//                db.execSQL(DROP_OPPORTUNITIES);
//                db.execSQL(CREATE_OPPORTUNITIES);
//        }
    }

    private void setCategory(SQLiteDatabase db) {
        db.execSQL("REPLACE INTO " + CacheHelper.TABLE_CATEGORY + " VALUES (1, 'Basic (regular)\ncleaning', 'You d like us to clean for you.\nBasic cleaning will make your home sparkle', null);");
        db.execSQL("REPLACE INTO " + CacheHelper.TABLE_CATEGORY + " VALUES (1, 'Move-in or move-out\ncleaning', 'A move-in or move-out cleaning is a great way to ease the stress of packing and moving', null);");
        db.execSQL("REPLACE INTO " + CacheHelper.TABLE_CATEGORY + " VALUES (1, 'Construction\nclean-up', 'When your contractor has left your home in what the industry calls a broom clean state', null);");
    }

}
