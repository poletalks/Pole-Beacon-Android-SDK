package org.poletalks.sdk.pole_android_sdk.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.poletalks.sdk.pole_android_sdk.FeedbackActivity;
import org.poletalks.sdk.pole_android_sdk.R;

import java.util.Map;


/**
 * Created by kuku on 29/9/16.
 */
public class MyFcmListenerService extends FirebaseMessagingService {

    Map data;
    private String channelName;
    Context mContext = this;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        data = message.getData();
        if (PoleNotificationService.onMessageReceived(message, mContext)){
            generateNotification(data);
        }
    }

    private void generateNotification(Map data) {
        String type = (String) data.get("type");
        String title = (String) data.get("title");
        String content = (String) data.get("content");
        channelName = (String) data.get("channelName");
        if (data.get("description") != null) {
            String description = (String) data.get("description");
        }
        if (data.get("time") != null) {
            double time = Double.parseDouble((String) data.get("time"));
        }

        createNotification(title, content);
    }

    private void createNotification(String title, String content) {
        try {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
            Intent resultIntent = new Intent(mContext, FeedbackActivity.class);

            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.brandlog);
            Integer notificationId = Integer.valueOf(String.valueOf((System.currentTimeMillis() / 1000000)));

            mBuilder.setSmallIcon(R.drawable.small_icon)
                    .setLargeIcon(icon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setOnlyAlertOnce(true);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(content);
            mBuilder.setStyle(bigTextStyle);

            SharedPreferences pref = mContext.getSharedPreferences("notification", Context.MODE_PRIVATE);
            int totalNotificationCount = pref.getInt("totalNotificationCount",0);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(totalNotificationCount, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(pendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setVibrate(new long[]{100});
            mBuilder.setAutoCancel(true);
            mNotificationManager.notify(notificationId, mBuilder.build());
        } catch (Exception e){
        }

    }


}
