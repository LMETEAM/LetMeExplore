package com.letmeexplore.lme;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class SeeMoreUsersFragment extends Fragment {

    private ImageView backButton;
    private TextView matchesName;
    private ListView matchesList;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private ArrayList<UserDetails> userArrayList =new ArrayList<UserDetails>();
    private Search_UserCustomAdapter search_userCustomAdapter;

    public SeeMoreUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_see_more_users, container, false);
        backButton=(ImageView)view.findViewById(R.id.seemore_backbutton_imageview);
        matchesName=(TextView)view.findViewById(R.id.seemore_matchesname_text);
        matchesList=(ListView)view.findViewById(R.id.seemore_listview);
        database=FirebaseDatabase.getInstance();
        search_userCustomAdapter=new Search_UserCustomAdapter(getContext(),userArrayList);
        matchesList.setAdapter(search_userCustomAdapter);
        myRef=database.getReference("Users");
        mAuth=FirebaseAuth.getInstance();
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            final String searchname=bundle.getString("searchName");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userArrayList.clear();
                    Log.e("DONGU",""+dataSnapshot.getChildrenCount());
                        //search_userCustomAdapter.notifyDataSetChanged();
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        if (!searchname.isEmpty()){
                            if(ds.child("properties").child("displayName").getValue(String.class).toString().toUpperCase().startsWith(searchname.toUpperCase())){
                                User user =ds.child("properties").getValue(User.class);
                                if(ds.hasChild("playlists")){
                                    Long songlistCOunt= ds.child("playlists").getChildrenCount();
                                    String songlCount=songlistCOunt.toString();
                                    UserDetails userDetails=new UserDetails(user,songlCount);
                                    userArrayList.add(userDetails);

                                    //  Log.e("DONGU",user.getDisplayName());
                                }
                                else{
                                    // Log.e("DONGU",user.getDisplayName());
                                    userArrayList.add(new UserDetails(user,"0"));
                                }
                            }
                        }
                    }

                    search_userCustomAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            setBackButtonListener();
            setMatchesListListener();
        }

        return view;
    }
    void setBackButtonListener(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment();
            }
        });
    }
    private void getFragment() {
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.popBackStack();
    }
    void setMatchesListListener(){
        matchesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putString("Uid",userArrayList.get(position).getUid());
                bundle.putString("PhotoUrl",userArrayList.get(position).getPhotoUrl());
                bundle.putString("DisplayName",userArrayList.get(position).getDisplayName());
                bundle.putString("PlaylistCount",userArrayList.get(position).getPlaylistCount());
                UserProfileFragment userProfileFragment=new UserProfileFragment();
                userProfileFragment.setArguments(bundle);
                setFragment(userProfileFragment);
            }
        });
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.addToBackStack("seemore");
        fragmentTransaction.commit();
    }
}
