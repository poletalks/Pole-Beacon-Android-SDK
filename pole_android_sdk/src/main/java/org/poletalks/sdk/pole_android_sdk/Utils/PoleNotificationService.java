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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;

import org.poletalks.sdk.pole_android_sdk.FeedbackActivity;
import org.poletalks.sdk.pole_android_sdk.Model.Queue;
import org.poletalks.sdk.pole_android_sdk.PoleProximityManager;
import org.poletalks.sdk.pole_android_sdk.R;

import java.io.IOException;
import java.util.Map;

/**
 * Created by anjal on 11/1/17.
 */

public class PoleNotificationService {

    public static String getToken(FirebaseInstanceId firebaseInstanceId, Context context) {
        String refreshedToken = null;
        try {
            refreshedToken = firebaseInstanceId.getToken("732877727331", "FCM");
            Log.d("MyInstanceId", "sdk:Refreshed token: " + refreshedToken);
            sendRegistrationToServer(refreshedToken, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO: Implement this method to send any registration to your app's servers.
        return (refreshedToken);
    }

    private static void sendRegistrationToServer(String refreshedToken, Context context) {
        SharedPreferences pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);
        String fcm_token = pref.getString("fcm_token", "none");

        setInFirebase("fcm_token", "sdsdsf", 3434, true, context);

//        if (CheckNetwork.isInternetAvailable(getApplicationContext()))
//        {
//            RetrofitConfig retrofitConfig = new RetrofitConfig(getApplicationContext());
//            Retrofit retro = retrofitConfig.getRetro();
//            ApiInterface setprofile = retro.create(ApiInterface.class);
//            UserProfile user = new UserProfile();
//            user.setGcm(refreshedToken);
//            Call<SetProfileResponse> call = setprofile.setProfile(user);
//            call.enqueue(new Callback<SetProfileResponse>()
//            {
//                @Override
//                public void onResponse(Call<SetProfileResponse> call, Response<SetProfileResponse> response)
//                {
//                    Log.e("FCM", String.valueOf(response.code()));
//                }
//
//                @Override
//                public void onFailure(Call<SetProfileResponse> call, Throwable t)
//                {
//                    Log.e("FCM", "onFailure::liketweet-" + t.toString());
//                }
//            });
//        }
    }

    public static boolean onMessageReceived(RemoteMessage message, Context mContext) {
        if (message == null){
            return true;
        }

        Map data = message.getData();

        if (data == null){
            return true;
        }

        String type = (String) data.get("type");

        if (type == null){
            return true;
        }

//        if (type.equals("POLE_NOTIFICATION")){
            createNotification("dsfsdf", "Sdfsf ",mContext);
            return false;
//        } else {
//            return true;
//        }
    }

    private static void setInFirebase(String name, String beacon_id, double distance, boolean isEnter, Context context) {
        SharedPreferences pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);
        String user_id = pref.getString("uid", "none");
        DatabaseReference mFirebaseHistoryReference = FirebaseDatabase.getInstance().getReference();
        Queue queue = new Queue(beacon_id, user_id, distance, isEnter);
        mFirebaseHistoryReference.child(name).push().setValue(queue);
    }

    private static void createNotification(String title, String content, Context mContext) {
        setInFirebase("getnotification", "sdsdsf", 3434, true, mContext);
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
