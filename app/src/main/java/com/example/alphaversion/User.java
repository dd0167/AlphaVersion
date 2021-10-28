package com.example.alphaversion;

public class User {

    private String UserEmail;
    private String UserPhone;

    public User (String UserEmail, String UserPhone) {
        this.UserEmail=UserEmail;
        this.UserPhone=UserPhone;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
