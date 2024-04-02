package com.example.mobileproject;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private String username;
    private String password;
    private String name;
    private String sex;
    private int age;
    private String city;
    private List<String> hobbies;
    private String status;

    private String photo_base64;

    public String getPhoto() {
        return photo_base64;
    }

    public void setPhoto(String photo_base64) {
        this.photo_base64 = photo_base64;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
