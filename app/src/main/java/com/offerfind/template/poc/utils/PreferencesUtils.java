package com.offerfind.template.poc.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ugar on 09.02.16.
 */
public class PreferencesUtils {

    public final static String FB_ID = "fb_id";
    public final static String USER_ID = "user_id";
    public final static String USER_TOKEN = "user_token";

    private static String getPrefName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    public static boolean setFbId(Context context, String id) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.edit().putString(FB_ID, id).commit();
    }

    public static String getFbId(Context context) {
        if (context == null) {
            return null;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.getString(FB_ID, null);
    }

    public static boolean setUserId(Context context, int id) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.edit().putInt(USER_ID, id).commit();
    }

    public static int getUserId(Context context) {
        if (context == null) {
            return 0;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.getInt(USER_ID, 0);
    }

    public static boolean setUserToken(Context context, String token) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.edit().putString(USER_TOKEN, token).commit();
    }

    public static String getUserToken(Context context) {
        if (context == null) {
            return null;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.getString(USER_TOKEN, null);
    }
}
