package com.letmeexplore.lme;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

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
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean doubleBackToExitPressedOnce = false;

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
}
