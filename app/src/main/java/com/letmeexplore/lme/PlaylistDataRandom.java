package com.letmeexplore.lme;

public class PlaylistDataRandom {

    private String uid;
    private String playlistname;
    private String photoUrl;

    public PlaylistDataRandom(){

    }

    public PlaylistDataRandom(String uid, String playlistname, String photoUrl) {
        this.uid = uid;
        this.playlistname=playlistname;
        this.photoUrl=photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

}
