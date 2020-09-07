package com.example.acadroidquiz.Logical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.acadroidquiz.Adapter.Category;
import com.example.acadroidquiz.Adapter.LogicalAdapter;
import com.example.acadroidquiz.Category.CategoriesActivity;
import com.example.acadroidquiz.HomeActivity;
import com.example.acadroidquiz.Modal.CategoryModel;
import com.example.acadroidquiz.Modal.LogicalModel;
import com.example.acadroidquiz.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogicalActivity extends AppCompatActivity {

    RecyclerView rv;
    DatabaseReference categories;
    public static List<LogicalModel> list;
    Dialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logical);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Logical");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadAds();

        categories = FirebaseDatabase.getInstance().getReference().child("Logical");

        loadingBar = new Dialog(this);
        loadingBar.setContentView(R.layout.loading_ailog);
        loadingBar.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loading_design));
        loadingBar.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingBar.setCancelable(false);

        rv = findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(manager);


        list = new ArrayList<>();

        final LogicalAdapter adapter = new LogicalAdapter(list);
        rv.setAdapter(adapter);

        loadingBar.show();

        categories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    List<String> sets = new ArrayList<>();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Sets").getChildren()) {
                        sets.add(dataSnapshot2.getKey());
                    }
                    list.add(new LogicalModel(dataSnapshot1.child("name").getValue().toString(),
                            dataSnapshot1.child("url").getValue().toString(),
                            dataSnapshot1.getKey(),
                            sets
                    ));
                }
                adapter.notifyDataSetChanged();
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LogicalActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                finish();
            }
        });
    }

    private void loadAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (item.getItemId() == R.id.logout) {
            new AlertDialog.Builder(LogicalActivity.this, R.style.Theme_AppCompat_Light_Dialog)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loadingBar.show();
                            FirebaseAuth.getInstance().signOut();
                            Intent main = new Intent(LogicalActivity.this, HomeActivity.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(main);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .setIcon(R.drawable.logoutblack)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}