package com.example.mobileproject;
import com.example.mobileproject.profiles.Profile;
import com.example.mobileproject.requests.CreateProfile;

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

    @GET("profiles/")
    Call<List<Profile>> getUsers(@Query("username") String username);
}
