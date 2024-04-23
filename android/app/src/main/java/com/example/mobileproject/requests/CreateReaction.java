package com.example.mobileproject.requests;

import com.example.mobileproject.Reaction;
import com.example.mobileproject.User;

public class CreateReaction {
    public Reaction reaction;
    public User auth_user;
    public CreateReaction(Reaction reaction, User authUser) {
        this.reaction = reaction;
        this.auth_user = authUser;
    }
}
