
package ca.cmpt276.parentapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadcast extends Application {
     public static final String CHANNEL_1_ID = "AlarmChannel";
     public static final String CHANNEL_2_ID = "AlarmChannel2";

     @Override
     public void onCreate() {
          super.onCreate();
          createNotificationChannels();
     }

     private void createNotificationChannels() {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
               NotificationChannel channel1 = new NotificationChannel(
                       CHANNEL_1_ID,
                       "Cool Down Timer",
                       NotificationManager.IMPORTANCE_HIGH
               );
               channel1.setDescription("Your cool down timer is up!");
               channel1.enableVibration(true);
               channel1.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
               NotificationChannel channel2 = new NotificationChannel(
                       CHANNEL_2_ID,
                       "Cool Down Timer 2",
                       NotificationManager.IMPORTANCE_LOW
               );
               channel2.setDescription("2 Your cool down timer is up!");
               channel2.enableVibration(true);
               channel2.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

               NotificationManager manager = getSystemService(NotificationManager.class);
               manager.createNotificationChannel(channel1);
               manager.createNotificationChannel(channel2);

          }
     }
}