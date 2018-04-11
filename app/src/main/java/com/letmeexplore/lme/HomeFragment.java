package com.letmeexplore.lme;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

import java.util.List;


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
        mAuth=FirebaseAuth.getInstance();
        setImageView2Visible();
        ExitButton = (ImageView) view.findViewById(R.id.imageView6);
        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent ıntent=new Intent(getContext(),MainActivity.class);
                startActivity(ıntent);
                Toast.makeText(getContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        songadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent=new Intent(getContext(),AddSongActivity.class);
                startActivity(ıntent);
                getActivity().finish();
            }
        });
        return view;
    }
    void setImageView2Visible(){
            database=FirebaseDatabase.getInstance();
            myRef=database.getReference("Users/"+mAuth.getCurrentUser().getUid());
            myRef.addValueEventListener(new ValueEventListener() {
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
