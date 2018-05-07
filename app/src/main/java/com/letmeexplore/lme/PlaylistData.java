package com.letmeexplore.lme;

public class PlaylistData {
    private String uid;
    private String songtype;
    private String playlistname;
    private String photoUrl;
    private int value;

    public PlaylistData() {

    }

    public PlaylistData(String uid, int value, String songtype, String playlistname, String photoUrl) {
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
        this.photoUrl = photoUrl;
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
