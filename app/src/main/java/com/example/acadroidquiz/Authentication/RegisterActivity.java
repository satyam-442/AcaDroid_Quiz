package com.example.acadroidquiz.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acadroidquiz.MainActivity;
import com.example.acadroidquiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText Name, Phone, Email, Password;
    FirebaseAuth mAuth;
    DatabaseReference usersRef;
    Button registerBtn;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        Name = findViewById(R.id.nameRegister);
        Phone = findViewById(R.id.phoneRegister);
        Email = findViewById(R.id.emailRegister);
        Password = findViewById(R.id.passwordRegister);
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToRegister();
            }
        });
        loadingBar = new ProgressDialog(this);
    }

    private void AllowUserToRegister() {
        final String name = Name.getText().toString();
        final String phone = Phone.getText().toString();
        final String email = Email.getText().toString();
        final String password = Password.getText().toString();

        if (name.isEmpty()) {
            Name.setError("Name Required");
            //return;
            if (phone.isEmpty()) {
                Phone.setError("Phone Required");
                //return;
                if (email.isEmpty()) {
                    Email.setError("Email Required");
                    //return;
                    if (password.isEmpty()) {
                        Password.setError("Password Required");
                        //return;
                    } else {
                        Password.setError(null);
                    }
                } else {
                    Email.setError(null);
                }
            } else {
                Phone.setError(null);
            }
        } else {
            Name.setError(null);
        }

        loadingBar.setMessage("please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String uid = mAuth.getCurrentUser().getUid();
                    HashMap<String , Object> usersMap = new HashMap<>();
                    usersMap.put("Name", name);
                    usersMap.put("Phone", phone);
                    usersMap.put("Email", email);
                    usersMap.put("uid", uid);
                    usersMap.put("password", password);
                    usersMap.put("image", "default");
                    usersRef.child(uid).updateChildren(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Data Uploaded successfully!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                SendToMainActivity();
                            }
                        }
                    });
                }
                else {
                    String msg = task.getException().getMessage();
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void SendToMainActivity() {
        Intent loginIntent = new Intent(this, MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    public void NavigateToLoginPage(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}