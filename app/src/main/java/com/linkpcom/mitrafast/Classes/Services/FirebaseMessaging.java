package com.linkpcom.mitrafast.Classes.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;
import com.linkpcom.mitrafast.MVVM.Agent.InProgressOrder.InProgressOrderActivity;
import com.linkpcom.mitrafast.MVVM.Client.OrderDetails.OrderDetailsActivity;
import com.linkpcom.mitrafast.MVVM.Common.SplashScreen.SplashScreenActivity;
import com.linkpcom.mitrafast.R;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class FirebaseMessaging extends FirebaseMessagingService {

    private final static String NOTIFICATION_CHANNEL_NAME = "notification_firebase_channel_id";
    private final static String NOTIFICATION_CHANNEL_DES = "notification_firebase_channel_desc";


    private String type_id;
    private String item_id;
    @Inject
    PreferencesManager preferencesManager;


    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        Timber.d("Shady Notification: %s", remoteMessage.getData());
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        type_id = remoteMessage.getData().get("type_id");
        item_id = remoteMessage.getData().get("item_id");

        if (preferencesManager != null)
            if (title != null && body != null) {
                buildNotification(title, body);
            }
    }

    private void buildNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = getResources().getString(R.string.default_notification_channel_id);

        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.ringtone);
        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();


            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_DES);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(Color.GRAY);
            notificationChannel.enableLights(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), attributes);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            notificationChannel.setSound(soundUri, audioAttributes);

            if (mNotificationManager != null)
                notificationManager.createNotificationChannel(notificationChannel);


        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);


        notificationBuilder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_mobile_notifications)
                .setContentTitle(title)
                .setContentText(body)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.ringtone))
                .setContentIntent(getIntent())
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setPriority(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? NotificationManager.IMPORTANCE_HIGH : Notification.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setLights(Color.RED, 3000, 3000);

        Random random = new Random();
        notificationManager.notify(random.nextInt(), notificationBuilder.build());
    }


// NEWORDER  = '1';

    PendingIntent getIntent() {
        Intent intent;
        if (preferencesManager.getUserTypeId().equals("3")) {
            if (item_id != null) {
                intent = new Intent(this, InProgressOrderActivity.class);
                intent.putExtra(CONSTANTS.INTENTS.ID, Integer.parseInt(item_id));
            } else
                intent = new Intent(this, SplashScreenActivity.class);

        } else {
            if (item_id != null) {
                if (type_id.equals("1")) {
                    intent = new Intent(this, OrderDetailsActivity.class);
                    intent.putExtra(CONSTANTS.INTENTS.ID, Integer.parseInt(item_id));
                } else {
                    intent = new Intent(this, SplashScreenActivity.class);
                }
            } else {
                intent = new Intent(this, SplashScreenActivity.class);
            }

        }

        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }
}
