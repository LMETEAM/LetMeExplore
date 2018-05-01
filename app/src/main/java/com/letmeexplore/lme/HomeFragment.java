package com.letmeexplore.lme;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
        ımageView =  view.findViewById(R.id.imageView);
        songadd=view.findViewById(R.id.imageView2);
        imgPop = view.findViewById(R.id.imageViewPop);
        imgPunk=view.findViewById(R.id.imageViewPunk2);
        imgClassic = view.findViewById(R.id.imageViewClassic);
        imgRock = view.findViewById(R.id.imageViewRock);
        imgIndie = view.findViewById(R.id.imageViewIndie);
        imgJazz = view.findViewById(R.id.imageViewJazz3);
        imgElectronic = view.findViewById(R.id.imageViewElectronic);
        imgHiphop = view.findViewById(R.id.imageViewHiphop);
        mAuth=FirebaseAuth.getInstance();
        setImageView2Visible();

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
        imgViewOnClickListener(imgClassic,"Classic");
        imgViewOnClickListener(imgPop,"Pop");
        imgViewOnClickListener(imgJazz,"Jazz");
        imgViewOnClickListener(imgIndie,"Indie");
        imgViewOnClickListener(imgPunk,"Punk");
        imgViewOnClickListener(imgHiphop,"HipHop");
        imgViewOnClickListener(imgRock,"Rock");
        imgViewOnClickListener(imgElectronic,"Electronic");
        //SongsChildEdit();
        return view;
    }
    void SongsChildEdit(){
         final DatabaseReference myRefSongs;
         FirebaseDatabase databaseSongs;
         databaseSongs=FirebaseDatabase.getInstance();
         myRefSongs=databaseSongs.getReference();
         myRefSongs.child("Yedek").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 for (DataSnapshot ds:dataSnapshot.getChildren()){

                     String songName=ds.child("songName").getValue(String.class);
                     String songType=ds.child("songType").getValue(String.class);
                     String singer=ds.child("singer").getValue(String.class);
                     String songPhotoUrl=ds.child("songPhotoUrl").getValue(String.class);
                     Long year=ds.child("year").getValue(Long.class);
                     String yearString= String.valueOf(year);
                     Long songkey=ds.child("songkey").getValue(Long.class);
                     String songkeyString= String.valueOf(songkey);
                     Song song=new Song(songName,songType,singer,yearString,songPhotoUrl,songkeyString);
                     myRefSongs.child("Songs").child(songkeyString).child("properties").setValue(song);
                 }
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
    }
   void imgViewOnClickListener(ImageView imgView, final String ptype){
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LMEsongsFragment lmEsongsFragment = new LMEsongsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type",ptype);
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

}
