package com.dddev.market.place.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ugar on 09.02.16.
 */
public class PreferencesUtils {

    public final static String FB_ID = "fb_id";
    public final static String USER_ID = "user_id";
    public final static String USER_EMAIL = "user_email";
    public final static String USER_TOKEN = "user_token";
    public final static String APP_ID = "app_id";
    public static final String SENT_TOKEN_TO_SERVER = "sent_token_to_server";
    public static final String TIME_UPDATE = "time_update";
    public static final String LOCALE_CHECK_BOX_STATE = "locale_check_box_state";

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

    public static boolean setAppId(Context context, String appId) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.edit().putString(APP_ID, appId).commit();
    }

    public static String getAppId(Context context) {
        if (context == null) {
            return null;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.getString(APP_ID, null);
    }

    public static boolean setUserEmail(Context context, String appId) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.edit().putString(USER_EMAIL, appId).commit();
    }

    public static String getUserEmail(Context context) {
        if (context == null) {
            return null;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.getString(USER_EMAIL, null);
    }

    public static boolean setSendToken(Context context, boolean send) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.edit().putBoolean(SENT_TOKEN_TO_SERVER, send).commit();
    }

    public static boolean isSendToken(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(SENT_TOKEN_TO_SERVER, false);
    }

    public static boolean setTimeUpdate(Context context, long time) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.edit().putLong(TIME_UPDATE, time).commit();
    }

    public static long getTimeUpdate(Context context) {
        if (context == null) {
            return 0;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.getLong(TIME_UPDATE, 0);
    }

    public static boolean setLocaleCheckBoxState(Context context, boolean send) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.edit().putBoolean(LOCALE_CHECK_BOX_STATE, send).commit();
    }

    public static boolean isLocaleCheckBoxEnable(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(LOCALE_CHECK_BOX_STATE, false);
    }
}
