package com.letmeexplore.lme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecylerViewAdapterSearchSong extends RecyclerView.Adapter<RecylerViewAdapterSearchSong.MyViewHolder> {
    private Context mContext;
    private List<Song> mSongList;

    public RecylerViewAdapterSearchSong(Context mContext, List<Song> mSongList) {
        this.mContext = mContext;
        this.mSongList = mSongList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.cardview_search_song,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.songname.setText(mSongList.get(position).getSongName());
            holder.singer.setText(mSongList.get(position).getSinger());
            holder.songkey.setText(mSongList.get(position).getSinger());
            holder.addbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               HomeActivity.showPopup((ArrayList<Song>) mSongList,mContext,position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView songname;
        TextView singer;
        TextView songkey;
        ImageView addbutton;

        public MyViewHolder(View itemView) {
            super(itemView);
            songkey=(TextView)itemView.findViewById(R.id.cardview_search_songkey);
            songname=(TextView)itemView.findViewById(R.id.cardview_search_songname_text);
            singer=(TextView)itemView.findViewById(R.id.cardview_search_singername_text);
            addbutton=(ImageView)itemView.findViewById(R.id.cardview_search_addsong_imageview);
        }
    }
    //Not Using


    /*void showPopup(final int position) {
        final Dialog mdialog = new Dialog(mContext);
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
        final DatabaseControl databaseControl=new DatabaseControl();
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
        arrayAdapter=new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, playlist);
        listView.setAdapter(arrayAdapter);
        songName.setText(mSongList.get(position).getSongName());
        Picasso.get().load(mSongList.get(position).getSongPhotoUrl()).resize(300, 300).into(circleImageView);
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
                        final String songkey=mSongList.get(position).getSongkey();
                        final String playlistname=newplaylistname.getText().toString();
                        final String currentuser=mAuth.getCurrentUser().getUid();
                        //Yeni oluşturulan çalma listesine seçilen şarkıyı gönderir
                        myRef.child("Users").child(currentuser).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("playlists")){
                                    if(dataSnapshot.child("playlists").hasChild(playlistname)){
                                        Toast.makeText(mContext, "Playlist already exists", Toast.LENGTH_SHORT).show();
                                        newplaylistname.setText("");
                                    }else {
                                        myRef.child("Users").child(currentuser).child("playlists").child(playlistname).child("dataSongKeys").push().child("songkey").setValue(songkey);
                                        Toast.makeText(mContext,"Added to "+playlistname,Toast.LENGTH_SHORT).show();
                                        mdialog.dismiss();
                                    }
                                }else {
                                    myRef.child("Users").child(currentuser).child("playlists").child(playlistname).child("dataSongKeys").push().child("songkey").setValue(songkey);
                                    Toast.makeText(mContext,"Added to "+playlistname,Toast.LENGTH_SHORT).show();
                                    mdialog.dismiss();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }catch (Exception e){
                        Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
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
                        String choosensongkey=mSongList.get(position).getSongkey();
                        List<String> songKeyList=databaseControl.getSongKeyList(dataSnapshot.child("playlists").child(playlist.get(listposition)).child("dataSongKeys"));
                        if(!songKeyList.contains(choosensongkey)){
                            myRef.child("Users").child(currentuser).child("playlists").child(playlist.get(listposition)).child("dataSongKeys").push().child("songkey").setValue(choosensongkey);
                            Toast.makeText(mContext,"Added to "+playlist.get(listposition),Toast.LENGTH_SHORT).show();
                            mdialog.dismiss();

                        }else {
                            Toast.makeText(mContext,"This song already exists in the playlist",Toast.LENGTH_SHORT).show();
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
    }*/

}
