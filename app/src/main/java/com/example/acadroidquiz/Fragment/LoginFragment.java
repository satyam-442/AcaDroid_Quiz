package com.example.acadroidquiz.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    FirebaseAuth mAuth;
    TextInputLayout Email, Password;
    Button CreateAccount;
    TextView forgotPassword;
    Dialog loadingBar;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new Dialog(getActivity());

        Email = view.findViewById(R.id.emailLogin);
        Password = view.findViewById(R.id.passwordLogin);
        CreateAccount = view.findViewById(R.id.loginBtn);
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });

        forgotPassword = view.findViewById(R.id.forgotPassword);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            SendToMainActivity();
        }
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

    private void AllowUserToLogin() {
        String email = Email.getEditText().getText().toString();
        String password = Password.getEditText().getText().toString();

        /*if (email.isEmpty()) {
            Email.setError("Email Required");
            if (password.isEmpty()) {
                Password.setError("Password Required");
            } else {
                Password.setError(null);
            }
        } else {
            Email.setError(null);
        }*/

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Email is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Password is empty!", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setContentView(R.layout.loading_ailog);
            loadingBar.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.loading_design));
            loadingBar.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            loadingBar.setCancelable(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SendToMainActivity();
                        loadingBar.dismiss();
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
        Intent regIntent = new Intent(getActivity(), MainActivity.class);
        regIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(regIntent);
        getActivity().finish();
    }
}