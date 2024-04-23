package com.example.mobileproject;

import com.example.mobileproject.profiles.Profile;

import java.util.ArrayList;
import java.util.List;

public class Profiles {
    public List<Profile> profileList = new ArrayList<>();
    public int currentUserIndex = 0;
    public FindActivity findActivity;

//    public Profiles(FindActivity findActivity) {
//        this.findActivity = findActivity;
//    }

    public Profile next() {
//        checkIsEmpty();
        if (currentUserIndex >= profileList.size()) {
            currentUserIndex = 0;
        }

        Profile profile = profileList.get(currentUserIndex);
        currentUserIndex += 1;
        return profile;
    }

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
    }

    public void deleteProfile(Profile profile) {
        if (profileList.size() != 1) {
            profileList.remove(profile);
        }
    }
}
