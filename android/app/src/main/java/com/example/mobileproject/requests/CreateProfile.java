package com.example.mobileproject.requests;

import com.example.mobileproject.profiles.ProfileIn;
import com.example.mobileproject.User;

public class CreateProfile {
    public ProfileIn profileIn;
    public User auth_user;

    public CreateProfile(ProfileIn profileIn, User user) {
        this.profileIn = profileIn;
        this.auth_user = user;
    }
}
