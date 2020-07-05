package com.example.acadroidquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;

public class ScoreActivity extends AppCompatActivity {

    TextView scoredMark, totalScore;
    Button shareBtn, doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        loadAds();

        scoredMark = findViewById(R.id.scoredScore);
        scoredMark.setText(String.valueOf(getIntent().getIntExtra("score",0)));

        totalScore = findViewById(R.id.totalScore);
        totalScore.setText("Out of "+String.valueOf(getIntent().getIntExtra("total",0)));

        shareBtn = findViewById(R.id.shareBtn);
        doneBtn = findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}