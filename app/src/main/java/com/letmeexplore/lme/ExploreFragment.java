package com.letmeexplore.lme;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewPlayList;
    private RecyclerViewAdapterExploreAccording adapterExploreAccording;
    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_explore, container, false);
        swipeRefreshLayout=view.findViewById(R.id.explore_swiperefreshlayout);
        recyclerViewPlayList= view.findViewById(R.id.accordingrecyclerview);
        adapterExploreAccording=new RecyclerViewAdapterExploreAccording(getContext(),HomeActivity.compabilitylist,getFragmentManager());
        recyclerViewPlayList.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerViewPlayList.setAdapter(adapterExploreAccording);
        setSwipeRefreshLayoutListener();
        //Yapılacaklar
        //Algrotima metodunu tasarla ve oluştur
        //Cardview Oluştur
        //ReyclerViewAdapter Oluştur
        //HomeActivity.FindSongTypeDENEME(getContext());
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

    }
    void setSwipeRefreshLayoutListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HomeActivity.showToast(getContext(),getLayoutInflater(),"According to your lists...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        HomeActivity.CompabilityDenemsi(getContext());
                    }
                },2000);


            }
        });
    }

}
