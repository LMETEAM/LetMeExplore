package com.letmeexplore.lme;


import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private EditText searchText;
    private ImageView deleteText;
    private ListView userRList;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private ArrayList<UserDetails> userArrayList =new ArrayList<UserDetails>();
    private ImageView getDeleteText;
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
        userRList=(ListView)view.findViewById(R.id.search_user_list);
        getDeleteText=(ImageView)view.findViewById(R.id.search_deletetext);
        searchText=(EditText) view.findViewById(R.id.search_searchtext);
        deleteText=(ImageView)view.findViewById(R.id.search_deletetext);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        search_userCustomAdapter =new Search_UserCustomAdapter(getContext(),userArrayList);
        userRList.setAdapter(search_userCustomAdapter);
        userRlistOnClickListener();
        setSearchText();
        setDeleteText();
        return view;

    }
    void setSearchText(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                deleteText.setVisibility(View.VISIBLE);
                if(s.toString().isEmpty()){
                    userArrayList.clear();
                    search_userCustomAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    userArrayList.clear();
                    search_userCustomAdapter.notifyDataSetChanged();

                }
                if(userArrayList.size()>3){
                    userArrayList.add(3,new UserDetails(new User(),null));
                    userArrayList.remove(4);
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
                 * Sonra UserDatailten oluşturmuş olduğumuz nesneyi userArrayList listemize ekler. */

                //********************************
               if(!s.toString().isEmpty()) {
                    myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userArrayList.clear();
//                            search_userCustomAdapter.notifyDataSetChanged();
                            for (DataSnapshot ds:dataSnapshot.getChildren()){
                                Log.e("DONGU","Devam");
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
                                userArrayList.remove(4);
                            }
                            search_userCustomAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
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
                Toast.makeText(getContext(),userArrayList.get(position).getUid(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}

