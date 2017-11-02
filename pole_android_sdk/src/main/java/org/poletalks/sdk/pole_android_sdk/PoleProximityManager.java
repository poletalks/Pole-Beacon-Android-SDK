package org.poletalks.sdk.pole_android_sdk;

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
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import org.poletalks.sdk.pole_android_sdk.Model.CommonResponse;
import org.poletalks.sdk.pole_android_sdk.Model.Queue;
import org.poletalks.sdk.pole_android_sdk.Model.UserProfile;
import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.ApiInterface;
import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.RetrofitConfig;
import org.poletalks.sdk.pole_android_sdk.Utils.CheckNetwork;
import org.poletalks.sdk.pole_android_sdk.Utils.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by anjal on 10/30/17.
 */

public class PoleProximityManager {

    private static Context mContext;
    public static ProximityManager proximityManager;
    public static String TASKS = "tasks-test";
    private static DatabaseReference mFirebaseHistoryReference;
    private static SharedPreferences polePref;

    public static void onCreateBeacons(Context context, String uid){
        mContext= context;
        KontaktSDK.initialize(Config.kontakt_api_key);

        proximityManager = ProximityManagerFactory.create(mContext);
        proximityManager.setEddystoneListener(createEddystoneListener(mContext));

        registerUser(context, uid);
    }

    private static void registerUser(Context context, String uid) {
        final SharedPreferences pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);
        String fcm_token = pref.getString("fcm_token", null);

        if (fcm_token != null && uid != null && pref.getBoolean("is_fcm_token_changed", true)){
            if (CheckNetwork.isInternetAvailable(context))
            {
                RetrofitConfig retrofitConfig = new RetrofitConfig(context);
                Retrofit retro = retrofitConfig.getRetro();
                ApiInterface setprofile = retro.create(ApiInterface.class);
                UserProfile user = new UserProfile();

                user.setClientapp_name("hubilo");
                user.setClientapp_uid(uid);
                user.setDevice_type("ANDROID");
                user.setFcm_token(fcm_token);

                Call<CommonResponse> call = setprofile.setProfile(user);
                call.enqueue(new Callback<CommonResponse>()
                {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                    {
                        if (response.code() == 200){
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("is_fcm_token_changed", false);
                            editor.apply();
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

    }


    private static EddystoneListener createEddystoneListener(final Context mContext) {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.e("Pole Enter ", eddystone.getUniqueId());
                createNotification("Welcome to "+eddystone.getUniqueId(), "Hope you have an awesome time.");
                setInFirebase(eddystone.getUniqueId(), eddystone.getDistance(), true, mContext);
            }

            @Override
            public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.e("Pole Exit ", eddystone.getUniqueId());
                createNotification("Hope you had a great time!", "Please give us your feedback");
                setInFirebase(eddystone.getUniqueId(), eddystone.getDistance(), false, mContext);
            }
        };
    }

    private static void setInFirebase(String beacon_id, double distance, boolean isEnter, Context context) {
        SharedPreferences pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);
        String user_id = pref.getString("uid", "none");
        mFirebaseHistoryReference = FirebaseDatabase.getInstance().getReference();
        Queue queue = new Queue(beacon_id, user_id, distance, isEnter);
        mFirebaseHistoryReference.child(TASKS).push().setValue(queue);
    }

    private static void createNotification(String title, String content) {
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

    public static void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }


    public static void destroyScanning() {
        proximityManager.stopScanning();
        proximityManager.disconnect();
        proximityManager = null;
    }
}
