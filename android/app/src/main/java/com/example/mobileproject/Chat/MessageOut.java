package com.example.mobileproject.Chat;

import com.example.mobileproject.User;
import com.google.gson.annotations.SerializedName;


public class MessageOut {
    public MessageOut(Integer chatID, String text) {
        this.chatID = chatID;
        this.text = text;
    }

    public Integer getChatID() {
        return chatID;
    }

    public void setChatID(Integer chatID) {
        this.chatID = chatID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @SerializedName("chat_id")
    private Integer chatID;

    @SerializedName("text")
    private String text;
}
