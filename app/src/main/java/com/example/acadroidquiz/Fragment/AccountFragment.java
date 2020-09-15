package com.example.acadroidquiz.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acadroidquiz.EditActivity;
import com.example.acadroidquiz.HomeActivity;
import com.example.acadroidquiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    ImageView editIcon;
    CircleImageView profileIcon;
    TextView accountName, accountUsername;
    Button accountProfileBtn, accountContactBtn, accountFeedbackBtn, accountAboutBtn, accountLogoutBtn;
    FragmentManager fragmentManager;
    ChipNavigationBar bottomNavigationView;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    String currentUserId;
    Dialog loadingBar;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        /*Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Account");
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        loadingBar = new Dialog(getActivity());
        loadingBar.setContentView(R.layout.loading_ailog);
        loadingBar.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.loading_design));
        loadingBar.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingBar.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        loadingBar.show();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("Name").getValue().toString();
                    String username = snapshot.child("Username").getValue().toString();

                    accountName.setText(name);
                    accountUsername.setText("@" + username);

                    final String image = snapshot.child("image").getValue().toString();
                    if (!image.equals("default")) {
                        Picasso.with(getActivity()).load(image).placeholder(R.drawable.default_avatar).into(profileIcon);
                        Picasso.with(getActivity()).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(profileIcon, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity()).load(image).placeholder(R.drawable.default_avatar).into(profileIcon);
                            }
                        });
                    }
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bottomNavigationView = view.findViewById(R.id.bottom_nav);

        profileIcon = view.findViewById(R.id.accountImage);
        editIcon = view.findViewById(R.id.editProfile);
        accountName = view.findViewById(R.id.accountName);
        accountUsername = view.findViewById(R.id.accountUsername);
        accountProfileBtn = view.findViewById(R.id.accountProfileBtn);
        accountLogoutBtn = view.findViewById(R.id.accountLogoutBtn);

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getActivity(), EditActivity.class);
                startActivity(edit);
            }
        });

        accountProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                ProfileFragment profileFragment = new ProfileFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, profileFragment).addToBackStack(null).commit();
            }
        });

        accountContactBtn = view.findViewById(R.id.accountContactBtn);
        accountContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                ContactusFragment contactusFragment = new ContactusFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, contactusFragment).addToBackStack(null).commit();
            }
        });

        accountFeedbackBtn = view.findViewById(R.id.accountFeedbackBtn);
        accountFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                FeedbackFragment feedbackFragment = new FeedbackFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, feedbackFragment).addToBackStack(null).commit();
            }
        });

        accountAboutBtn = view.findViewById(R.id.accountAboutBtn);
        accountAboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                AboutFragment aboutFragment = new AboutFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, aboutFragment).addToBackStack(null).commit();
            }
        });

        accountLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent logout = new Intent(getActivity(), HomeActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
                getActivity().finish();
            }
        });
        return view;
    }
}