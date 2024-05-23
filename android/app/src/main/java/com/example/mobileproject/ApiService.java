package com.example.mobileproject;
import com.example.mobileproject.profiles.Profile;
import com.example.mobileproject.requests.CreateProfile;
import com.example.mobileproject.requests.CreateReaction;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("users/register/")
    Call<ResponseBody> registerUser(@Body User user);

    @POST("users/login/")
    Call<ResponseBody> loginUser(@Body User user);

    @POST("profiles/")
    Call<ResponseBody> postProfile(@Body CreateProfile createProfile);

    @PATCH("profiles/")
    Call<ResponseBody> patchProfile(@Body CreateProfile createProfile);

    @POST("profiles/own/")
    Call<Profile> getProfile(@Body User user);

    @POST("profiles/selection/")
    Call<List<Profile>> getProfiles(@Body User user);

    @POST("reactions/")
    Call<ResponseBody> postReaction(@Body CreateReaction createReaction);

}
