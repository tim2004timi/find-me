package com.example.mobileproject.requests;

import com.example.mobileproject.profiles.Profile;
import com.example.mobileproject.User;

public class CreateProfile {
    public Profile profile;
    public User auth_user;

    public CreateProfile(Profile profile, User user) {
        this.profile = profile;
        this.auth_user = user;
    }
}
