package com.letmeexplore.lme;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burak on 7.04.2018.
 */

public class DatabaseControl{
    private boolean adminController;
    private Song song;
    private Context context;
    private ArrayAdapter<User> userArrayAdapter;
    public DatabaseControl() {
        // Write a message to the database
    }
    User getUser(DataSnapshot dataSnapshot){

        return dataSnapshot.getValue(User.class);
    }
    Song getSong(DataSnapshot dataSnapshot){
        return dataSnapshot.getValue(Song.class);
    }
    List<Song> getSongList(DataSnapshot dataSnapshot){
        List<Song> songList=new ArrayList<Song>();
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            Song song=ds.child("properties").getValue(Song.class);
            songList.add(song);
        }
        return songList;
    }
    List<String> getSongKeyList(DataSnapshot dataSnapshot){
        List<String> songKeyList=new ArrayList<>();
        for (DataSnapshot ds:dataSnapshot.getChildren()){
            songKeyList.add(ds.child("songkey").getValue(String.class));
        }
        return songKeyList;
    }
    ArrayList<String> getPlaylistName(DataSnapshot dataSnapshot){
        ArrayList<String> playlistName=new ArrayList<>();
        for (DataSnapshot ds:dataSnapshot.getChildren()){
            if(ds.hasChildren())playlistName.add(ds.getKey());
        }
        return playlistName;
    }
    ArrayList<Song> getMatchSongs(List<Song> songList,List<String> songKeyList){
        ArrayList<Song> songList1 =new ArrayList<>();
        for (String songkey:songKeyList){
            for (Song song:songList){
                if(songkey.equalsIgnoreCase(song.getSongkey())){
                    songList1.add(song);
                    // Toast.makeText(getContext(),song.getSongName()+"Var",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        return songList1;
    }
    List<FindSongTypeUserClass> getFindSongTypeUser(DataSnapshot dataSnapshot){
        List<FindSongTypeUserClass> list=new ArrayList<>();
        for (DataSnapshot playlist:dataSnapshot.getChildren()){
            list.add(playlist.getValue(FindSongTypeUserClass.class));
        }
        return list;
    }
}
