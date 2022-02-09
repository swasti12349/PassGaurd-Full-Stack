package com.sro.passgaurd.model;

public class dataModel {

    String title;
    String password;

    public dataModel(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        password = password;
    }
}
