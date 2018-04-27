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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
public class OtherUsersSongsFragment extends Fragment {

    private TextView textViewPlaylist;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private ListView trackListview;
    private  DatabaseControl databaseControl;
    private ImageView backbutton;
    private ArrayList<Song> arrayTrackList=new ArrayList<Song>();
    private ArrayAdapter<Song> arrayAdapter;
    private Other_Users_Song_CustomAdapter other_users_song_customAdapter;
    public OtherUsersSongsFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_other_users_songs, container, false);
        trackListview=(ListView)view.findViewById(R.id.tracklist_other_user);
        mAuth=FirebaseAuth.getInstance();
        backbutton=(ImageView)view.findViewById(R.id.other_users_backbuttonview);
        textViewPlaylist=(TextView)view.findViewById(R.id.other_user_playlist_name);
        databaseControl=new DatabaseControl();
        other_users_song_customAdapter=new Other_Users_Song_CustomAdapter(getContext(),arrayTrackList);
       //arrayAdapter=new ArrayAdapter<Song>(getContext(),android.R.layout.simple_list_item_1, android.R.id.text1, arrayTrackList);
        trackListview.setAdapter(other_users_song_customAdapter);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            String uid=bundle.getString("Uid");
            String playlistName=bundle.getString("PlaylistName");
            textViewPlaylist.setText(playlistName.toUpperCase());
            getTrackList(uid,playlistName);
            getBacktoFragment();
            OnitemClickListener();
        }
        return view;

    }
    void getTrackList(final String uid, final String playlistName){
        myRef.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseControl databaseControl=new DatabaseControl();
                final List<String> songKey=(databaseControl.getSongKeyList(dataSnapshot.child("playlists").child(playlistName).child("dataSongKeys")));

                myRef.child("Songs").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arrayTrackList.clear();
                        //Toast.makeText(getContext(),songKey.get(0),Toast.LENGTH_SHORT).show();
                        DatabaseControl databaseControl1=new DatabaseControl();
                        List<Song> songs=databaseControl1.getSongList(dataSnapshot);
                        ArrayList<Song> songList=databaseControl1.getMatchSongs(songs,songKey);
                        //Toast.makeText(getContext(),""+songs.size(),Toast.LENGTH_SHORT).show();
                        arrayTrackList.addAll(songList);
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
    void getBacktoFragment(){
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment();
            }
        });

    }
    private void popFragment() {
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.popBackStack();
    }
    void OnitemClickListener(){
        trackListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HomeActivity.showPopup(arrayTrackList,getContext(),position);
            }
        });
    }
    /*void FindSongTypeAndPush(final String playlistname, Context ctx){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=database.getReference();
        FirebaseAuth  mAuth=FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        Toast.makeText(ctx,"islem bitti",Toast.LENGTH_SHORT).show();
        myRef.child("Songs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseControl databaseControl =new DatabaseControl();
                final List<Song> songList= databaseControl.getSongList(dataSnapshot);
                //Toast.makeText(getContext(),"Songsdayım",Toast.LENGTH_SHORT).show();
                myRef.child("Users").child(currentUser.getUid()).child("playlists").child(playlistname).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DatabaseControl databaseControl =new DatabaseControl();
                        List<Song> songList1 =new ArrayList<>();
                        List<String> songKeyList=databaseControl.getSongKeyList(dataSnapshot);
                        songList1.addAll(databaseControl.getMatchSongs(songList,songKeyList));
                        FindSongType findSongType = new FindSongType(songList1);
                        findSongType.findSongtype();
                        String songtype= findSongType.getSongType();
                        int songvalue=findSongType.getValue();
                       //PlaylistData findSongTypeUserClass=new PlaylistData(currentUser,songvalue,,songvalue);
                        myRef.child("FavSongTypes").child("Pop").child(currentUser.getUid()).child("burak").setValue("Hello");
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

    }*/
}
