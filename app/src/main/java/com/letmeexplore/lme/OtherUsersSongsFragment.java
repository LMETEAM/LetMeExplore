package com.letmeexplore.lme;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_other_users_songs, container, false);
        trackListview=(ListView)view.findViewById(R.id.tracklist_other_user);
        mAuth=FirebaseAuth.getInstance();
        backbutton=(ImageView)view.findViewById(R.id.other_users_backbuttonview);
        textViewPlaylist=(TextView)view.findViewById(R.id.other_user_playlist_name);
       databaseControl=new DatabaseControl();
        other_users_song_customAdapter=new Other_Users_Song_CustomAdapter(getContext(),arrayTrackList);
       // arrayAdapter=new ArrayAdapter<Song>(getContext(),android.R.layout.simple_list_item_1, android.R.id.text1, arrayTrackList);
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
                setFragment();
            }
        });

    }
    private void setFragment() {
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.popBackStack();
    }
    void OnitemClickListener(){
        trackListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showPopup(position);

            }
        });
    }
    void showPopup(final int position) {
        final Dialog mdialog = new Dialog(getContext());
        CircleImageView circleImageView;
        final TextView songName;
        final TextView addtoList;
        final TextView cancel;
        final EditText newplaylistname;
        final TextView createNewList;
        final ListView listView;
        final TextView createtext;
        final List<String> playlist=new ArrayList<String>();
        final ArrayAdapter arrayAdapter;
        mdialog.setContentView(R.layout.song_custompopup);
        mdialog.getWindow().getAttributes().windowAnimations =R.style.UptoDown;
        addtoList=(TextView)mdialog.findViewById(R.id.popup_addtolisttext);
        newplaylistname=(EditText)mdialog.findViewById(R.id.popup_newlistnametext);
        createtext=(TextView)mdialog.findViewById(R.id.popup_createtext);
        cancel=(TextView)mdialog.findViewById(R.id.popup_cancel);
        songName = (TextView) mdialog.findViewById(R.id.popup_songnametext);
        createNewList = (TextView) mdialog.findViewById(R.id.popup_createnewlisttext);
        circleImageView = (CircleImageView) mdialog.findViewById(R.id.popup_circleimageview);
        listView = (ListView) mdialog.findViewById(R.id.popup_listview);
        arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, playlist);
        listView.setAdapter(arrayAdapter);
        songName.setText(arrayTrackList.get(position).getSongName());
        Picasso.get().load(arrayTrackList.get(position).getSongPhotoUrl()).resize(300, 300).into(circleImageView);
        createNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newplaylistname.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                addtoList.setVisibility(View.GONE);
                createtext.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newplaylistname.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                addtoList.setVisibility(View.VISIBLE);
                createtext.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
            }
        });
        createtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newplaylistname.getText().toString().isEmpty()){
                    try {
                        final String songkey=arrayTrackList.get(position).getSongkey();
                        final String playlistname=newplaylistname.getText().toString();
                        final String currentuser=mAuth.getCurrentUser().getUid();
                        //Yeni oluşturulan çalma listesine seçilen şarkıyı gönderir
                        myRef.child("Users").child(currentuser).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("playlists")){
                                    if(dataSnapshot.child("playlists").hasChild(playlistname)){
                                        Toast.makeText(getContext(), "Playlist already exists", Toast.LENGTH_SHORT).show();
                                        newplaylistname.setText("");
                                    }else {
                                        myRef.child("Users").child(currentuser).child("playlists").child(playlistname).push().child("songkey").setValue(songkey);
                                        Toast.makeText(getContext(),"Added to "+playlistname,Toast.LENGTH_SHORT).show();
                                        mdialog.dismiss();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }catch (Exception e){
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int listposition, long id) {
                myRef.child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentuser=mAuth.getCurrentUser().getUid();
                        String choosensongkey=arrayTrackList.get(position).getSongkey();
                       List<String> songKeyList=databaseControl.getSongKeyList(dataSnapshot.child("playlists").child(playlist.get(listposition)));
                        if(!songKeyList.contains(choosensongkey)){
                            myRef.child("Users").child(currentuser).child("playlists").child(playlist.get(position)).push().child("songkey").setValue(choosensongkey);
                            Toast.makeText(getContext(),"Added to "+playlist.get(position),Toast.LENGTH_SHORT).show();
                            mdialog.dismiss();

                        }else {
                            Toast.makeText(getContext(),"This song already exists in the playlist",Toast.LENGTH_SHORT).show();
                            mdialog.dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                playlist.clear();
                if(dataSnapshot.hasChild("playlists")){
                playlist.addAll(databaseControl.getPlaylistName(dataSnapshot.child("playlists")));
                arrayAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.show();
    }
}
