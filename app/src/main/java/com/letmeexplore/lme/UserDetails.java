package com.letmeexplore.lme;

/**
 * Created by Burak on 16.04.2018.
 */

public class UserDetails extends User {
    private String PlaylistCount;
    public UserDetails() {
    }

    public UserDetails(User user,String playlistCount) {
        super(user);
        this.PlaylistCount=playlistCount;
    }

    public String getPlaylistCount() {
        return PlaylistCount;
    }

    public void setPlaylistCount(String playlistCount) {
        PlaylistCount = playlistCount;
    }

    public UserDetails(String name, String photoUrl, String uid) {
        super(name, photoUrl, uid);
    }
}
