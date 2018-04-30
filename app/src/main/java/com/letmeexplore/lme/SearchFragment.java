package com.letmeexplore.lme;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
public class SearchFragment extends Fragment {
    private EditText searchText;
    private ImageView deleteText;
    private ListView userRList;
    private RecyclerView recyclerViewSongList;
    private FirebaseAuth mAuth;
    private List<Song> songList=new ArrayList<>();
    private FirebaseUser firebaseUser;
    private ArrayList<UserDetails> userArrayList =new ArrayList<UserDetails>();
    private ImageView getDeleteText;
    private RecylerViewAdapterSearchSong recylerViewAdapterSearchSong;
    private Search_UserCustomAdapter search_userCustomAdapter;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment
        userRList=view.findViewById(R.id.search_user_list);
        recyclerViewSongList=view.findViewById(R.id.search_songrecyclerlist);
        getDeleteText=view.findViewById(R.id.search_deletetext);
        searchText= view.findViewById(R.id.search_searchtext);
        deleteText=view.findViewById(R.id.search_deletetext);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        myRef.keepSynced(true);
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        recylerViewAdapterSearchSong=new RecylerViewAdapterSearchSong(getContext(),songList);
        search_userCustomAdapter =new Search_UserCustomAdapter(getContext(),userArrayList);
        userRList.setAdapter(search_userCustomAdapter);
        recyclerViewSongList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSongList.setAdapter(recylerViewAdapterSearchSong);
        userRlistOnClickListener();
        setSearchText();
        setDeleteText();
        return view;

    }
    void setSearchText(){

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Yazma işlemi tamamlanmışmı onu kontrol ediyor eğer klavyeden tamama tıklanmışsa klavyeyi kapatıyor
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                   hideKeyboard(getView());
                    return true;
                }
                return false;
            }
        });
        //
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                deleteText.setVisibility(View.VISIBLE);
                if(s.toString().isEmpty()){
                    userArrayList.clear();
                    search_userCustomAdapter.notifyDataSetChanged();
                    songList.clear();
                    songList.clear();
                    recylerViewAdapterSearchSong.notifyDataSetChanged();

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    songList.clear();
                    recylerViewAdapterSearchSong.notifyDataSetChanged();
                    userArrayList.clear();
                    search_userCustomAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(final Editable s) {
                /*Kod ne yapar??
                * Veritabanından kullanıcı aramayı sağlar
                * Bulunan kullanıcının properties bilgileri alınır
                * daha sonra kullanıcı playlist oluşturmuşmu ona bakılır oluşturmuşsa
                * playlist sayısını bulur ve User.class tan türetilmiş yeni bir sınıf olan UserDetails.class a parametre olarak
                 * (User,PlayListSayısı) şeklini gönderir. Oluşturmamışsa yine aynısını yapar fakat bu sefer (User,"0") şeklini gönderir
                 * Sonra UserDatailsten oluşturmuş olduğumuz nesneyi userArrayList listemize ekler. */

                //********************************
                    //User Search and Song Search
               if(!s.toString().isEmpty()) {
                   //---User Search---
                    myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userArrayList.clear();
                               Log.e("DONGU",""+dataSnapshot.getChildrenCount());
//                              search_userCustomAdapter.notifyDataSetChanged();
                            for (DataSnapshot ds:dataSnapshot.getChildren()){

                                if(userArrayList.size()>3) break;
                                if (!s.toString().isEmpty()){
                                if(ds.child("properties").child("displayName").getValue(String.class).toString().toUpperCase().startsWith(s.toString().toUpperCase())){
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
                            if(userArrayList.size()>3){
                                userArrayList.add(3,new UserDetails(new User(),null));
                            }
                            if(userArrayList.isEmpty()){
                                userRList.setVisibility(View.GONE);
                            }else {
                                userRList.setVisibility(View.VISIBLE);
                            }
                            search_userCustomAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                   //---Song Search---
                   myRef.child("Songs").addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                            songList.clear();
                            for (DataSnapshot ds:dataSnapshot.getChildren()){
                                if(ds.child("properties").child("songName").getValue(String.class).toString().toUpperCase().startsWith(s.toString().toUpperCase())||
                                        ds.child("properties").child("singer").getValue(String.class).toString().toUpperCase().startsWith(s.toString().toUpperCase())){
                                    if(songList.size()>10){//en fazla 10 tane eşleşen şarkı gösterir gereksiz yere tüm şarkılar çekilip kıyaslanmasın diye
                                        break;
                                    }
                                    if(!s.toString().isEmpty()){ Song song=ds.child("properties").getValue(Song.class);
                                    songList.add(song);}
                                    else {songList.clear();
                                   recylerViewAdapterSearchSong.notifyDataSetChanged();
                                   break;
                                   }

                                }
                            }
                            recylerViewAdapterSearchSong.notifyDataSetChanged();
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

                }else{
                   songList.clear();
                   recylerViewAdapterSearchSong.notifyDataSetChanged();
                    deleteText.setVisibility(View.GONE);
                    userArrayList.clear();
                    search_userCustomAdapter.notifyDataSetChanged();
                }
            }
        };
        searchText.addTextChangedListener(textWatcher);
    }
    void setDeleteText(){
        getDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songList.clear();
                recylerViewAdapterSearchSong.notifyDataSetChanged();
                searchText.setText("");
                userArrayList.clear();
                search_userCustomAdapter.notifyDataSetChanged();

            }
        });
    }
    void userRlistOnClickListener(){
        userRList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=3){Bundle bundle=new Bundle();
                bundle.putString("Uid",userArrayList.get(position).getUid());
                bundle.putString("PhotoUrl",userArrayList.get(position).getPhotoUrl());
                bundle.putString("DisplayName",userArrayList.get(position).getDisplayName());
                bundle.putString("PlaylistCount",userArrayList.get(position).getPlaylistCount());
                UserProfileFragment userProfileFragment=new UserProfileFragment();
                userProfileFragment.setArguments(bundle);
                setFragment(userProfileFragment);
                hideKeyboard(view);
              //  Toast.makeText(getContext(),userArrayList.get(position).getUid(),Toast.LENGTH_SHORT).show();

                //------Close Keyboard-----
                    /*InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.hideSoftInputFromWindow(getActivity().getWindow( ).getAttributes().token, 0);*/
            }
            else {
                Bundle bundle=new Bundle();
                bundle.putString("searchName",searchText.getText().toString());
                SeeMoreUsersFragment seeMoreUsersFragment=new SeeMoreUsersFragment();
                seeMoreUsersFragment.setArguments(bundle);
                setFragment(seeMoreUsersFragment);
                hideKeyboard(view);
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.addToBackStack("search");
        fragmentTransaction.commit();

    }
    public void hideKeyboard(View view) {

        //Keyboard Closing Method
        InputMethodManager inputMethodManager =(InputMethodManager)getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public void onPause() {
        super.onPause();

    }


}

