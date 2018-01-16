package org.poletalks.sdk.pole_android_sdk;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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
import org.poletalks.sdk.pole_android_sdk.Utils.PoleNotificationService;

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
    private static BluetoothAdapter adapter;
    private static boolean initialbBluetoothStatus = false;


    public static void onCreateBeacons(Context context, String uid) {
        mContext= context;
        KontaktSDK.initialize(Config.kontakt_api_key);

        proximityManager = ProximityManagerFactory.create(mContext);
        proximityManager.setEddystoneListener(createEddystoneListener(mContext));


        try {
            adapter = BluetoothAdapter.getDefaultAdapter();

            if (adapter != null) {
                if (adapter.isEnabled()) {
                    initialbBluetoothStatus =true;
                }
            }

            final Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e("Bluetooth" , "Calling handler");
                        if (adapter == null) {
                            // Device does not support Bluetooth
                        } else {
                            if (adapter.isEnabled()) {
                                if (!initialbBluetoothStatus){
                                    startScanning();
                                    Log.e("Bluetooth" , "Connected");
                                }
                            } else {
                                handler.postDelayed(this, 10000);
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }, 10000);
        } catch (Exception e){
            Log.e("ServiceConnection", "Leak");
        }

        registerUser(context, uid);
    }

    private static void registerUser(Context context, String client_uid) {
        try {
            final SharedPreferences pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);

            if (client_uid != null){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("client_uid", client_uid);
                editor.apply();
            }

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
                    if (client_uid != null){
                        user.setClientapp_uid(client_uid);
                    }
                    user.setDevice_type("ANDROID");
                    user.setFcm_token(fcm_token);
                    user.setVersion(2);

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
                    if (client_uid != null){
                        user.setClientapp_uid(client_uid);
                    }
                    user.setDevice_type("ANDROID");
                    user.setFcm_token(fcm_token);
                    user.setVersion(2);

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
        } catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }


    private static EddystoneListener createEddystoneListener(final Context mContext) {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                try {
                    if (eddystone != null){
                        if (eddystone.getUniqueId() != null){
                            setInFirebase(eddystone.getUniqueId(), eddystone.getDistance(), true, mContext);
                        }
                    }
                } catch (Exception e){
                    Log.e("Error", e.getMessage());
                }
            }

            @Override
            public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                try {
                    if (eddystone != null){
                        if (eddystone.getUniqueId() != null){
                            setInFirebase(eddystone.getUniqueId(), eddystone.getDistance(), false, mContext);
                        }
                    }
                } catch (Exception e){
                    Log.e("Error", e.getMessage());
                }

            }
        };
    }

    private static void setInFirebase(String beacon_id, double distance, boolean isEnter, Context context) {

    }

    public static void startScanning() {
        if (proximityManager != null) {
            try {
                proximityManager.connect(new OnServiceReadyListener() {
                    @Override
                    public void onServiceReady() {
                        try {
                            if (proximityManager.isConnected()){
                                try {
                                    proximityManager.startScanning();
                                } catch (Exception e) {
                                    Log.e("Error: ", "IllegalStateException");
                                }
                            }
                        } catch (Exception e){
                            Log.e("Error", e.getMessage());
                        }
                    }
                });
            } catch (Exception e){
                Log.e("Error: ", "IllegalStateException");
            }
        }
    }

    public static void stopScanning() {
        proximityManager.stopScanning();
    }

    public static void destroyScanning() {
        if (proximityManager != null){
            proximityManager.stopScanning();
            proximityManager.disconnect();
            proximityManager = null;
        }
    }

    public static void setUserInfo(JSONObject info, Context context, String client_uid){
        try {
            JsonObject gson = new JsonParser().parse(String.valueOf(info)).getAsJsonObject();

            final SharedPreferences pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);

            if (client_uid != null){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("client_uid", client_uid);
                editor.apply();
            }

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
                    if (client_uid != null){
                        user.addProperty("clientapp_uid", client_uid);
                    }
                    user.addProperty("device_type", "ANDROID");
                    user.addProperty("fcm_token", fcm_token);
                    user.addProperty("version", 2);
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
                    if (client_uid != null){
                        user.addProperty("clientapp_uid", client_uid);
                    }
                    user.addProperty("device_type", "ANDROID");
                    user.addProperty("fcm_token", fcm_token);
                    user.addProperty("version", 2);
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
        } catch (Exception e){
            Log.e ("Error", e.getMessage());
        }

    }
}
