package com.letmeexplore.lme;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LMEsongsFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ListView playlistView;
    private DatabaseControl databaseControl;
    private Other_Users_Song_CustomAdapter other_users_song_customAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<Song> songArrayList= new ArrayList<>();
    private CircleImageView circleImageViewPhoto;
    private ImageView backButton;
    public LMEsongsFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lmesongs, container, false);

        Bundle bundle = this.getArguments();
        String type = bundle.getString("type");
        circleImageViewPhoto=view.findViewById(R.id.lmesongs_circleimageview);
        playlistView = view.findViewById(R.id.lmesongs_listview);
        backButton = view.findViewById(R.id.lmesongs_backbuttonimageview);
        other_users_song_customAdapter = new Other_Users_Song_CustomAdapter(getContext(), songArrayList);
        playlistView.setAdapter(other_users_song_customAdapter);
        mAuth = FirebaseAuth.getInstance();
        databaseControl = new DatabaseControl();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        HomeActivity.showToast(getContext(),getLayoutInflater(),type);
        getSongFunction(type);
        setPlaylistView(type);
        backButtonListener();
        setOnItemClickListener();

        return view;
    }
        void getSongFunction(String ptype){
        myRef.child("LMEsongs").child(ptype).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                songArrayList.clear();
                final List<String> songKeyList = new ArrayList<>();
                songKeyList.addAll(databaseControl.getSongKeyList(dataSnapshot));
                myRef.child("Songs").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Song> songList = new ArrayList<>();
                        songList.addAll(databaseControl.getSongList(dataSnapshot));
                        ArrayList<Song> songMatchesArrayList=databaseControl.getMatchSongs(songList, songKeyList);
                        songArrayList.addAll(songMatchesArrayList);
                        other_users_song_customAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
        void setPlaylistView(String ptype){
        switch (ptype){
            case "Classic":{
                Picasso.get().load(R.drawable.rsz_classical).resize(200,200).into(circleImageViewPhoto);
                break;
            }
            case "Pop":{
                Picasso.get().load(R.drawable.rsz_1pop).resize(200,200).into(circleImageViewPhoto);
                break;
            }
            case "Dance":{
                Picasso.get().load(R.drawable.rsz_electronic).resize(200,200).into(circleImageViewPhoto);
                break;
            }
            case "HipHop":{
                Picasso.get().load(R.drawable.rsz_hiphop).resize(200,200).into(circleImageViewPhoto);
                break;
            }
            case "Jazz":{
                Picasso.get().load(R.drawable.rsz_1jazz).resize(200,200).into(circleImageViewPhoto);
                break;
            }
            case "Metal":{
                Picasso.get().load(R.drawable.rsz_1punk).resize(200,200).into(circleImageViewPhoto);
                break;
            }
            case "Rock":{
                Picasso.get().load(R.drawable.rsz_rock).resize(200,200).into(circleImageViewPhoto);
                break;
            }
            case "Indie":{
                Picasso.get().load(R.drawable.rsz_1indie).resize(200,200).into(circleImageViewPhoto);
                break;

            }
            default: {Picasso.get().load(R.drawable.daddylesssons).into(circleImageViewPhoto);break;}
        }
        }
        void backButtonListener(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getFragmentManager();
                fragmentManager.popBackStack();
            }
        });




        }
        void setOnItemClickListener(){
        playlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeActivity.showPopup(songArrayList,getContext(),position);

            }
        });



        }
}
