package com.letmeexplore.lme;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Burak on 7.04.2018.
 */

public class DatabaseControl {
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private User user;
    private Song song;
    private List<String> stringList;
    private List<Song> songList;
    public DatabaseControl(String adress) {
        // Write a message to the database

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(adress);
    }
    void UserSend(User user){
        myRef.setValue(user);
    }
    void SongSend(Song song){
        myRef.setValue(song);
    }
    User UserGet() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return user;
    }
    Song SongGet(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                song=dataSnapshot.getValue(Song.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return song;
    }
    List<Song> getSongList(List<String> stringList){
        //Şarkı adresi bulunduran stringList ile veritabanından şarkıları teker teker çekip songList'e aktarır(Song.class tipinde)
        songList=new ArrayList<Song>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Songs");

        for(int i=0;i<=stringList.size();i++){
            myRef.child(stringList.get(i).toString()).child("Properties").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        songList.add(dataSnapshot.getValue(Song.class));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return songList;

    }
    void StringSend(String string){
        myRef.setValue(string);
    }
    List<String> getStringList(){
        stringList=new ArrayList<String>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    stringList.add(ds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return stringList;
    }
    void UpdateUser(User user){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.setValue(user);
    }
    void UpdateSong(Song song){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.setValue(song);
    }
    DatabaseReference getMyRef(){
        return myRef.getRef();
    }
}
