package org.poletalks.sdk.pole_android_sdk.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.ApiInterface;
import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.RetrofitConfig;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by LENOVO on 04-Feb-16.
 */


public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
//        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d("MyInstanceId", "Refreshed token: " + refreshedToken);
//        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(refreshedToken);

        String refreshedToken = null;
        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken(Config.fcm_sender_id, "FCM");

            Log.d("MyInstanceId", "sdk:Refreshed token: " + refreshedToken);
            // TODO: Implement this method to send any registration to your app's servers.
            PoleNotificationService.sendRegistrationToServer(refreshedToken, this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
