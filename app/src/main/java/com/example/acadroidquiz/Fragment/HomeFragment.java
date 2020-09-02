package com.example.acadroidquiz.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.acadroidquiz.CategoriesActivity;
import com.example.acadroidquiz.R;

public class HomeFragment extends Fragment {
    RelativeLayout categoryRelative;
    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        categoryRelative = view.findViewById(R.id.categoryRelative);
        categoryRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categories = new Intent(getActivity(), CategoriesActivity.class);
                startActivity(categories);
            }
        });
        return view;
    }
}