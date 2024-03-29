package com.example.mobileproject.dto;

public class UserData {
    public String username;
    public String status;
    public String age;
    public String sex;
    public String city;
    public String hobby1;
    public String hobby2;
    public String hobby3;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHobby1() { return hobby1; }

    public void setHobby1(String hobby1) { this.hobby1 = hobby1; }

    public String getHobby2() { return hobby2; }

    public void setHobby2(String hobby2) { this.hobby2 = hobby2; }

    public String getHobby3() { return hobby3; }

    public void setHobby3(String hobby3) { this.hobby3 = hobby3; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
