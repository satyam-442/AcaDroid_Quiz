package com.example.acadroidquiz.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.acadroidquiz.Adapter.Bookmark;
import com.example.acadroidquiz.Modal.QuestionM;
import com.example.acadroidquiz.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkFragment extends Fragment {

    public static final String FILE_NAME = "Acadroid Quiz";
    public static final String KEY_NAME = "QUESTIONS";
    RecyclerView rv_bm;

    List<QuestionM> bmList;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Gson gson;

    public BookmarkFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        preferences = getActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmark();
        /*Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Bookmarks");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        //loadAds();

        rv_bm = view.findViewById(R.id.rv_bookmark);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rv_bm.setLayoutManager(manager);

        List<QuestionM> list = new ArrayList<>();

        Bookmark adapter = new Bookmark(bmList);
        rv_bm.setAdapter(adapter);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        storeBookmark();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            getActivity().finish();
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