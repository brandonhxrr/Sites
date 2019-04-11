package com.foxware.sites;


public class User {
    String user_name, photo_url;

    public User(){

    }
    public User(String user_name, String photo_url) {
        this.user_name = user_name;
        this.photo_url = photo_url;

    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }


}
