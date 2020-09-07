package com.example.acadroidquiz.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.acadroidquiz.Category.CategoriesActivity;
import com.example.acadroidquiz.Logical.LogicalActivity;
import com.example.acadroidquiz.R;
import com.example.acadroidquiz.Reasoning.ReasoningActivity;
import com.example.acadroidquiz.SpeedMath.SpeedmathActivity;
import com.example.acadroidquiz.TrainBrain.TrainBrainActivity;
import com.example.acadroidquiz.WeeklyTest.WeeklyTestActivity;

public class HomeFragment extends Fragment {
    RelativeLayout categoryRelative, trainBrainRelative,  weeklyTestRelative, logicalRelative, speedMathRelative;
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

        speedMathRelative = view.findViewById(R.id.categorySpeedMath);
        speedMathRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setsActivity = new Intent(getActivity(), SpeedmathActivity.class);
                startActivity(setsActivity);
            }
        });

        trainBrainRelative = view.findViewById(R.id.categoryTrainBrain);
        trainBrainRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trainbrain = new Intent(getActivity(), TrainBrainActivity.class);
                startActivity(trainbrain);
            }
        });

        weeklyTestRelative = view.findViewById(R.id.categoryWeeklyTest);
        weeklyTestRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent weeklytest = new Intent(getActivity(), WeeklyTestActivity.class);
                startActivity(weeklytest);
            }
        });

        logicalRelative = view.findViewById(R.id.categoryLogical);
        logicalRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logical = new Intent(getActivity(), LogicalActivity.class);
                startActivity(logical);
            }
        });

        return view;
    }
}