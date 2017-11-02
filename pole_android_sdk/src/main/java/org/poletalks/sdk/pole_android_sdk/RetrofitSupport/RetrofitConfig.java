package org.poletalks.sdk.pole_android_sdk.RetrofitSupport;

/**
 * Created by anjal on 10/30/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    Retrofit retro;

    public RetrofitConfig(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        OkHttpClient.Builder httpClient2 = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);


        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                SharedPreferences preferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

                Log.e("uid", preferences.getString("uid", ""));

                Log.e("hash", preferences.getString("hash", ""));

                Log.e("admin", preferences.getString("admin", ""));

                Log.e("adminhash", preferences.getString("adminhash", ""));

                Request request = original.newBuilder()
                        .header("uid", preferences.getString("uid", ""))
                        .header("hash", preferences.getString("hash", ""))
                        .header("storeadmin", preferences.getString("admin", ""))
                        .header("adminhash", preferences.getString("adminhash", ""))
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client2 = httpClient2.build();
        OkHttpClient client = httpClient.build();

        this.retro = new Retrofit.Builder()
                .baseUrl("http://test.poletalks.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }

    public Retrofit getRetro() {
        return retro;
    }
}
