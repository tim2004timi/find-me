package com.example.mobileproject;

import com.example.mobileproject.profiles.ProfileIn;

import java.util.ArrayList;
import java.util.List;

public class Profiles {
    public List<ProfileIn> profileInList = new ArrayList<>();
    public int currentUserIndex = 0;
    public FindActivity findActivity;

//    public Profiles(FindActivity findActivity) {
//        this.findActivity = findActivity;
//    }

    public ProfileIn next() {
//        checkIsEmpty();
        if (currentUserIndex >= profileInList.size()) {
            currentUserIndex = 0;
        }

        ProfileIn profileIn = profileInList.get(currentUserIndex);
        currentUserIndex += 1;
        return profileIn;
    }

    public List<ProfileIn> getProfileList() {
        return profileInList;
    }

    public void setProfileList(List<ProfileIn> profileInList) {
        this.profileInList = profileInList;
    }

    public void deleteProfile(ProfileIn profileIn) {
        if (profileInList.size() != 1) {
            profileInList.remove(profileIn);
        }
    }
}
