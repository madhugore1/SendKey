package com.app.sendkey;

public class MessageModel {

    private String text;
    private String email;

    public MessageModel() {
    }

    public MessageModel(String text, String email) {
        this.text = text;
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
