package com.letmeexplore.lme;

public class FindSongTypeUserClass {
    private String uid;
    private String songtype;
    private String playlistname;
    private int value;
    private String photoUrl;
    public FindSongTypeUserClass() {
    }

    public FindSongTypeUserClass(String uid, int value,String songtype,String playlistname,String photoUrl) {
        this.uid = uid;
        this.value = value;
        this.playlistname=playlistname;
        this.songtype=songtype;
        this.photoUrl=photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        photoUrl = photoUrl;
    }

    public String getSongtype() {
        return songtype;
    }

    public void setSongtype(String songtype) {
        this.songtype = songtype;
    }

    public String getPlaylistname() {
        return playlistname;
    }

    public void setPlaylistname(String playlistname) {
        this.playlistname = playlistname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
