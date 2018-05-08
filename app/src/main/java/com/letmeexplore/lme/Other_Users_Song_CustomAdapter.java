package com.letmeexplore.lme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Other_Users_Song_CustomAdapter extends ArrayAdapter<Song>{
    public Other_Users_Song_CustomAdapter(Context context, ArrayList<Song> songArrayList) {
        super(context, 0, songArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Song song=getItem(position);
        convertView= LayoutInflater.from(getContext()).inflate(R.layout.other_users_songs_layout,parent,false);
        TextView songName=(TextView)convertView.findViewById(R.id.other_users_songs_name);
        TextView songuid=(TextView)convertView.findViewById(R.id.other_users_songs_uidtext);
        CircleImageView songPhoto=(CircleImageView) convertView.findViewById(R.id.other_users_song_circleimageview);
        TextView singer=(TextView)convertView.findViewById(R.id.other_users_songs_singertext);
        songName.setText(song.getSongName());
        songuid.setText(song.getSongkey());
        singer.setText(song.getSinger());
        Picasso.get().load(song.getSongPhotoUrl()).fit().into(songPhoto);
        return convertView;
    }
}
