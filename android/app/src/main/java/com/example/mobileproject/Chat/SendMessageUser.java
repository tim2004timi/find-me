package com.example.mobileproject.Chat;

import com.example.mobileproject.User;
import com.example.mobileproject.verification.Photo;

public class SendMessageUser {
    public User auth_user;
    public MessageOut message;

    public SendMessageUser(User auth_user, MessageOut messageOut) {
        this.auth_user = auth_user;
        this.message = messageOut;
    }
}
