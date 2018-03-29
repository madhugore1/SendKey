package com.app.sendkey;

public class User {

    public String email, user_type, uid;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type() {
        this.user_type = user_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String email) {
        this.uid = email;
    }

    public User() {

    }

    public User(String email, String user_type, String uid) {
        this.email = email;
        this.user_type = user_type;
        this.uid = uid;
    }

}
