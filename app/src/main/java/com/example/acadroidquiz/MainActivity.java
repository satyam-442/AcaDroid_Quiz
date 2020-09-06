package com.example.acadroidquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.acadroidquiz.Fragment.HomeFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    ChipNavigationBar bottomNavigationView;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_nav_main);
        if (savedInstanceState == null)
        {
            bottomNavigationView.setItemSelected(R.id.home,true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,homeFragment).commit();
        }

        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i)
                {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.bookmark:
                        fragment = new BookmarkFragment();
                        break;
                    case R.id.account:
                        fragment = new AccountFragment();
                        break;
                }
                if (fragment!=null)
                {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container,fragment).commit();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "ERROR OCCURRED...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MobileAds.initialize(this);

        //loadAds();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId())
        {
            case R.id.home:
                selectedFragment = new HomeFragment();
                break;

            case R.id.bookmark:
                selectedFragment = new BookmarkFragment();
                break;

            case R.id.account:
                selectedFragment = new AccountFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            SendToLoginActivity();
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