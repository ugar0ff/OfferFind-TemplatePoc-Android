package co.mrktplaces.android.core.gcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import co.mrktplaces.android.R;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.Arrays;
import java.util.List;

import co.mrktplaces.android.ui.activity.MainActivity;
import timber.log.Timber;

/**
 * Created by ugar on 22.02.16.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Timber.i("onMessageReceived");
        if (checkApp()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtra(MainActivity.START_INTENT, data);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        String message = data.getString("message");
        int id = Integer.valueOf(data.getString("id"));
        Timber.d("From: %s", from);
        Timber.d("Messages: %s", message);
        Timber.d("data: %s", data.toString());

        sendNotification(message, id);
    }

    private void sendNotification(String message, int id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.icon_logo)
                .setTicker("Ticker!")
                .setContentTitle("GCM Messages")
                .setContentText(message)
                .setNumber(4)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        Notification.InboxStyle inboxStyle = new Notification.InboxStyle(notificationBuilder);
        inboxStyle.setBigContentTitle("Big Text");
        for (int i = 0; i <= 3; i++) {
            inboxStyle.addLine("Line: " + i);
        }
        inboxStyle.setSummaryText("+3 more");
        Notification notification = inboxStyle.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    public boolean checkApp(){
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            ActivityManager.RunningAppProcessInfo processInfo = processInfos.get(0);
            return processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && Arrays.asList(processInfo.pkgList).get(0).equalsIgnoreCase(getPackageName());
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            return componentInfo.getPackageName().equalsIgnoreCase(getPackageName());
        }
    }
}
