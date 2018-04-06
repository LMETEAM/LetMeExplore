package com.letmeexplore.lme;

/**
 * Created by Casper on 7.04.2018.
 */

public class User {
    private String name;
    private String surname;
    private String photoUrl;

    public User(String name,String surname,String photoUrl){
        this.name=name;
        this.surname=surname;
        this.photoUrl=photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

