package com.example.mobileproject.verification;

import com.example.mobileproject.User;
import com.example.mobileproject.verification.Photo;

public class VerifyPhoto {
    public User auth_user;
    public Photo photo;
    public VerifyPhoto(Photo photo, User authUser) {
        this.photo = photo;
        this.auth_user = authUser;
    }
}
