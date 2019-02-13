package com.example.administrator.retrofitmvp;


public class UserBean extends BaseResponse {

    private String username;
    private String password;

    public String getUsenname() {
        return username == null ? "" : username;
    }

    public UserBean setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    public UserBean setPassword(String password) {
        this.password = password;
        return this;
    }
}
