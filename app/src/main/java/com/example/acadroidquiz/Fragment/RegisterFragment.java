package com.example.acadroidquiz.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acadroidquiz.HomeActivity;
import com.example.acadroidquiz.MainActivity;
import com.example.acadroidquiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterFragment extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference usersRef, usersUsernameRef;
    TextInputLayout Name, Phone, Email, Password, Username, Address;
    Button registerBtn, registerUsernameButton, registerProfileBtn;
    Dialog loadingBar;
    LinearLayout registerLinear, registerLinear1, registerLinear2;
    Spinner selectGenderSpinner;
    String gender;
    private String[] genderString = new String[]{"MALE", "FEMALE", "OTHER"};

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usersUsernameRef = FirebaseDatabase.getInstance().getReference().child("UsersUsername");
        loadingBar = new Dialog(getActivity());
        loadingBar.setContentView(R.layout.loading_ailog);
        loadingBar.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.loading_design));
        loadingBar.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingBar.setCancelable(false);

        Name = view.findViewById(R.id.nameRegister);
        Phone = view.findViewById(R.id.phoneRegister);
        Email = view.findViewById(R.id.emailRegister);
        Password = view.findViewById(R.id.passwordRegister);
        Username = view.findViewById(R.id.usernameRegister);
        Address = view.findViewById(R.id.addressRegister);

        registerBtn = view.findViewById(R.id.registerBtnF);
        registerProfileBtn = view.findViewById(R.id.registerProfileBtn);
        registerUsernameButton = view.findViewById(R.id.registerUsernameBtn);
        registerLinear = view.findViewById(R.id.registerLinear);
        registerLinear1 = view.findViewById(R.id.registerLinear1);
        registerLinear2 = view.findViewById(R.id.registerLinear2);

        selectGenderSpinner = view.findViewById(R.id.selectGenderSpinner);
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
        ArrayAdapter<String> adapter_branch = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderString);
        adapter_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectGenderSpinner.setAdapter(adapter_branch);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToRegister();
            }
        });
        registerUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToAddUsername();
            }
        });

        return view;
    }

    private void AllowUserToAddUsername() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isConnected(getActivity())) {
            showCustomDialog();
        }
    }


    private boolean isConnected(Context ctx) {

        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiCon != null && wifiCon.isConnected()) || (mobileCon != null && mobileCon.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please get connect to internet to proceed further!")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
    }

    private Boolean validateEmail() {
        String valName = Email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (valName.isEmpty()) {
            Email.setError("E-mail is empty");
            return false;
        } else if (!valName.matches(emailPattern)) {
            Email.setError("Invalid E-mail address");
            return false;
        } else {
            Email.setError(null);
            Email.setErrorEnabled(false);
            return true;
        }
    }

    private void AllowUserToRegister() {
        final String name = Name.getEditText().getText().toString();
        final String phone = Phone.getEditText().getText().toString();
        final String email = Email.getEditText().getText().toString();
        final String password = Password.getEditText().getText().toString();

        /*if (name.isEmpty()) {
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
        }*/
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Name is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getActivity(), "Phone is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Email is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Password is empty!", Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        final String uid = mAuth.getCurrentUser().getUid();
                        HashMap<String, Object> usersMap = new HashMap<>();
                        usersMap.put("Name", name);
                        usersMap.put("Phone", phone);
                        usersMap.put("Email", email);
                        usersMap.put("uid", uid);
                        usersMap.put("password", password);
                        usersMap.put("image", "default");
                        usersRef.child(uid).updateChildren(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Data Uploaded successfully!", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    //SendToMainActivity();
                                    registerLinear.setVisibility(View.INVISIBLE);
                                    registerLinear1.setVisibility(View.VISIBLE);
                                    registerLinear2.setVisibility(View.INVISIBLE);
                                    registerUsernameButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final String username = Username.getEditText().getText().toString();
                                            if (TextUtils.isEmpty(username)) {
                                                Toast.makeText(getActivity(), "Username empty", Toast.LENGTH_SHORT).show();
                                            } else {
                                                loadingBar.show();

                                                usersUsernameRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.hasChild(username))
                                                        {
                                                            Toast.makeText(getActivity(), "Username already present!", Toast.LENGTH_SHORT).show();
                                                            loadingBar.dismiss();
                                                        }
                                                        else
                                                        {
                                                            loadingBar.show();
                                                            HashMap<String, Object> usersUsernameMap = new HashMap<>();
                                                            usersUsernameMap.put("Name", name);
                                                            usersUsernameMap.put("Username", username);
                                                            usersUsernameMap.put("Phone", phone);
                                                            usersUsernameMap.put("Email", email);
                                                            usersUsernameMap.put("uid", uid);
                                                            usersUsernameMap.put("password", password);
                                                            usersUsernameMap.put("image", "default");
                                                            usersUsernameRef.child(username).updateChildren(usersUsernameMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        final HashMap<String, Object> usersMap = new HashMap<>();
                                                                        usersMap.put("Username", username);
                                                                        usersRef.child(uid).updateChildren(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                                                                                    loadingBar.dismiss();
                                                                                    registerLinear.setVisibility(View.INVISIBLE);
                                                                                    registerLinear1.setVisibility(View.INVISIBLE);
                                                                                    registerLinear2.setVisibility(View.VISIBLE);
                                                                                    registerProfileBtn.setOnClickListener(new View.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View v) {
                                                                                            final String address = Address.getEditText().getText().toString();
                                                                                            if (TextUtils.isEmpty(address)) {
                                                                                                Toast.makeText(getActivity(), "Address empty", Toast.LENGTH_SHORT).show();
                                                                                            } else {
                                                                                                loadingBar.show();
                                                                                                HashMap<String, Object> profileMap = new HashMap<>();
                                                                                                profileMap.put("Address", address);
                                                                                                profileMap.put("Gender", gender);
                                                                                                usersRef.child(uid).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            loadingBar.dismiss();
                                                                                                            SendToMainActivity();
                                                                                                        } else {
                                                                                                            String msg = task.getException().getMessage();
                                                                                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                                                                            loadingBar.dismiss();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    String msg = task.getException().getMessage();
                                                                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                                                    loadingBar.dismiss();
                                                                                }
                                                                            }
                                                                        });
                                                                        loadingBar.dismiss();
                                                                        //SendToMainActivity();
                                                                    } else {
                                                                        String msg = task.getException().getMessage();
                                                                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                                        loadingBar.dismiss();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                                /*HashMap<String, Object> usersUsernameMap = new HashMap<>();
                                                usersUsernameMap.put("Name", name);
                                                usersUsernameMap.put("Username", username);
                                                usersUsernameMap.put("Phone", phone);
                                                usersUsernameMap.put("Email", email);
                                                usersUsernameMap.put("uid", uid);
                                                usersUsernameMap.put("password", password);
                                                usersUsernameMap.put("image", "default");
                                                usersUsernameRef.child(username).updateChildren(usersUsernameMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            final HashMap<String, Object> usersMap = new HashMap<>();
                                                            usersMap.put("Username", username);
                                                            usersRef.child(uid).updateChildren(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                                                                        loadingBar.dismiss();
                                                                        registerLinear.setVisibility(View.INVISIBLE);
                                                                        registerLinear1.setVisibility(View.INVISIBLE);
                                                                        registerLinear2.setVisibility(View.VISIBLE);
                                                                        registerProfileBtn.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                final String address = Address.getEditText().getText().toString();
                                                                                if (TextUtils.isEmpty(address)) {
                                                                                    Toast.makeText(getActivity(), "Address empty", Toast.LENGTH_SHORT).show();
                                                                                } else {
                                                                                    loadingBar.show();
                                                                                    HashMap<String, Object> profileMap = new HashMap<>();
                                                                                    profileMap.put("Address", address);
                                                                                    profileMap.put("Gender", gender);
                                                                                    usersRef.child(uid).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                loadingBar.dismiss();
                                                                                                SendToMainActivity();
                                                                                            } else {
                                                                                                String msg = task.getException().getMessage();
                                                                                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                                                                loadingBar.dismiss();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });
                                                                    } else {
                                                                        String msg = task.getException().getMessage();
                                                                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                                        loadingBar.dismiss();
                                                                    }
                                                                }
                                                            });
                                                            loadingBar.dismiss();
                                                            //SendToMainActivity();
                                                        } else {
                                                            String msg = task.getException().getMessage();
                                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                            loadingBar.dismiss();
                                                        }
                                                    }
                                                })*/;
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        String msg = task.getException().getMessage();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void SendToMainActivity() {
        Intent loginIntent = new Intent(getActivity(), MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        getActivity().finish();
    }
}