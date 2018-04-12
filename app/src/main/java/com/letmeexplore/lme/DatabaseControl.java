package com.letmeexplore.lme;

import android.content.Context;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

/**
 * Created by Burak on 7.04.2018.
 */

public class DatabaseControl{
    private FirebaseAuth mAuth;
    private boolean adminController;
    private Song song;
    private Context context;
    private ArrayAdapter<User> userArrayAdapter;
    private List<User> userList;
    private List<String> stringList;
    private List<Song> songList;
    public DatabaseControl() {
        // Write a message to the database
        mAuth=FirebaseAuth.getInstance();
    }
    User getUser(DataSnapshot dataSnapshot){

        return dataSnapshot.getValue(User.class);
    }
    Song getSong(DataSnapshot dataSnapshot){
        return dataSnapshot.getValue(Song.class);
    }
    List<Song> getSongList(DataSnapshot dataSnapshot){
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            songList.add(ds.getValue(Song.class));
        }
        return songList;
    }

}
