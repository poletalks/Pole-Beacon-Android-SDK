package org.poletalks.sdk.pole_android_sdk.Utils;

import android.app.LauncherActivity;
import android.app.Notification;
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

import org.poletalks.sdk.pole_android_sdk.FeedbackPage.FeedbackSDKActivity;
import org.poletalks.sdk.pole_android_sdk.Model.CommonResponse;
import org.poletalks.sdk.pole_android_sdk.Model.UserProfile;
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

    static void sendRegistrationToServer(final String refreshedToken, final Context context) {
        final SharedPreferences pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fcm_token", refreshedToken);
        editor.apply();

        String pole_uid = pref.getString("pole_uid", "none");

        if (CheckNetwork.isInternetAvailable(context)) {
            if (pole_uid.equals("none")) {
                RetrofitConfig retrofitConfig = new RetrofitConfig(context);
                Retrofit retro = retrofitConfig.getRetro();
                ApiInterface setprofile = retro.create(ApiInterface.class);
                UserProfile user = new UserProfile();

                user.setClientapp_name("HUBILO");
                user.setDevice_type("ANDROID");
                user.setVersion(2);
                user.setFcm_token(refreshedToken);

                Call<CommonResponse> call = setprofile.createUser(user);
                call.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.code() == 200) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("pole_uid", response.body().get_id());
                            editor.apply();
                        } else {
                            Log.e("Create user error", "Status code: " + String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        Log.e("Create user error", t.toString());
                    }
                });
            } else {
                RetrofitConfig retrofitConfig = new RetrofitConfig(context);
                Retrofit retro = retrofitConfig.getRetro();
                ApiInterface setprofile = retro.create(ApiInterface.class);
                UserProfile user = new UserProfile();

                user.setClientapp_name("HUBILO");
                user.setDevice_type("ANDROID");
                user.setVersion(2);
                user.setFcm_token(refreshedToken);
                user.set_id(pole_uid);

                Call<CommonResponse> call = setprofile.createUser(user);
                call.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.code() == 200) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("pole_uid", response.body().get_id());
                            editor.apply();
                        } else {
                            Log.e("Create user error", "Status code: " + String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        Log.e("Create user error", t.toString());
                    }
                });
            }
        }
    }



    public static void createNotification(String title, String content, String item_id, String item_type, Context mContext) {
        try {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
            Integer notificationId = Integer.valueOf(String.valueOf((System.currentTimeMillis() % 4234)));

            Intent resultIntent = new Intent(mContext, FeedbackSDKActivity.class);
            resultIntent.putExtra("item_id", item_id);
            resultIntent.putExtra("item_type", item_type);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, notificationId, resultIntent, 0);

            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.brandlog);

            mBuilder.setSmallIcon(R.drawable.small_icon)
                    .setLargeIcon(icon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setOnlyAlertOnce(true);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(content);
            mBuilder.setStyle(bigTextStyle);

            SharedPreferences pref = mContext.getSharedPreferences("polePref", Context.MODE_PRIVATE);
            int totalNotificationCount = pref.getInt("totalNotificationCount", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("totalNotificationCount", totalNotificationCount+1);
            editor.apply();

            mBuilder.setContentIntent(pendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setVibrate(new long[]{100});
            mBuilder.setAutoCancel(true);
            mNotificationManager.notify(notificationId, mBuilder.build());
        } catch (Exception e){
            Log.e("NOtification Error", "Dsdsf");
        }

    }
}
