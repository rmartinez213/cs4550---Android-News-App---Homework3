package com.example.rkjc.news_app_2;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationApp extends Application {
    public static final String Channel_1_id = "channel1";


    @Override
    public void onCreate(){
        super.onCreate();

        createNotificationChannels();
    }

    //Creates Notification Channels
    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    Channel_1_id,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channel1.setDescription("Your News Application has been synced!");


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
