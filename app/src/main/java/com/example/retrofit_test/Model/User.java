package com.example.retrofit_test.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    private int accountId ;
    private String username;
    private String password ;
    @SerializedName("avt")
    private String avatar ;

    public User(int accountId, String username, String password, String avatar) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
