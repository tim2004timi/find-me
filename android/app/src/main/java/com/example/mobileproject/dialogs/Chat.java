package com.example.mobileproject.dialogs;

import com.google.gson.annotations.SerializedName;

public class Chat {
    @SerializedName("first_user_id")
    private int firstUserId;
    @SerializedName("second_user_id")
    private int secondUserId;
    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("photo_base64")
    private String photoBase64;
    @SerializedName("user_adequacy")
    private Double userAdequacy;

    public int getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(int firstUserId) {
        this.firstUserId = firstUserId;
    }

    public int getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(int secondUserId) {
        this.secondUserId = secondUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public Double getUserAdequacy() {
        return userAdequacy;
    }

    public void setUserAdequacy(Double userAdequacy) {
        this.userAdequacy = (userAdequacy*100);
    }
}
