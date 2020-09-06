package com.example.acadroidquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acadroidquiz.Modal.QuestionM;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    public static final String FILE_NAME = "Acadroid Quiz";
    public static final String KEY_NAME = "QUESTIONS";

    TextView questionText, indicatorText;
    FloatingActionButton bookmarkBtn;
    LinearLayout optionsContainer;
    Button shareBtn, nextBtn;
    int count = 0, position = 0, score = 0;
    List<QuestionM> list;
    String setId;
    Dialog loadingBar;
    DatabaseReference questionRef;

    List<QuestionM> bmList;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Gson gson;
    int matchedQuestPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadAds();

        questionRef = FirebaseDatabase.getInstance().getReference();

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        loadingBar = new Dialog(this);
        loadingBar.setContentView(R.layout.loading_ailog);
        loadingBar.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loading_design));
        loadingBar.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingBar.setCancelable(false);

        setId = getIntent().getStringExtra("setId");

        questionText = findViewById(R.id.questionText);
        indicatorText = findViewById(R.id.indicatorText);
        bookmarkBtn = findViewById(R.id.bookmark_btn);
        optionsContainer = findViewById(R.id.optionsContainer);
        shareBtn = findViewById(R.id.shareBtn);

        list = new ArrayList<>();

        loadingBar.show();

        questionRef.child("Sets").child(setId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String id = dataSnapshot1.getKey();
                    String quest = dataSnapshot1.child("question").getValue().toString();
                    String optA = dataSnapshot1.child("optiona").getValue().toString();
                    String optB = dataSnapshot1.child("optionb").getValue().toString();
                    String optC = dataSnapshot1.child("optionc").getValue().toString();
                    String optD = dataSnapshot1.child("optiond").getValue().toString();
                    String correctAnswer = dataSnapshot1.child("correctAns").getValue().toString();
                    list.add(new QuestionM(id, quest, optA, optB, optC, optD, correctAnswer, setId));
                }
                if (list.size() > 0) {
                    for (int i = 0; i < 4; i++) {
                        optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkAnswer((Button) v);
                            }
                        });
                    }

                    nextBtn = findViewById(R.id.nextBtn);
                    playAnim(questionText, 0, list.get(position).getQuestionn());

                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextBtn.setEnabled(false);
                            nextBtn.setAlpha(0.7f);
                            enableOption(true);
                            position++;
                            if (position == list.size()) {
                                //score activity
                                Intent scoreActivity = new Intent(QuestionsActivity.this, ScoreActivity.class);
                                scoreActivity.putExtra("score", score);
                                scoreActivity.putExtra("total", list.size());
                                startActivity(scoreActivity);
                                finish();
                                //13.32
                                return;
                            }
                            count = 0;
                            playAnim(questionText, 0, list.get(position).getQuestionn());
                        }
                    });

                    shareBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String body = list.get(position).getQuestionn() + "\n" +
                                    list.get(position).getOptionaa() + "\n" +
                                    list.get(position).getOptionbb() + "\n" +
                                    list.get(position).getOptioncc() + "\n" +
                                    list.get(position).getOptiondd();

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "AcaDroid Quiz Challenge");
                            shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                            startActivity(Intent.createChooser(shareIntent,"Share Quiz via"));
                        }
                    });
                } else {
                    Toast.makeText(QuestionsActivity.this, "No Questions", Toast.LENGTH_SHORT).show();
                    finish();
                }
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });

        getBookmark();
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelMatched()){
                    bmList.remove(matchedQuestPosition);
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                }
                else
                {
                    bmList.add(list.get(position));
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.border_filled));
                }
            }
        });

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

    private void checkAnswer(Button selectedOption) {
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if (selectedOption.getText().toString().equals(list.get(position).getCorrectAnss())) {
            //correct ans
            score++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        } else {
            //incorrect ans
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctAnswer = (Button) optionsContainer.findViewWithTag(list.get(position).getCorrectAnss());
            correctAnswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
    }

    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            optionsContainer.getChildAt(i).setEnabled(enable);
            if (enable) {
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#716B6D")));
            }
        }
    }

    private void playAnim(final View view, final int value, final String datda) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4) {
                    String option = "";
                    if (count == 0) {
                        option = list.get(position).getOptionaa();
                    } else if (count == 1) {
                        option = list.get(position).getOptionbb();
                    } else if (count == 2) {
                        option = list.get(position).getOptioncc();
                    } else if (count == 3) {
                        option = list.get(position).getOptiondd();
                    }

                    playAnim(optionsContainer.getChildAt(count), 0, option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (value == 0) {
                    try {
                        ((TextView) view).setText(datda);
                        indicatorText.setText(position + 1 + "/" + list.size());
                        if (modelMatched()){
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.border_filled));
                        }
                        else
                        {
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                        }
                    } catch (ClassCastException e) {
                        ((Button) view).setText(datda);
                    }
                    view.setTag(datda);
                    playAnim(view, 1, datda);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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

    private boolean modelMatched() {
        boolean matched = false;
        int i = 0;
        for (QuestionM questionM : bmList) {
            if (questionM.getQuestionn().equals(list.get(position).getQuestionn())
                    && questionM.getCorrectAnss().equals(list.get(position).getCorrectAnss())
                    && questionM.getSetIdd().equals(list.get(position).getSetIdd())) {
                matched = true;
                matchedQuestPosition = i;
            }
            i++;
        }
        return matched;
    }

    private void storeBookmark() {
        String json = gson.toJson(bmList);
        editor.putString(KEY_NAME, json);
        editor.commit();
    }

}
//11:00