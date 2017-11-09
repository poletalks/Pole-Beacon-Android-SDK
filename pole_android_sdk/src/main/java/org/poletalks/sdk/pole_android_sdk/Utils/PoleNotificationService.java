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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;

import org.poletalks.sdk.pole_android_sdk.FeedbackSDKActivity;
import org.poletalks.sdk.pole_android_sdk.Model.CommonResponse;
import org.poletalks.sdk.pole_android_sdk.Model.UserProfile;
import org.poletalks.sdk.pole_android_sdk.PoleProximityManager;
import org.poletalks.sdk.pole_android_sdk.R;
import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.ApiInterface;
import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.RetrofitConfig;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by anjal on 11/1/17.
 */

public class PoleNotificationService {

    public static String getToken(FirebaseInstanceId firebaseInstanceId, Context context) {
        String refreshedToken = null;
        try {
            refreshedToken = firebaseInstanceId.getToken(Config.fcm_sender_id, "FCM");
            Log.d("MyInstanceId", "sdk:Refreshed token: " + refreshedToken);
            sendRegistrationToServer(refreshedToken, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO: Implement this method to send any registration to your app's servers.
        return (refreshedToken);
    }

    static void sendRegistrationToServer(String refreshedToken, Context context) {
        SharedPreferences pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);

        if (!pref.getString("fcm_token", "none").equals(refreshedToken)) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("fcm_token", refreshedToken);
            editor.putBoolean("is_fcm_token_changed", true);
            editor.apply();
        }

        if (CheckNetwork.isInternetAvailable(context))
        {
            RetrofitConfig retrofitConfig = new RetrofitConfig(context);
            Retrofit retro = retrofitConfig.getRetro();
            ApiInterface setprofile = retro.create(ApiInterface.class);
            UserProfile user = new UserProfile();

            user.setClientapp_name("hubilo");
            user.setClientapp_uid(null);
            user.setDevice_type("ANDROID");
            user.setFcm_token(refreshedToken);

            Call<CommonResponse> call = setprofile.setProfile(user);
            call.enqueue(new Callback<CommonResponse>()
            {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                {
                    if (response.code() == 200){
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t)
                {
                    Log.e("FCM", "onFailure::liketweet-" + t.toString());
                }
            });
        }

    }

    public static boolean onMessageReceived(RemoteMessage message, Context mContext) {
        return true;
//        if (message == null){
//            return true;
//        }
//
//        Map data = message.getData();
//
//        if (data == null){
//            return true;
//        }
//
//        String type = (String) data.get("type");
//
//        if (type == null){
//            return true;
//        }
//
//        if (type.equals("POLE_NOTIFICATION")){
//            createNotification("dsfsdf", "Sdfsf ",mContext);
//            return false;
//        } else {
//            return true;
//        }
    }

    private static void createNotification(String title, String content, Context mContext) {
        try {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
            Intent resultIntent = new Intent(mContext, FeedbackSDKActivity.class);

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
