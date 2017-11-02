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
            refreshedToken = FirebaseInstanceId.getInstance().getToken("732877727331", "FCM");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("MyInstanceId", "sdk:Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }


    private void sendRegistrationToServer(String refreshedToken) {
        final SharedPreferences pref = getSharedPreferences("PolePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fcm_token", refreshedToken);
        editor.commit();

        Log.e("fcm_token", refreshedToken);

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

}
