package org.poletalks.sdk.pole_android_sdk.RetrofitSupport;

import com.google.gson.JsonObject;

import org.poletalks.sdk.pole_android_sdk.Model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
//all the interface calls to the apis are defined here


public interface ApiInterface {
    //store admin

    @POST("storeAdmins/login")
    Call<LoginResponse> adminLogin(@Body JsonObject data);
}