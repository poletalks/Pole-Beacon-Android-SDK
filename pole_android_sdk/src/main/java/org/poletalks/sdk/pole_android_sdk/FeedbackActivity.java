package org.poletalks.sdk.pole_android_sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import org.poletalks.sdk.pole_android_sdk.Model.LoginResponse;
import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.ApiInterface;
import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.RetrofitConfig;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeedbackActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        context = this;
//        setTitle("FEEDBACK");

//        PoleNotificationService.getToken(FirebaseInstanceId.getInstance());

        getdata("jdp_superadmin", "password");
        getFcmToken();
    }

    private void getFcmToken() {
        String refreshedToken = null;
        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken("732877727331", "FCM");
            Log.d("MyInstanceId", "sdk:Refreshed token in feedback: " + refreshedToken);
            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(refreshedToken);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRegistrationToServer(String refreshedToken) {
        final SharedPreferences pref = getSharedPreferences("OTP", Context.MODE_PRIVATE);

        if (pref.getString("fcm_token", null) == null){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("fcm_token", refreshedToken);
            editor.commit();

            Log.e("fcm_token", refreshedToken);
        }


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


    public void getdata(String login, String pass) {
        RetrofitConfig retrofitConfig = new RetrofitConfig(context);
        Retrofit retro = retrofitConfig.getRetro();
        final ApiInterface admin = retro.create(ApiInterface.class);

        JsonObject abc = new JsonObject();
        abc.addProperty("login_id", login);
        abc.addProperty("password", pass);

        Call<LoginResponse> call = admin.adminLogin(abc);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, "Oops, something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
