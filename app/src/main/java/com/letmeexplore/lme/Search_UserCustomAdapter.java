package com.letmeexplore.lme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burak on 15.04.2018.
 */

public class Search_UserCustomAdapter extends ArrayAdapter<UserDetails> {

    public Search_UserCustomAdapter(Context context, ArrayList<UserDetails> chatList) {
        super(context,0,chatList);

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserDetails user=getItem(position);
        convertView= LayoutInflater.from(getContext()).inflate(R.layout.search_userlist_layout,parent,false);
        TextView userName=(TextView)convertView.findViewById(R.id.search_username_text);
        TextView userSonglistC=(TextView)convertView.findViewById(R.id.search_usersonglistcount_text);
        ImageView userImgage=(ImageView)convertView.findViewById(R.id.search_profileimage);
        String UserFullName=user.getName()+" "+user.getSurname();
        userName.setText(UserFullName);
        userSonglistC.setText("Playlist Count:"+user.getPlaylistCount());
        TextView user_uid=(TextView)convertView.findViewById(R.id.search_user_uid_text);
        user_uid.setText(user.getUid());
        Picasso.with(getContext()).load(user.getPhotoUrl()).fit().centerCrop().into(userImgage);
        return convertView;
    }
}