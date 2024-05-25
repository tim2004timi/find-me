package com.example.mobileproject;
import com.example.mobileproject.Chat.Message;
import com.example.mobileproject.Chat.MessageIn;
import com.example.mobileproject.Chat.SendMessageUser;
import com.example.mobileproject.dialogs.Chat;
import com.example.mobileproject.profiles.Profile;
import com.example.mobileproject.requests.CreateProfile;
import com.example.mobileproject.requests.CreateReaction;
import com.example.mobileproject.verification.VerifyPhoto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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

    @POST("profiles/verify_photo/")
    Call<ResponseBody> verifyPhoto(@Body VerifyPhoto verifyPhoto);

    @POST("chats/own/")
    Call<List<Chat>> getOwnChats(@Body User user);

    @POST("chats/messages/")
    Call<List<MessageIn>> getMessagesByChatId(@Body User user, @Query("chat_id") Integer chatId);

    @POST("chats/message/")
    Call<ResponseBody> sendMessage(@Body SendMessageUser sendMessageUser);

}
