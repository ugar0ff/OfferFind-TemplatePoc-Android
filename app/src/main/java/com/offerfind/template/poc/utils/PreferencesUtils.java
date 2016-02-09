package com.offerfind.template.poc.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ugar on 09.02.16.
 */
public class PreferencesUtils {

    public final static String FB_ID = "fb_id";

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
}
