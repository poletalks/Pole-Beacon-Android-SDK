package org.poletalks.sdk.pole_android_sdk.RetrofitSupport;

import com.google.gson.JsonObject;

import org.poletalks.sdk.pole_android_sdk.Model.CommonResponse;
import org.poletalks.sdk.pole_android_sdk.Model.FeedbackResponse;
import org.poletalks.sdk.pole_android_sdk.Model.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
//all the interface calls to the apis are defined here


public interface ApiInterface {

    @POST("/users/createorupdate")
    Call<CommonResponse> createUser(@Body UserProfile userProfile);

    @POST("/users/createorupdate")
    Call<CommonResponse> createUserFromJsonObject(@Body JsonObject data);

    @GET("/feedbacks/questions")
    Call<CommonResponse> getFeedbackQuestions(@Query("item_id") String item_id, @Query("item_type") String item_type);

    @POST("/feedbacks/multipleresponses")
    Call<CommonResponse> sendFeedback(@Body FeedbackResponse feedbackResponse);
}