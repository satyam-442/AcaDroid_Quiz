package com.example.acadroidquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String currentUserId;
    DatabaseReference usersRef;
    TextInputLayout nameEdit, phoneEdit, emailEdit, usernameEdit, addressEdit;
    Spinner selectGenderSpinner;
    String gender;
    Button updateProfile;
    private String[] genderString = new String[]{"MALE", "FEMALE", "OTHER"};

    CircleImageView setupProfileImage;
    Uri imageUri;
    StorageReference storagePicRef;
    String myUrl = "";
    String checker = "";
    StorageTask uploadTask;
    Button setupSelectImage;
    Dialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        storagePicRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        loadingBar = new Dialog(this);
        loadingBar.setContentView(R.layout.loading_ailog);
        loadingBar.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loading_design));
        loadingBar.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingBar.setCancelable(false);

        nameEdit = findViewById(R.id.nameEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        usernameEdit = findViewById(R.id.usernameEdit);
        addressEdit = findViewById(R.id.addressEdit);
        emailEdit = findViewById(R.id.emailEdit);
        updateProfile = findViewById(R.id.updateProfile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserProfile();
            }
        });

        selectGenderSpinner = findViewById(R.id.selectGenderSpinner);
        selectGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                // TODO Auto-generated method stub
                ((TextView) arg0.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) arg0.getChildAt(0)).setTextSize(17);
                gender = (String) selectGenderSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        ArrayAdapter<String> adapter_branch = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_spinner_item, genderString);
        adapter_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectGenderSpinner.setAdapter(adapter_branch);

        setupProfileImage = findViewById(R.id.setupProfileImage);

        setupSelectImage = findViewById(R.id.setupSelectImage);
        setupSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(EditActivity.this);
            }
        });

        loadingBar.show();

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("Name").getValue().toString();
                    String usernameOld = snapshot.child("Username").getValue().toString();
                    String email = snapshot.child("Email").getValue().toString();
                    String gender = snapshot.child("Gender").getValue().toString();
                    String phone = snapshot.child("Phone").getValue().toString();
                    String address = snapshot.child("Address").getValue().toString();

                    nameEdit.getEditText().setText(name);
                    usernameEdit.getEditText().setText(usernameOld);
                    emailEdit.getEditText().setText(email);
                    addressEdit.getEditText().setText(address);
                    //genderEdit.setText(gender);
                    phoneEdit.getEditText().setText(phone);

                    final String image = snapshot.child("image").getValue().toString();
                    if (!image.equals("default")) {
                        setupSelectImage.setText("Change Image");
                        Picasso.with(EditActivity.this).load(image).placeholder(R.drawable.default_avatar).into(setupProfileImage);
                        Picasso.with(EditActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(setupProfileImage, new Callback() {
                            @Override
                            public void onSuccess() { }

                            @Override
                            public void onError() {
                                Picasso.with(EditActivity.this).load(image).placeholder(R.drawable.default_avatar).into(setupProfileImage);
                            }
                        });
                    }
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    private void UpdateUserProfile() {
        final String name = nameEdit.getEditText().getText().toString();
        final String phone = phoneEdit.getEditText().getText().toString();
        final String email = emailEdit.getEditText().getText().toString();
        final String username = usernameEdit.getEditText().getText().toString();
        final String address = addressEdit.getEditText().getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(EditActivity.this, "Name is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(EditActivity.this, "Phone is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(EditActivity.this, "Email is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(EditActivity.this, "Username is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(EditActivity.this, "Address is empty!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.show();
            if (imageUri != null) {
                final StorageReference fileref = storagePicRef.child(name + ".jpg");
                uploadTask = fileref.putFile(imageUri);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return fileref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            HashMap<String, Object> userMapImg = new HashMap<String, Object>();
                            userMapImg.put("image", myUrl);
                            usersRef.updateChildren(userMapImg);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
            }

            HashMap<String, Object> usersMap = new HashMap<>();
            usersMap.put("Name", name);
            usersMap.put("Phone", phone);
            usersMap.put("Email", email);
            usersMap.put("Gender", gender);
            usersMap.put("Address", address);
            usersRef.child(currentUserId).updateChildren(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        loadingBar.dismiss();
                        Toast.makeText(EditActivity.this, "Data Updated Successfully!!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String msg = task.getException().getMessage();
                        Toast.makeText(EditActivity.this,msg , Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            setupProfileImage.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error! Try again.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}