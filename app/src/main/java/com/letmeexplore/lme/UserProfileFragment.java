package com.letmeexplore.lme;


import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.letmeexplore.lme.AddSongActivity.RC_SELECT_IMAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private CircleImageView circleImageView;
    private TextView textView;
    private ImageView backbuttonimage;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myref;
    private ListView userPlaylist;
    private ArrayList<String> playList;
    private ArrayAdapter<String> arrayAdapter;
    private StorageReference storageReference;
    private FirebaseStorage mStorage;
    private Uri choosenImage;
    private User userNew;
    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_profile, container, false);
        circleImageView=(CircleImageView)view.findViewById(R.id.user_profile_image);
        textView=(TextView)view.findViewById(R.id.user_profile_displayname);
        backbuttonimage=(ImageView)view.findViewById(R.id.user_profile_backbuttonview);
        userPlaylist=(ListView)view.findViewById(R.id.search_userprofile_playlist);
        playList=new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, playList);
        userPlaylist.setAdapter(arrayAdapter);
        database=FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
        myref=database.getReference("Users");
        choosenImage = null;
        userNew = new User();
        myref.keepSynced(true);
        Bundle bundle=this.getArguments();
        if(bundle!=null) {
            String uid = bundle.getString("Uid");
            String photoUrl=bundle.getString("PhotoUrl");
            String displayName=bundle.getString("DisplayName").toUpperCase();
            //Picasso.get().load(photoUrl).fit().into(circleImageView);
            Picasso.get().load(photoUrl).noPlaceholder().centerCrop().rotate(270).fit().into(circleImageView);
            textView.setText(displayName);
            getPlaylistName(uid);
            playlistonClickListener(uid);
            getBacktoFragment();
        }
        if(bundle.getString("Uid").equalsIgnoreCase(mAuth.getCurrentUser().getUid())){
            circleImageView.setClickable(true);
            changePhoto(mAuth.getCurrentUser().getUid(),bundle.getString("DisplayName"),bundle.getString("PhotoUrl"));
        }
        return view;
    }

    void changePhoto(final String uid, final String displayName, final String currentPhotoUrl){
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browseImage=new Intent(Intent.ACTION_GET_CONTENT);
                browseImage.setType("image/*");
                startActivityForResult(Intent.createChooser(browseImage,"Select Picture"),RC_SELECT_IMAGE);
            }
        });
    }

    void uploadTheNewPhoto(final String uid, final String displayName, final String currentPhotoUrl){
        if(currentPhotoUrl.equalsIgnoreCase("https://firebasestorage.googleapis.com/v0/b/letmeexplore-fb83f.appspot.com/o/Users%2FPictures%2Fstduserpic.jpg?alt=media&token=e428373b-0367-4b07-bc45-3a01494e03d9")){
            userNew.setUid(mAuth.getCurrentUser().getUid());
            userNew.setDisplayName(displayName);
            storageReference = FirebaseStorage.getInstance().getReference("Users/Pictures/" + uid);
            storageReference.putFile(choosenImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String photoUrl = taskSnapshot.getDownloadUrl().toString();
                    userNew.setPhotoUrl(photoUrl);
                    myref.child(mAuth.getCurrentUser().getUid()).child("properties").setValue(userNew);
                    Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                }
            });
            Toast.makeText(getContext(), "Your Profile Picture Has Been Changed Successfully!", Toast.LENGTH_SHORT).show();
        }
        else {
            StorageReference photoRef = mStorage.getReferenceFromUrl(currentPhotoUrl);
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    userNew.setUid(uid);
                    storageReference = FirebaseStorage.getInstance().getReference("Users/Pictures/" + uid);
                    storageReference.putFile(choosenImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String photoUrl = taskSnapshot.getDownloadUrl().toString();
                            userNew.setPhotoUrl(photoUrl);
                            userNew.setDisplayName(displayName);
                            myref.child(mAuth.getCurrentUser().getUid()).child("properties").setValue(userNew);
                            Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(getContext(), "Your Profile Picture Has Been Changed Successfully!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SELECT_IMAGE&&resultCode==RESULT_OK){
            choosenImage = data.getData();
            Bundle bundle = this.getArguments();
            String uid = bundle.getString("Uid");
            String currentPhotoUrl=bundle.getString("PhotoUrl");
            String displayName=bundle.getString("DisplayName");
            uploadTheNewPhoto(uid,displayName,currentPhotoUrl);
            Picasso.get().load(choosenImage).noPlaceholder().centerCrop().fit()
                    .into((ImageView) getView().findViewById(R.id.user_profile_image));
        }
    }

    void getPlaylistName(String uid){
        myref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playList.clear();
                if (dataSnapshot.hasChild("playlists")) {
                    DatabaseControl databaseControl=new DatabaseControl();
                    playList.addAll(databaseControl.getPlaylistName(dataSnapshot.child("playlists")));
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void playlistonClickListener(final String uid){
        userPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String playlistname = playList.get(position);
                Bundle bundle=new Bundle();
                bundle.putString("Uid",uid);
                bundle.putString("PlaylistName",playlistname);
                OtherUsersSongsFragment otherUsersSongs=new OtherUsersSongsFragment();
                otherUsersSongs.setArguments(bundle);
                setFragment(otherUsersSongs);

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.addToBackStack("userprofile");
        fragmentTransaction.commit();
    }

    void getBacktoFragment(){
        backbuttonimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getFragmentManager();
                fragmentManager.popBackStack();
            }
        });

    }
}