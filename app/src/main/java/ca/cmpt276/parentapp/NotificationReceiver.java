package ca.cmpt276.parentapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import static ca.cmpt276.parentapp.TimeoutTimer.NOTIFICATION_CHANNEL_ID;

import ca.cmpt276.parentapp.R;

/**
 * NotificationReceiver class:
 *
 * broadcast receiver for timeout timer pop-up notification
 */
public class NotificationReceiver extends BroadcastReceiver {
    public static String NOTIF_ID = "notifid";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Cool Down Timer",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription(context.getString(R.string.cool_down_timer_done));
            channel1.enableVibration(true);
            channel1.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            Uri soundUri = Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.blue_danube_alarm);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel1.setSound(soundUri, audioAttributes);
            notificationManager.createNotificationChannel(channel1);
        }
        int id = intent.getIntExtra(NOTIF_ID, 0);
        notificationManager.notify(id, notification);
    }
}
