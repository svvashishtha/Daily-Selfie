package com.example.saurabh.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Saurabh on 11/24/2014.
 */
public class AlarmNotificationReceiver extends BroadcastReceiver{
    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;
    private static final int MY_NOTIFICATION_ID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {

        mNotificationIntent = new Intent(context, MainActivity.class);
        mContentIntent = PendingIntent.getActivity(context, 0,
                mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        Notification.Builder notificationBuilder = new Notification.Builder(
                context).setSmallIcon(R.drawable.ic_action_camera)
                .setAutoCancel(true).setContentTitle("Take a Selfie")
                .setContentText("Daily Selfie").setContentIntent(mContentIntent);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());
        Log.i("", "Sending notification at:"
                + DateFormat.getDateTimeInstance().format(new Date()));
    }
}
