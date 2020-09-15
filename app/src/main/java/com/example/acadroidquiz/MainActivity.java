package com.example.acadroidquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.acadroidquiz.Category.CategoriesActivity;
import com.example.acadroidquiz.Fragment.AccountFragment;
import com.example.acadroidquiz.Fragment.BookmarkFragment;
import com.example.acadroidquiz.Fragment.ContactusFragment;
import com.example.acadroidquiz.Fragment.FeedbackFragment;
import com.example.acadroidquiz.Fragment.HomeFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    ChipNavigationBar bottomNavigationView;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //DRAWER NAVIGATION CODE
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);

        //TOOLBAR CODE
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //navigationView.setCheckedItem(R.id.home_main);

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.home_main);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        }

        /*bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.home_main:
                        fragment = new HomeFragment();
                        break;
                    case R.id.bookmark_main:
                        fragment = new BookmarkFragment();
                        break;
                    case R.id.profile_main:
                        fragment = new AccountFragment();
                        break;
                }
                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment).commit();
                } else {
                    Toast.makeText(MainActivity.this, "ERROR OCCURRED...", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        MobileAds.initialize(this);
        //loadAds();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.home_main:
                selectedFragment = new HomeFragment();
                break;

            case R.id.bookmark_main:
                selectedFragment = new BookmarkFragment();
                break;

            case R.id.profile_main:
                selectedFragment = new AccountFragment();
                break;

            case R.id.contactus_main:
                selectedFragment = new ContactusFragment();
                break;

            case R.id.feedback_main:
                selectedFragment = new FeedbackFragment();
                break;

            case R.id.logout_main:
                mAuth.signOut();
                Intent logout = new Intent(MainActivity.this, HomeActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
                finish();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).addToBackStack(null).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            SendToLoginActivity();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed(
            );
        }
    }

    private void SendToLoginActivity() {
        Intent regIntent = new Intent(this, HomeActivity.class);
        regIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(regIntent);
        finish();
    }

    /*private void loadAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }*/

    public void StartQuizProcess(View view) {
        Intent categories = new Intent(this, CategoriesActivity.class);
        startActivity(categories);
    }

    public void StartBookmarkPage(View view) {
        Intent categories = new Intent(this, BookmarkActivity.class);
        startActivity(categories);
    }
}