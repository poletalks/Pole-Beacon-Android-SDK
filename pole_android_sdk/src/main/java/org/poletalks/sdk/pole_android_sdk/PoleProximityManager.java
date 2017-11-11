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

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import org.json.JSONObject;
import org.poletalks.sdk.pole_android_sdk.FeedbackPage.FeedbackSDKActivity;
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

    private static final int REQUEST_ENABLE_BT = 77;
    private static Context mContext;
    public static ProximityManager proximityManager;
    public static String TASKS = "beacon-logs-test";
    private static DatabaseReference mFirebaseHistoryReference;
    private static FirebaseDatabase secondaryDatabase;


    public static void onCreateBeacons(Context context, String uid) {
        mContext= context;
        KontaktSDK.initialize(Config.kontakt_api_key);

        proximityManager = ProximityManagerFactory.create(mContext);
        proximityManager.setEddystoneListener(createEddystoneListener(mContext));

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApplicationId(Config.firebase_application_id) // Required for Analytics.
                    .setApiKey(Config.firebase_api_key) // Required for Auth.
                    .setDatabaseUrl(Config.firebase_db_url) // Required for RTDB.
                    .build();

            // Initialize with secondary app.
            FirebaseApp.initializeApp(context, options, "secondary");

            // Retrieve secondary app.
            FirebaseApp secondary = FirebaseApp.getInstance("secondary");

            // Get the database for the other app.
            secondaryDatabase = FirebaseDatabase.getInstance(secondary);
        } catch (Exception e){
            secondaryDatabase = FirebaseDatabase.getInstance();
        }

        registerUser(context, uid);
    }

    private static void registerUser(Context context, String client_uid) {
        final SharedPreferences pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("client_uid", client_uid);
        editor.apply();

        String fcm_token = pref.getString("fcm_token", "none");
        String pole_uid = pref.getString("pole_uid", "none");

        if (!fcm_token.equals("none") && CheckNetwork.isInternetAvailable(context)) {
            if (pole_uid.equals("none")) {
                // Create user
                RetrofitConfig retrofitConfig = new RetrofitConfig(context);
                Retrofit retro = retrofitConfig.getRetro();
                ApiInterface setprofile = retro.create(ApiInterface.class);
                UserProfile user = new UserProfile();

                user.setClientapp_name("HUBILO");
                user.setClientapp_uid(client_uid);
                user.setDevice_type("ANDROID");
                user.setFcm_token(fcm_token);

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
                    public void onFailure(Call<CommonResponse> call, Throwable t)
                    {
                        Log.e("Create user error ", t.toString());
                    }
                });
            } else {
                // Update user
                RetrofitConfig retrofitConfig = new RetrofitConfig(context);
                Retrofit retro = retrofitConfig.getRetro();
                ApiInterface setprofile = retro.create(ApiInterface.class);
                UserProfile user = new UserProfile();

                user.set_id(pole_uid);
                user.setClientapp_name("HUBILO");
                user.setClientapp_uid(client_uid);
                user.setDevice_type("ANDROID");
                user.setFcm_token(fcm_token);

                Call<CommonResponse> call = setprofile.createUser(user);
                call.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)  {
                        if (response.code() == 200){
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("pole_uid", response.body().get_id());
                            editor.apply();
                        } else {
                            Log.e("Create user error", "Status code: " + String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t)
                    {
                        Log.e("Create user error ", t.toString());
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
        String user_id = pref.getString("pole_uid", "none");
        String client_id = pref.getString("client_uid", "none");
        mFirebaseHistoryReference = secondaryDatabase.getReference();
        Queue queue = new Queue(beacon_id, user_id, client_id, distance, isEnter, pref.getString("fcm_token", "fcm_token"));
        mFirebaseHistoryReference.child(TASKS).child("tasks").push().setValue(queue);
    }

    private static void createNotification(String title, String content) {
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

    public static void setUserInfo(JSONObject info, Context context, String client_uid){
        JsonObject gson = new JsonParser().parse(String.valueOf(info)).getAsJsonObject();

        final SharedPreferences pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("client_uid", client_uid);
        editor.apply();

        String fcm_token = pref.getString("fcm_token", "none");
        String pole_uid = pref.getString("pole_uid", "none");

        if (!fcm_token.equals("none") && CheckNetwork.isInternetAvailable(context)) {
            if (pole_uid.equals("none")) {
                // Create user
                RetrofitConfig retrofitConfig = new RetrofitConfig(context);
                Retrofit retro = retrofitConfig.getRetro();
                ApiInterface setprofile = retro.create(ApiInterface.class);

                JsonObject user = new JsonObject();
                user.addProperty("clientapp_name", "HUBILO");
                user.addProperty("clientapp_uid", client_uid);
                user.addProperty("device_type", "ANDROID");
                user.addProperty("fcm_token", fcm_token);
                user.add("user_info", gson);

                Call<CommonResponse> call = setprofile.createUserFromJsonObject(user);
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
                    public void onFailure(Call<CommonResponse> call, Throwable t)
                    {
                        Log.e("Create user error ", t.toString());
                    }
                });
            } else {
                // Update user
                RetrofitConfig retrofitConfig = new RetrofitConfig(context);
                Retrofit retro = retrofitConfig.getRetro();
                ApiInterface setprofile = retro.create(ApiInterface.class);
                JsonObject user = new JsonObject();

                user.addProperty("_id", pole_uid);
                user.addProperty("clientapp_name", "HUBILO");
                user.addProperty("clientapp_uid", client_uid);
                user.addProperty("device_type", "ANDROID");
                user.addProperty("fcm_token", fcm_token);
                user.add("user_info", gson);

                Call<CommonResponse> call = setprofile.createUserFromJsonObject(user);
                call.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)  {
                        if (response.code() == 200){
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("pole_uid", response.body().get_id());
                            editor.apply();
                        } else {
                            Log.e("Create user error", "Status code: " + String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t)
                    {
                        Log.e("Create user error ", t.toString());
                    }
                });
            }

        }
    }
}
