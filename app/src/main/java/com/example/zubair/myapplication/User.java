package com.example.zubair.myapplication;

/**
 * Created by Zubair on 10-Apr-17.
 */
public class User {
    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public String userName;
    public String UserPassword;
    public String UserEmail;

    public User(String userName,String UserPassword, String UserEmail){
        this.userName = userName;
        this.UserPassword = UserPassword;
        this.UserEmail = UserEmail;
    }
}
