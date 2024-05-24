package com.example.mobileproject;

import com.example.mobileproject.profiles.Profile;

import java.util.ArrayList;
import java.util.List;

public class Profiles {
    public List<Profile> profileList = new ArrayList<>();
    public int currentUserIndex = -1;
    public FindActivity findActivity;

//    public Profiles(FindActivity findActivity) {
//        this.findActivity = findActivity;
//    }
    public int getCurrentUserId() {
        return profileList.get(currentUserIndex).getUserId();
    }
    public Profile next() {
        currentUserIndex += 1;
//        checkIsEmpty();
        if (currentUserIndex >= profileList.size()) {
            currentUserIndex = 0;
        }

        Profile profile = profileList.get(currentUserIndex);

        return profile;
    }

    public List<Profile> getProfileList() {
        return profileList;
    }
    public String getProfilesInfo() {
        String ids = "";
        for (int i=0; i<profileList.size(); i++) {
            ids += profileList.get(i).getName();
            ids += ": ";
            ids += Integer.toString(profileList.get(i).getUserId());
            ids += " ";

        }
        return ids;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
    }

    public void deleteProfile(Profile profile) {
        profileList.remove(profile);
    }
}
