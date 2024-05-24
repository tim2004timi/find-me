package com.example.mobileproject.dialogs;

import com.google.gson.annotations.SerializedName;


public class Chat {
    @SerializedName("first_user_id")
    private int firstUserId;

    @SerializedName("first_username")
    private String firstUsername;

    @SerializedName("second_username")
    private String secondUsername;

    @SerializedName("second_user_id")
    private int secondUserId;

    @SerializedName("id")
    private int id;

    @SerializedName("first_user_messages_amount")
    private int firstUserMessagesAmount;

    @SerializedName("first_photo_base64")
    private int firstPhotoBase64;

    @SerializedName("second_photo_base64")
    private int secondPhotoBase64;

    @SerializedName("second_user_messages_amount")
    private int secondUserMessagesAmount;

    @SerializedName("first_user_adequacy_sum")
    private float firstUserAdequacySum;

    @SerializedName("second_user_adequacy_sum")
    private float secondUserAdequacySum;

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

    public int getFirstUserMessagesAmount() {
        return firstUserMessagesAmount;
    }

    public void setFirstUserMessagesAmount(int firstUserMessagesAmount) {
        this.firstUserMessagesAmount = firstUserMessagesAmount;
    }

    public int getSecondUserMessagesAmount() {
        return secondUserMessagesAmount;
    }

    public void setSecondUserMessagesAmount(int secondUserMessagesAmount) {
        this.secondUserMessagesAmount = secondUserMessagesAmount;
    }

    public float getFirstUserAdequacySum() {
        return firstUserAdequacySum;
    }

    public void setFirstUserAdequacySum(float firstUserAdequacySum) {
        this.firstUserAdequacySum = firstUserAdequacySum;
    }

    public float getSecondUserAdequacySum() {
        return secondUserAdequacySum;
    }

    public void setSecondUserAdequacySum(float secondUserAdequacySum) {
        this.secondUserAdequacySum = secondUserAdequacySum;
    }

    public String getFirstUsername() {
        return firstUsername;
    }

    public void setFirstUsername(String firstUsername) {
        this.firstUsername = firstUsername;
    }

    public String getSecondUsername() {
        return secondUsername;
    }

    public void setSecondUsername(String secondUsername) {
        this.secondUsername = secondUsername;
    }

    public int getFirstPhotoBase64() {
        return firstPhotoBase64;
    }

    public void setFirstPhotoBase64(int firstPhotoBase64) {
        this.firstPhotoBase64 = firstPhotoBase64;
    }

    public int getSecondPhotoBase64() {
        return secondPhotoBase64;
    }

    public void setSecondPhotoBase64(int secondPhotoBase64) {
        this.secondPhotoBase64 = secondPhotoBase64;
    }
}
