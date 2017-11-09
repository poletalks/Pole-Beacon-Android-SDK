package org.poletalks.sdk.pole_android_sdk.RetrofitSupport;

import com.google.gson.JsonObject;

import org.poletalks.sdk.pole_android_sdk.Model.CommonResponse;
import org.poletalks.sdk.pole_android_sdk.Model.LoginResponse;
import org.poletalks.sdk.pole_android_sdk.Model.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
//all the interface calls to the apis are defined here


public interface ApiInterface {
    //store admin

    @POST("storeAdmins/login")
    Call<LoginResponse> adminLogin(@Body JsonObject data);

    @POST("Users")
    Call<CommonResponse> setProfile(@Body UserProfile userProfile);

    @PUT("user/update")
    Call<CommonResponse> updateProfile(@Body UserProfile userProfile);

    @POST("user/update")
    Call<CommonResponse> sendUserDetails(@Body JsonObject data);
}