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
import java.util.Random;

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
    public static List<PlaylistDataRandom> randomList;



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
        randomList = new ArrayList<>();
        Compability(getApplicationContext());

       LmeSongsEdit();
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
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final FirebaseAuth  mAuth = FirebaseAuth.getInstance();
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

    static void userRandom(final Context context){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabaseRef = mDatabase.getReference();
        try {
            mDatabaseRef.child("Users").limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<PlaylistDataRandom> playlists = new ArrayList<>();
                    randomList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String mDsUid = ds.getKey();
                        String mCurrentUid = mAuth.getCurrentUser().getUid();
                        if (mDsUid.equalsIgnoreCase(mCurrentUid)) {}
                        else if (ds.hasChild("playlists")) {
                            for (DataSnapshot playlist : ds.child("playlists").getChildren()) {
                                PlaylistDataRandom playlistData = new PlaylistDataRandom(mDsUid,playlist.getKey(),playlist.child("dataPhoto").child("photoUrl").getValue().toString());
                                playlists.add(playlistData);
                            }
                        }
                        else{
                            continue;
                        }
                    }
                    randomList.addAll(playlists);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    static void Compability(final Context context){
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



    void LmeSongsEdit(){
        final DatabaseReference myRefSongs;
        FirebaseDatabase databaseSongs;
        databaseSongs=FirebaseDatabase.getInstance();
        myRefSongs=databaseSongs.getReference();
        myRefSongs.child("Songs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Song> Classic=new ArrayList<>();
                List<Song> Pop=new ArrayList<>();
                List<Song> Rock=new ArrayList<>();
                List<Song> Indie=new ArrayList<>();
                List<Song> HipHop=new ArrayList<>();
                List<Song> Dance=new ArrayList<>();
                List<Song> Jazz=new ArrayList<>();
                List<Song> Metal=new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Song song =ds.child("properties").getValue(Song.class);
                    String SongType=song.getSongType();
                    // Toast.makeText(getContext(),SongType,Toast.LENGTH_SHORT).show();
                    switch (SongType){
                        case "Classic":{
                            Classic.add(song);
                            //myRefSongs.child("LMEsongs").child("Classic").child(song.getSongkey()).setValue(song);
                            break;
                        }
                        case "Pop":{
                            Pop.add(song);
                            //  myRefSongs.child("LMEsongs").child("Pop").child(song.getSongkey()).setValue(song);
                            break;
                        }
                        case "Rock":{
                            Rock.add(song);
                            // myRefSongs.child("LMEsongs").child("Rock").child(song.getSongkey()).setValue(song);
                            break;
                        }
                        case "Jazz":{
                            Jazz.add(song);
                            // myRefSongs.child("LMEsongs").child("Jazz").child(song.getSongkey()).setValue(song);
                            break;
                        }
                        case "Indie":{
                            Indie.add(song);
                            // myRefSongs.child("LMEsongs").child("Indie").child(song.getSongkey()).setValue(song);
                            break;
                        }
                        case "Metal":{
                            Metal.add(song);
                            // myRefSongs.child("LMEsongs").child("Punk").child(song.getSongkey()).setValue(song);
                            break;
                        }
                        case "HipHop":{
                            HipHop.add(song);
                            // myRefSongs.child("LMEsongs").child("HipHop").child(song.getSongkey()).setValue(song);
                            break;
                        }
                        case "Dance":{
                            Dance.add(song);
                            //  myRefSongs.child("LMEsongs").child("Electronic").child(song.getSongkey()).setValue(song);
                            break;
                        }
                        default:break;
                    }
                }
                myRefSongs.child("LMEsongs").child("Classic").getRef().removeValue();
                myRefSongs.child("LMEsongs").child("Rock").getRef().removeValue();
                myRefSongs.child("LMEsongs").child("Jazz").getRef().removeValue();
                myRefSongs.child("LMEsongs").child("HipHop").getRef().removeValue();
                myRefSongs.child("LMEsongs").child("Indie").getRef().removeValue();
                myRefSongs.child("LMEsongs").child("Dance").getRef().removeValue();
                myRefSongs.child("LMEsongs").child("Metal").getRef().removeValue();
                myRefSongs.child("LMEsongs").child("Pop").getRef().removeValue();
                Random generator = new Random();
                for (int i=0;i<10;i++){
                    int random=generator.nextInt(99);
                    myRefSongs.child("LMEsongs").child("Rock").child(Rock.get(random).getSongkey()).setValue(Rock.get(random));
                    myRefSongs.child("LMEsongs").child("Jazz").child(Jazz.get(random).getSongkey()).setValue(Jazz.get(random));
                    myRefSongs.child("LMEsongs").child("HipHop").child(HipHop.get(random).getSongkey()).setValue(HipHop.get(random));
                    myRefSongs.child("LMEsongs").child("Dance").child(Dance.get(random).getSongkey()).setValue(Dance.get(random));
                    myRefSongs.child("LMEsongs").child("Indie").child(Indie.get(random).getSongkey()).setValue(Indie.get(random));
                    myRefSongs.child("LMEsongs").child("Metal").child(Metal.get(random).getSongkey()).setValue(Metal.get(random));
                    myRefSongs.child("LMEsongs").child("Pop").child(Pop.get(random).getSongkey()).setValue(Pop.get(random));
                    myRefSongs.child("LMEsongs").child("Classic").child(Classic.get(random).getSongkey()).setValue(Classic.get(random));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    static void showPopup(final ArrayList<Song> arrayTrackList, final Context context, final int position) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
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
        Picasso.get().load(arrayTrackList.get(position).getSongPhotoUrl()).fit().into(circleImageView);
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

    /*void songaaktar(){
        final DatabaseReference myRefSongs;
        FirebaseDatabase databaseSongs;
        databaseSongs=FirebaseDatabase.getInstance();
        myRefSongs=databaseSongs.getReference();
        myRefSongs.child("Yedek").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Song> songList=new ArrayList<>();
               for (DataSnapshot ds:dataSnapshot.getChildren()){
                   String songname=ds.child("songName").getValue(String.class);
                   String songkey= String.valueOf(ds.child("songkey").getValue(Long.class));
                   String photoUrl=ds.child("songPhotoUrl").getValue(String.class);
                   String singer=ds.child("singer").getValue(String.class);
                   String syear= String.valueOf(ds.child("year").getValue(Long.class));
                   String stype=ds.child("songType").getValue(String.class);
                   Song song=new Song(songname,stype,singer,syear,photoUrl,songkey);
                   songList.add(song);
               }
               for (int i=0;i<songList.size();i++){
                   myRefSongs.child("Songs").child(songList.get(i).getSongkey()).child("properties").setValue(songList.get(i));
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
}
