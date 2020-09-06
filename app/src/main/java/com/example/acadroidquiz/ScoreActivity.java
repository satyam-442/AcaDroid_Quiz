package com.example.acadroidquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ScoreActivity extends AppCompatActivity {

    TextView scoredMark, totalScore, scoreText, playerUsername;
    ImageView scoreImage, doneBtn;
    Button shareBtn, doneBtna;
    String scoreGained, totalAllScore;
    int GV = 10, SV = 9, BV = 8;

    FirebaseAuth mAuth;
    String userId;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //loadAds();

        scoreText = findViewById(R.id.scoreText);
        playerUsername = findViewById(R.id.playerUsername);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("Username").getValue().toString();
                    playerUsername.setText("@"+username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        scoredMark = findViewById(R.id.scoredScore);
        scoreGained = String.valueOf(getIntent().getIntExtra("score",0));
        scoredMark.setText("Score is " + scoreGained);

        totalScore = findViewById(R.id.totalScore);
        totalAllScore = String.valueOf(getIntent().getIntExtra("total",0));
        totalScore.setText(" Out of "+totalAllScore);

        if (scoreGained.equals(GV))
        {
            scoreImage.setImageResource(R.drawable.gold);
            scoreText.setText("You are the first");
        }
        else if (scoreGained.equals(SV))
        {
            scoreImage.setImageResource(R.drawable.silver);
            scoreText.setText("You are the second");
        }
        else if (scoreGained.equals(BV))
        {
            scoreImage.setImageResource(R.drawable.bronze);
            scoreText.setText("You are the third");
        }

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