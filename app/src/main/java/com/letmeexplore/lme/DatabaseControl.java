package com.letmeexplore.lme;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Burak on 7.04.2018.
 */

public class DatabaseControl {
    private DatabaseReference myRef;
    FirebaseDatabase database;
    public DatabaseControl(String Adress) {
        // Write a message to the database
         database = FirebaseDatabase.getInstance();
         myRef = database.getReference(Adress);
    }
    void UserSend(User user){
        myRef.setValue(user);
    }
}
