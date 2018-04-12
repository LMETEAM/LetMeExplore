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
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    private boolean adminController;
    private Song song;
    private Context context;
    private ArrayAdapter<User> userArrayAdapter;
    private List<User> userList;
    private List<String> stringList;
    private List<Song> songList;
    public DatabaseControl(){}
    public DatabaseControl(String adress,Context context1) {
        this.context = context1;
        // Write a message to the database
        mAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(adress);
    }
    void sendUser(User user){
        myRef.setValue(user);
    }
    void sendSong(final Song song, final Uri uri){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;

                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    count++;
                    Song song1=ds.child("properties").getValue(Song.class);
                    if((song.getSongName().toUpperCase().equalsIgnoreCase(song1.getSongName().toUpperCase()))&&song.getSongType().toUpperCase()
                            .equalsIgnoreCase(song1.getSongType().toUpperCase())){
                        Toast.makeText(context,"This song is already exists",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(count==dataSnapshot.getChildrenCount()){
                        if(uri==null){
                        String key=myRef.push().getKey();
                        song.setSongkey(key);
                        song.setSongPhotoUrl("https://firebasestorage.googleapis.com/v0/b/letmeexplore-fb83f.appspot.com/o/Songs%2FPhotos%2Fdefaultimage.png?alt=media&token=b97f0aa5-7ee2-4536-a024-6222f69573ee");
                        myRef.push().child("properties").setValue(song);
                            Toast.makeText(context,"Update Successful",Toast.LENGTH_SHORT).show();
                        break;
                        }
                        else {

                            String key=myRef.push().getKey();
                            song.setSongkey(key);
                            mStorageRef = FirebaseStorage.getInstance().getReference("Songs/Photos/"+key);
                            mStorageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    song.setSongPhotoUrl(taskSnapshot.getDownloadUrl().toString());
                                    myRef.push().child("properties").setValue(song);
                                    Toast.makeText(context,"Update Successful",Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void getUser(String uid) {
            myRef.child(uid).child("properties").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        User userq = dataSnapshot.getValue(User.class);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
    Song getSong(){
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
    void getSongListtoFindSongType(final List<String> stringList){
        //Şarkı adresi bulunduran stringList ile veritabanından şarkıları teker teker çekip songList'e aktarır(Song.class tipinde)
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Songs");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        FindSongType.songList.clear();
                       for (int i=0;i<=stringList.size();i++){
                          FindSongType.songList.add(dataSnapshot.child(stringList.get(i)).child("properties").getValue(Song.class));
                       }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
    void sendString(String string){
        myRef.setValue(string);
    }
    void getStringListtoFindSongType(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FindSongType.stringList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                   FindSongType.stringList.add(ds.getValue(String.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void updateUser(User user){
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
    void updateSong(Song song){
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
