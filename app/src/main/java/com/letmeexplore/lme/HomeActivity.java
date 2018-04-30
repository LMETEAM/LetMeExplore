package com.letmeexplore.lme;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private HomeFragment homeFragment;
    private BooksFragment booksFragment;
    private SearchFragment searchFragment;
    private ExploreFragment exploreFragment;
    private FirebaseAuth mAuth;
    public static List<PlaylistData> compabilitylist;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mMainNav=(BottomNavigationView)findViewById(R.id.main_nav);
        mMainFrame=(FrameLayout)findViewById(R.id.main_frame);
        mAuth=FirebaseAuth.getInstance();
        homeFragment=new HomeFragment();
        setFragment(homeFragment);
        ItemSelected();
        compabilitylist=new ArrayList<>();
        CompabilityDenemsi(getApplicationContext());
    }
    void ItemSelected(){
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                            getSupportFragmentManager().popBackStackImmediate();
                        }
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_search:
                        while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                            getSupportFragmentManager().popBackStackImmediate();
                        }
                        if(searchFragment==null) searchFragment=new SearchFragment();
                        setFragment(searchFragment);
                        return true;
                    case R.id.nav_explore:
                        while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                            getSupportFragmentManager().popBackStackImmediate();
                        }
                        if(exploreFragment==null)exploreFragment=new ExploreFragment();
                        setFragment(exploreFragment);
                        return true;
                    case R.id.nav_books:
                        while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                            getSupportFragmentManager().popBackStackImmediate();
                        }
                        if(booksFragment==null) booksFragment=new BooksFragment();
                        setFragment(booksFragment);
                        return true;
                    default:return false;
                }
            }
        });

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();

    }

    static void FindSongTypeAndPush(final String playlistname, final Context ctx){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=database.getReference();
        final FirebaseAuth  mAuth=FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        //Toast.makeText(ctx,"islem bitti",Toast.LENGTH_SHORT).show();
        myRef.child("Songs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseControl databaseControl =new DatabaseControl();
                final List<Song> songList= databaseControl.getSongList(dataSnapshot);
                // Toast.makeText(getContext(),"Songsdayım",Toast.LENGTH_SHORT).show();
                myRef.child("Users").child(currentUser.getUid()).child("playlists").child(playlistname).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DatabaseControl databaseControl =new DatabaseControl();
                        List<Song> songList1 =new ArrayList<>();

                        List<String> songKeyList=databaseControl.getSongKeyList(dataSnapshot.child("dataSongKeys"));
                        String photoUrl=dataSnapshot.child("dataPhoto").child("photoUrl").getValue(String.class);
                        songList1.addAll(databaseControl.getMatchSongs(songList,songKeyList));

                        FindSongType findSongType = new FindSongType(songList1);
                        findSongType.findSongtype();
                        String songtype= findSongType.getSongType();
                        int songvalue=findSongType.getValue();
                        PlaylistData playlistData =new PlaylistData(currentUser.getUid(),songvalue,songtype,playlistname,photoUrl);
                        myRef.child("FavSongTypes").child(currentUser.getUid()).child(playlistname).setValue(playlistData);
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

    static void CompabilityDenemsi(final Context context){
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference=database.getReference();
        try {
            databaseReference.child("FavSongTypes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<PlaylistData> myplaylist=new ArrayList<>();
                    List<PlaylistData> otherplaylist=new ArrayList<>();
                    compabilitylist.clear();
                    DatabaseControl databaseControl=new DatabaseControl();
                    for(DataSnapshot uid:dataSnapshot.getChildren()) {
                        String usersid = uid.getKey();
                        String currentuid=mAuth.getCurrentUser().getUid();
                        if (usersid.equalsIgnoreCase(currentuid)){
                            myplaylist.addAll(databaseControl.getPlaylistData(dataSnapshot.child(usersid)));
                            break;
                        }
                    }
                    for (DataSnapshot uid:dataSnapshot.getChildren()){
                        if(!uid.getKey().equalsIgnoreCase(mAuth.getCurrentUser().getUid()))
                            for (DataSnapshot playlist:uid.getChildren()){
                                PlaylistData playlistData =playlist.getValue(PlaylistData.class);
                                otherplaylist.add(playlistData);
                            }
                    }
                    //Toast.makeText(context,"Buraya erişildi CompabilityDenemsei",Toast.LENGTH_SHORT).show();
                    compabilitylist.addAll(databaseControl.getCompabilityPlaylistData(myplaylist,otherplaylist));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(context,"Bir Sorunla Karşılaşıldı\n"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    static void showPopup(final ArrayList<Song> arrayTrackList, final Context context, final int position) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=database.getReference();
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currenUser=mAuth.getCurrentUser();
        final DatabaseControl databaseControl=new DatabaseControl();
        final Dialog mdialog = new Dialog(context);
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
        arrayAdapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, playlist);
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
                        final String photoUrl=arrayTrackList.get(position).getSongPhotoUrl();
                        //Yeni oluşturulan çalma listesine seçilen şarkıyı gönderir
                        myRef.child("Users").child(currentuser).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("playlists")){
                                    if(dataSnapshot.child("playlists").hasChild(playlistname)){
                                        Toast.makeText(context, "Playlist already exists", Toast.LENGTH_SHORT).show();
                                        newplaylistname.setText("");
                                    }else {
                                        myRef.child("Users").child(currentuser).child("playlists").child(playlistname).child("dataPhoto").child("photoUrl").setValue(photoUrl);
                                        myRef.child("Users").child(currentuser).child("playlists").child(playlistname).child("dataSongKeys").push().child("songkey").setValue(songkey);
                                        Toast.makeText(context,"Added to "+playlistname,Toast.LENGTH_SHORT).show();
                                        FindSongTypeAndPush(playlistname,context);
                                        mdialog.dismiss();
                                    }
                                }else {
                                    myRef.child("Users").child(currentuser).child("playlists").child(playlistname).child("dataPhoto").child("photoUrl").setValue(photoUrl);
                                    myRef.child("Users").child(currentuser).child("playlists").child(playlistname).child("dataSongKeys").push().child("songkey").setValue(songkey);
                                    Toast.makeText(context,"Added to "+playlistname,Toast.LENGTH_SHORT).show();
                                    mdialog.dismiss();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }catch (Exception e){
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
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
                        List<String> songKeyList=databaseControl.getSongKeyList(dataSnapshot.child("playlists").child(playlist.get(listposition)).child("dataSongKeys"));
                        if(!songKeyList.contains(choosensongkey)){
                            myRef.child("Users").child(currentuser).child("playlists").child(playlist.get(listposition)).child("dataSongKeys").push().child("songkey").setValue(choosensongkey);
                            Toast.makeText(context,"Added to "+playlist.get(listposition),Toast.LENGTH_SHORT).show();
                            FindSongTypeAndPush(playlist.get(listposition),context);
                            mdialog.dismiss();
                        }else {
                            Toast.makeText(context,"This song already exists in the playlist",Toast.LENGTH_SHORT).show();
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
    static void showToast(Context context,LayoutInflater layoutInflater,String text){
        TextView textView;
        View customToastRoot=layoutInflater.inflate(R.layout.toast_layout,null);
        textView=customToastRoot.findViewById(R.id.toast_text);
        textView.setText(text);
        Toast customToast=new Toast(context);
        customToast.setView(customToastRoot);
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.show();
    }
}
