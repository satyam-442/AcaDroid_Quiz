package com.example.acadroidquiz.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.acadroidquiz.EditActivity;
import com.example.acadroidquiz.HomeActivity;
import com.example.acadroidquiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    ImageView editIcon;
    CircleImageView profileIcon;
    TextView accountName, accountUsername;
    Button accountProfileBtn, accountContactBtn, accountFeedbackBtn, accountAboutBtn, accountLogoutBtn;
    FragmentManager fragmentManager;
    ChipNavigationBar bottomNavigationView;
    FirebaseAuth mAuth;

    public AccountFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Account");
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

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
                fragmentManager.beginTransaction().replace(R.id.fragment_container,profileFragment).addToBackStack(null).commit();
                            }
        });

        accountContactBtn = view.findViewById(R.id.accountContactBtn);
        accountContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                ContactusFragment contactusFragment = new ContactusFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,contactusFragment).addToBackStack(null).commit();
            }
        });

        accountFeedbackBtn = view.findViewById(R.id.accountFeedbackBtn);
        accountFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                FeedbackFragment feedbackFragment = new FeedbackFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,feedbackFragment).addToBackStack(null).commit();
            }
        });

        accountAboutBtn = view.findViewById(R.id.accountAboutBtn);
        accountAboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                AboutFragment aboutFragment = new AboutFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,aboutFragment).addToBackStack(null).commit();
            }
        });

        accountLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent logout = new Intent(getActivity(), HomeActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
                getActivity().finish();
            }
        });
        return view;
    }
}