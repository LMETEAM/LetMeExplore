package com.letmeexplore.lme;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private ImageView ımageView;
    private ImageView songadd;
    private ImageView ExitButton;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private User user;
    private String burak;
    private ImageView imgPop;
    private ImageView imgClassic;
    private ImageView imgRock;
    private ImageView imgIndie;
    private ImageView imgJazz;
    private ImageView imgElectronic;
    private ImageView imgPunk;
    private ImageView imgHiphop;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ımageView = (ImageView) view.findViewById(R.id.imageView);
        songadd=(ImageView)view.findViewById(R.id.imageView2);
        imgPop = (ImageView)view.findViewById(R.id.imageViewPop);
        imgClassic = (ImageView)view.findViewById(R.id.imageViewClassic);
        imgRock = (ImageView)view.findViewById(R.id.imageViewRock);
        imgIndie = (ImageView)view.findViewById(R.id.imageViewIndie);
        imgJazz = (ImageView)view.findViewById(R.id.imageViewJazz3);
        imgElectronic = (ImageView)view.findViewById(R.id.imageViewElectronic);
        imgHiphop = (ImageView)view.findViewById(R.id.imageViewHiphop);
        mAuth=FirebaseAuth.getInstance();
        setImageView2Visible();
        imgClassicOnClickListener();
        imgRockOnClickListener();
        ExitButton = (ImageView) view.findViewById(R.id.imageView6);
        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        songadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AddSongActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

   void imgClassicOnClickListener(){
        imgClassic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LMEsongsFragment lmEsongsFragment = new LMEsongsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type","Classic");
                lmEsongsFragment.setArguments(bundle);
                setFragment(lmEsongsFragment);
            }
        });

   }
   void imgRockOnClickListener(){
        imgRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LMEsongsFragment lmEsongsFragment = new LMEsongsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type","Rock");
                lmEsongsFragment.setArguments(bundle);
                setFragment(lmEsongsFragment);
            }
        });
   }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.addToBackStack("home");
        fragmentTransaction.commit();

    }

    void setImageView2Visible(){
            database=FirebaseDatabase.getInstance();
            myRef=database.getReference("Users/"+mAuth.getCurrentUser().getUid());
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("admin")){
                        songadd.setVisibility(View.VISIBLE);
                    }
                    else songadd.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    void FindSongTypeDENEME(){
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();


        myRef.child("Songs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseControl databaseControl =new DatabaseControl();
               final List<Song> songList= databaseControl.getSongList(dataSnapshot);
               // Toast.makeText(getContext(),"Songsdayım",Toast.LENGTH_SHORT).show();
                myRef.child("Users").child(currentUser.getUid()).child("playlists").child("pop").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DatabaseControl databaseControl =new DatabaseControl();
                        List<Song> songList1 =new ArrayList<>();
                        List<String> songKeyList=databaseControl.getSongKeyList(dataSnapshot);
                        songList1.addAll(databaseControl.getMatchSongs(songList,songKeyList));
                        //Toast.makeText(getContext(),"islem bitti",Toast.LENGTH_SHORT).show();
                        FindSongType findSongType = new FindSongType(songList1);
                        findSongType.findSongtype();
                        String songtype= findSongType.getSongType();
                        Toast.makeText(getContext(),"Fav SongType: "+songtype,Toast.LENGTH_SHORT).show();
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
}
