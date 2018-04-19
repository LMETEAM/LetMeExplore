package com.letmeexplore.lme;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherUsersSongs extends Fragment {

    private TextView textViewPlaylist;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ListView trackListview;
    private ImageView backbutton;
    private ArrayList<String> arrayTrackList;
    private ArrayAdapter<String> arrayAdapter;
    public OtherUsersSongs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_other_users_songs, container, false);
        trackListview=(ListView)view.findViewById(R.id.tracklist_other_user);
        backbutton=(ImageView)view.findViewById(R.id.other_users_backbuttonview);
        textViewPlaylist=(TextView)view.findViewById(R.id.other_user_playlist_name);
        arrayTrackList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, android.R.id.text1, arrayTrackList);
        trackListview.setAdapter(arrayAdapter);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            String uid=bundle.getString("Uid");
            String playlistName=bundle.getString("PlaylistName");
            textViewPlaylist.setText(playlistName.toUpperCase());
            getTrackList(uid,playlistName);
            getBacktoFragment();
        }
        return view;

    }
    void getTrackList(final String uid, final String playlistName){
        myRef.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseControl databaseControl=new DatabaseControl();
                final List<String> songKey=(databaseControl.getSongKeyList(dataSnapshot.child("playlists").child(playlistName)));
                //
                myRef.child("Songs").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arrayTrackList.clear();
                       // Toast.makeText(getContext(),songKey.get(0),Toast.LENGTH_SHORT).show();
                        DatabaseControl databaseControl1=new DatabaseControl();
                        List<Song> songs=databaseControl1.getSongList(dataSnapshot);
                        ArrayList<Song> songList=databaseControl1.getMatchSongs(songs,songKey);
                        //Toast.makeText(getContext(),""+songs.size(),Toast.LENGTH_SHORT).show();
                        for (Song song:songList){
                            arrayTrackList.add(song.getSongName());
                        }
                        arrayAdapter.notifyDataSetChanged();
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
    void getBacktoFragment(){
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment();
            }
        });

    }
    private void setFragment() {
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.popBackStack();
    }
}
