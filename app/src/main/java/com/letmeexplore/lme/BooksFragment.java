package com.letmeexplore.lme;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment {
    ArrayList<String> playlistNamesFireBase;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ListView listView;
    FirebaseAuth firebaseAuth;
    ArrayAdapter<String> adapter;

    public BooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_books,container,false);
        listView = rootView.findViewById(R.id.bookcase_listview);
        playlistNamesFireBase = new ArrayList<String>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = firebaseAuth.getInstance();

        myRef = firebaseDatabase.getReference("Users");
        getDataFromFirebase(firebaseAuth.getCurrentUser().getUid());

        adapter = new ArrayAdapter<String>(getActivity(),R.layout.bookcase_custom,R.id.bookcaseCustom_textview,playlistNamesFireBase);
        listView.setAdapter(adapter);
        playlistOnClickListener(firebaseAuth.getCurrentUser().getUid().toString());
        // Inflate the layout for this fragment*/
        return rootView;
    }

    void getDataFromFirebase(String uid){
        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playlistNamesFireBase.clear();
                if (dataSnapshot.hasChild("playlists")) {
                    DatabaseControl databaseControl=new DatabaseControl();
                    playlistNamesFireBase.addAll(databaseControl.getPlaylistName(dataSnapshot.child("playlists")));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void playlistOnClickListener(final String uid){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String playlistName = playlistNamesFireBase.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("Uid",uid);
                bundle.putString("PlaylistName",playlistName);
                OtherUsersSongsFragment otherUsersSongs = new OtherUsersSongsFragment();
                otherUsersSongs.setArguments(bundle);
                setFragment(otherUsersSongs);

            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.addToBackStack("bookcase");
        fragmentTransaction.commit();
    }
}
