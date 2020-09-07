package com.example.acadroidquiz.Logical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.acadroidquiz.Adapter.Grid;
import com.example.acadroidquiz.Adapter.LogicalGridAdapter;
import com.example.acadroidquiz.Category.CategoriesActivity;
import com.example.acadroidquiz.R;

import java.util.List;

public class LogicalSetsActivity extends AppCompatActivity {

    GridView gridView;
    LogicalGridAdapter adapter ;
    String setsNo;
    List<String> sets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logical_sets);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        gridView = findViewById(R.id.grid_view);
        sets = LogicalActivity.list.get(getIntent().getIntExtra("position", 0)).getSetss();
        adapter = new LogicalGridAdapter(sets,getIntent().getStringExtra("title"));
        gridView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}