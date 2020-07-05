package com.example.acadroidquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.acadroidquiz.Adapter.Bookmark;
import com.example.acadroidquiz.Modal.QuestionM;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    public static final String FILE_NAME = "Acadroid Quiz";
    public static final String KEY_NAME = "QUESTIONS";
    RecyclerView rv_bm;

    List<QuestionM> bmList;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmark();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadAds();

        rv_bm = findViewById(R.id.rv_bookmark);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        rv_bm.setLayoutManager(manager);

        List<QuestionM> list = new ArrayList<>();

        Bookmark adapter = new Bookmark(bmList);
        rv_bm.setAdapter(adapter);
    }

    private void loadAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmark();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBookmark() {
        String json = preferences.getString(KEY_NAME, "");
        Type type = new TypeToken<List<QuestionM>>() {}.getType();
        bmList = gson.fromJson(json, type);
        if (bmList == null) {
            bmList = new ArrayList<>();
        }
    }

    private void storeBookmark() {
        String json = gson.toJson(bmList);
        editor.putString(KEY_NAME, json);
        editor.commit();
    }

}