package com.example.mobileproject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("register/")
    Call<ResponseBody> registerUser(@Body User user);

    @POST("login/")
    Call<ResponseBody> loginUser(@Body User user);

    @POST("profile/")
    Call<ResponseBody> postProfile(@Body Profile profile);

    @GET("profile/")
    Call<Profile> getProfile(@Query("username") String username, @Query("password") String password);
}
