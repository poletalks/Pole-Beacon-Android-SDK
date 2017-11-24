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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.poletalks.sdk.pole_android_sdk.FeedbackPage.FeedbackSDKActivity;
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
        String item_id = (String) data.get("item_id");
        String item_type = (String) data.get("item_type");

        channelName = (String) data.get("channelName");
        if (data.get("description") != null) {
            String description = (String) data.get("description");
        }
        if (data.get("time") != null) {
            double time = Double.parseDouble((String) data.get("time"));
        }

//        createNotification(title, content);
        PoleNotificationService.createNotification(title, content, item_id, item_type, mContext);
    }
}
