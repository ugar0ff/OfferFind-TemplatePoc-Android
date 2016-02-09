package com.offerfind.template.poc.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.security.MessageDigest;

import timber.log.Timber;

/**
 * Created by ugar on 09.02.16.
 */
public class Utilities {

    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Timber.i("hash key = '%s'", something);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
