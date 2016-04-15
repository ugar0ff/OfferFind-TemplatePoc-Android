package com.dddev.market.place.core.gcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.dddev.market.place.R;
import com.dddev.market.place.ui.activity.MainActivity;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ugar on 22.02.16.
 */
public class MyGcmListenerService extends GcmListenerService {

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        if (checkApp()) {
            return;
        }
        String message = data.getString("message");
        String id = data.getString("id");
        Timber.d("From: %s", from);
        Timber.d("Messages: %s", message);
        Timber.d("data: %s", data.toString());

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message, id);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, String id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_logo)
                .setContentTitle("GCM Messages")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
//                .setStyle(getExpanded());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

//    private NotificationCompat.InboxStyle getExpanded() {
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle.setBigContentTitle("Big Text");
//        for (int i = 0; i <= 3; i++) {
//            inboxStyle.addLine("Line: " + i);
//        }
//        return inboxStyle;
//    }

    public boolean checkApp(){
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            ActivityManager.RunningAppProcessInfo processInfo = processInfos.get(0);
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (Arrays.asList(processInfo.pkgList).get(0).equalsIgnoreCase(getPackageName())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equalsIgnoreCase(getPackageName())) {
                return true;
            } else {
                return false;
            }
        }
    }
}
