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
    private ArrayList<String> stringArrayList;
    public DatabaseControl(String Adress) {
        // Write a message to the database

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Adress);
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
    void StringSend(String string){
        myRef.setValue(string);
    }
    ArrayList<String> StringGet(){
        stringArrayList=new ArrayList<String>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    stringArrayList.add(ds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return stringArrayList;
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
}
