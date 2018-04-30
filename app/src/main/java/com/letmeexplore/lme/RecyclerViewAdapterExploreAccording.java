package com.letmeexplore.lme;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterExploreAccording extends RecyclerView.Adapter<RecyclerViewAdapterExploreAccording.MyViewHolder>{

    private Context mContext;
    private List<PlaylistData> playlistDataList;
    FragmentManager fragmentManager;

    public RecyclerViewAdapterExploreAccording(Context mcontext, List<PlaylistData> playlistDataList,FragmentManager fragmentManager) {
        this.mContext = mcontext;
        this.playlistDataList = playlistDataList;
        this.fragmentManager=fragmentManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        view=layoutInflater.inflate(R.layout.cardview_explore_accordinglist,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.playLisName.setText(playlistDataList.get(position).getPlaylistname());
        Picasso.get().load(playlistDataList.get(position).getPhotoUrl()).networkPolicy(NetworkPolicy.OFFLINE).resize(100,100)
                .into(holder.circleImageViewPhoto);
        holder.ımageViewAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherUsersSongsFragment otherUsersSongsFragment=new OtherUsersSongsFragment();
                Bundle bundle=new Bundle();
                bundle.putString("Uid",playlistDataList.get(position).getUid());
                bundle.putString("PlaylistName",playlistDataList.get(position).getPlaylistname());
                otherUsersSongsFragment.setArguments(bundle);
                setFragment(otherUsersSongsFragment);
                //   HomeActivity.showPopup(playlistDataList,mContext,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageViewPhoto;
        TextView playLisName;
        ImageView ımageViewAddButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            circleImageViewPhoto= itemView.findViewById(R.id.explore_according_circleimageview);
            playLisName= itemView.findViewById(R.id.explore_according_playlistNameText);
            ımageViewAddButton=itemView.findViewById(R.id.explore_according_addimage);

        }
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.addToBackStack("search");
        fragmentTransaction.commit();
    }


}
