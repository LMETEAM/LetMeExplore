package com.letmeexplore.lme;

public class FindSongTypeUserClass {
    private String uid;
    private String songtype;
    private String playlistname;
    private int value;
    public FindSongTypeUserClass() {
    }

    public FindSongTypeUserClass(String uid, int value,String songtype,String playlistname) {
        this.uid = uid;
        this.value = value;
        this.playlistname=playlistname;
        this.songtype=songtype;
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
