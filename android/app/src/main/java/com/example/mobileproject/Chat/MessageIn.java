package com.example.mobileproject.Chat;


import com.google.gson.annotations.SerializedName;


public class MessageIn {
    @SerializedName("chat_id")
    private int chatId;

    @SerializedName("text")
    private String messageText;

    @SerializedName("id")
    private int id;

    @SerializedName("from_username")
    private String fromUsername;

    @SerializedName("created_at")
    private String createdAt;

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
