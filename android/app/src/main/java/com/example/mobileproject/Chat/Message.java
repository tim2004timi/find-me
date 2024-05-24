package com.example.mobileproject.Chat;

import com.google.gson.annotations.SerializedName;


import java.util.Date;

public class Message {
    public String username;
    public String textMessage;
    private long messageTime;


    @SerializedName("chat_id")
    private int chatId;

    @SerializedName("text")
    private String messageText;

    @SerializedName("id")
    private int id;

    @SerializedName("from_user_id")
    private int fromUserId;

    @SerializedName("adequacy")
    private float adequacy;

    @SerializedName("created_at")
    private String createdTime;

    @SerializedName("username")
    private String userNickname;



    public Message(String userName, String messageText, long messageTime) {
        this.username = userName;
        this.textMessage = messageText;
        this.messageTime = messageTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
