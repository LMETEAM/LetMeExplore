package com.letmeexplore.lme;

import android.app.ActionBar;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private HomeFragment homeFragment;
    private BooksFragment booksFragment;
    private SearchFragment searchFragment;
    private ExploreFragment exploreFragment;
    private FirebaseAuth mAuth;
    private android.support.v7.app.ActionBar actionBar;
    private FirebaseUser firebaseUser;

    @Override
    public void onBackPressed() {

    }

    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mMainNav=(BottomNavigationView)findViewById(R.id.main_nav);
        mMainFrame=(FrameLayout)findViewById(R.id.main_frame);
        mAuth=FirebaseAuth.getInstance();
        homeFragment=new HomeFragment();
        searchFragment=new SearchFragment();
        exploreFragment=new ExploreFragment();
        booksFragment=new BooksFragment();
        setFragment(homeFragment);
        ItemSelected();

    }
    void ItemSelected(){
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_search:
                        setFragment(searchFragment);
                        return true;
                    case R.id.nav_explore:
                        setFragment(exploreFragment);
                        return true;
                    case R.id.nav_books:
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
}
