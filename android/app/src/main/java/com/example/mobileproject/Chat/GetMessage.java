package com.example.mobileproject.Chat;

public class GetMessage {
    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    String fromUsername;
    String messageText;

    public GetMessage(String fromUsername, String messageText) {
        this.fromUsername = fromUsername;
        this.messageText = messageText;
    }
}
