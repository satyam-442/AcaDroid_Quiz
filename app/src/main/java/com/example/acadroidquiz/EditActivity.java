package com.example.acadroidquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {

    CircleImageView setupProfileImage;
    Uri imageUri;
    StorageReference storagePicRef;
    String myUrl = "";
    String checker = "";
    StorageTask uploadTask;
    Button setupSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

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