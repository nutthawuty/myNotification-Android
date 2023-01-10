package com.example.mynotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mynotification.service.NotificationService;

import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

public class MyBackgroundService extends Service {
    public MyBackgroundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // try to created new task run every 2 sec
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        NotificationService instance = NotificationService.getInstance();
                        Random random = new Random();
                        while (true) {
                            Log.d("Service", "Service is running..." + MainActivity.notificationCount);
                            try {
                                Thread.sleep(30000);
                                instance.pushNotification(getApplicationContext(), getString(R.string.channel_id), "background service", "backgroud service running", MainActivity.notificationId, MainActivity.notificationCount);
                                MainActivity.notificationId = random.nextInt();
                                MainActivity.notificationCount = MainActivity.notificationCount +1;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}