package com.letmeexplore.lme;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Casper on 7.04.2018.
 */

public class User {
    private String name;
    private String surname;
    private String photoUrl;
    private String uid;
    public User(){
    }

    public User(User user){
        this.name=user.getName();
        this.surname=user.getSurname();
        this.photoUrl=user.getPhotoUrl();
        this.uid=user.getUid();

    }
    public User(String name, String surname, String photoUrl, String uid){
        this.name=name;
        this.surname=surname;
        this.photoUrl=photoUrl;
        this.uid=uid;

    }
    public void changeUser(User user){
        this.name=user.getName();
        this.surname=user.getSurname();
        this.photoUrl=user.getPhotoUrl();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

