package com.letmeexplore.lme;

import android.net.Uri;

/**
 * Created by Casper on 7.04.2018.
 */

public class Song {
    private String songName;
    private String songType;
    private String singer;
    private String year;
    private String songTime;
    private String songPhotoUrl;
    private String songkey;

    public Song(){
    }
    public Song(String songName, String songType, String singer, String year, String songTime, String songPhotoUrl, String songkey){
        this.songName=songName;
        this.songType=songType;
        this.singer=singer;
        this.year=year;
        this.songTime=songTime;
        this.songPhotoUrl=songPhotoUrl;
        this.songkey=songkey;

    }

    public String getSongkey() {
        return songkey;
    }

    public void setSongkey(String songkey) {
        this.songkey = songkey;
    }

    public String getSongPhotoUrl() {
        return songPhotoUrl;
    }

    public void setSongPhotoUrl(String songPhotoUrl) {
        this.songPhotoUrl = songPhotoUrl;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongType() {
        return songType;
    }

    public void setSongType(String songType) {
        this.songType = songType;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSongTime() {
        return songTime;
    }

    public void setSongTime(String songTime) {
        this.songTime = songTime;
    }
}
