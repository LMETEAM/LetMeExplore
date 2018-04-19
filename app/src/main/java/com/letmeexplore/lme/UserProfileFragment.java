package com.letmeexplore.lme;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
            arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, playList);
            userPlaylist.setAdapter(arrayAdapter);
            database=FirebaseDatabase.getInstance();
            myref=database.getReference("Users");
            myref.keepSynced(true);
            Bundle bundle=this.getArguments();
            if(bundle!=null) {
                String uid = bundle.getString("Uid");
                String photoUrl=bundle.getString("PhotoUrl");
                String displayName=bundle.getString("DisplayName");
                String playlistCount=bundle.getString("PlaylistCount");
                Picasso.get().load(photoUrl).resize(400, 400).centerCrop().into(circleImageView);
                textView.setText(displayName);
                getPlaylistName(uid);
                playlistonClickListener(uid);
                getBacktoFragment();
            }
        return view;
    }
    void getPlaylistName(String uid){
        myref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playList.clear();
                if (dataSnapshot.hasChild("playlists")) {
                    //Toast.makeText(getContext(),"Buradayım",Toast.LENGTH_SHORT).show();
                    //Kullanıcının playlistini çektik ve listeye aktarıp arrayadapter a bildirdik
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
                OtherUsersSongs otherUsersSongs=new OtherUsersSongs();
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
        fragmentTransaction.addToBackStack("burak");
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
