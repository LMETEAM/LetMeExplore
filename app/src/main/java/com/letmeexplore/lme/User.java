package com.letmeexplore.lme;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Casper on 7.04.2018.
 */

public class User {
    private String displayName;
    private String photoUrl;
    private String uid;
    public User(){
    }

    public User(User user){
        this.displayName=user.getDisplayName();
        this.photoUrl=user.getPhotoUrl();
        this.uid=user.getUid();

    }
    public User(String displayName, String photoUrl, String uid){
        this.displayName=displayName;
        this.photoUrl=photoUrl;
        this.uid=uid;

    }
    public void changeUser(User user){
        this.displayName=user.getDisplayName();
        this.photoUrl=user.getPhotoUrl();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

