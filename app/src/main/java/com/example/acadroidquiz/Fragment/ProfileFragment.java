package com.example.acadroidquiz.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acadroidquiz.EditActivity;
import com.example.acadroidquiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference usersRef;
    CircleImageView profileImage;
    TextView profileName, profileUsername, profileAddress, profileGender, profilePhone, profileEmail;
    String currentUserId;
    Dialog loadingBar;
    TextView editProfileTV;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        loadingBar = new Dialog(getActivity());
        loadingBar.setContentView(R.layout.loading_ailog);
        loadingBar.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.loading_design));
        loadingBar.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingBar.setCancelable(false);

        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        profileUsername = view.findViewById(R.id.profileUsername);
        profileAddress = view.findViewById(R.id.profileAddress);
        profileGender = view.findViewById(R.id.profileGender);
        profilePhone = view.findViewById(R.id.profilePhone);
        profileEmail = view.findViewById(R.id.profileEmail);

        editProfileTV = view.findViewById(R.id.editProfileTV);
        editProfileTV.setPaintFlags(editProfileTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        editProfileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getActivity(), EditActivity.class);
                startActivity(edit);
            }
        });

        loadingBar.show();

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("Name").getValue().toString();
                    String username = snapshot.child("Username").getValue().toString();
                    String email = snapshot.child("Email").getValue().toString();
                    String gender = snapshot.child("Gender").getValue().toString();
                    String phone = snapshot.child("Phone").getValue().toString();
                    String address = snapshot.child("Address").getValue().toString();

                    profileName.setText(name);
                    profileUsername.setText(username);
                    profileEmail.setText(email);
                    profileAddress.setText(address);
                    profileGender.setText(gender);
                    profilePhone.setText(phone);

                    final String image = snapshot.child("image").getValue().toString();
                    if (!image.equals("default")) {
                        Picasso.with(getActivity()).load(image).placeholder(R.drawable.default_avatar).into(profileImage);
                        Picasso.with(getActivity()).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(profileImage, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity()).load(image).placeholder(R.drawable.default_avatar).into(profileImage);
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
        return view;
    }
}