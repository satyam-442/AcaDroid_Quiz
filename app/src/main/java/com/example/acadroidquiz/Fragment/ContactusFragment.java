package com.example.acadroidquiz.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acadroidquiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ContactusFragment extends Fragment {

    private TextView eMail;
    private TextView pNumber;
    private TextView wAddress;
    TextInputLayout contactName, contactEmail, contactPhone, contactSpinnerValue, contactSpinnerMessage;
    Spinner selectOptions;
    Button sendAppointRequestButton;
    String requestType[] = {"-Select a type-","Business Purpose","Others"};
    ArrayAdapter<String> adapter;
    String result, currentUserId, appointmentId;
    FirebaseAuth mAuth;
    DatabaseReference appointmentRef;
    ProgressDialog loadingBar;
    Toolbar toolbar;

    public ContactusFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactus, container, false);

/*        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Contact Us");
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        appointmentRef = FirebaseDatabase.getInstance().getReference().child("AppointmentReq").child("Users");

        loadingBar = new ProgressDialog(getActivity());

        eMail = (TextView) view.findViewById(R.id.contact_email);
        eMail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                semdMail();
            }
        });
        pNumber = (TextView) view.findViewById(R.id.contact_number);
        pNumber.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                contactBnn();
            }
        });
        wAddress = (TextView) view.findViewById(R.id.contact_web);
        wAddress.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent bnnLink = new Intent(Intent.ACTION_VIEW);
                bnnLink.setData(Uri.parse("https://letcprograme.blogspot.com"));
                startActivity(bnnLink);
            }
        });

        contactName = (TextInputLayout) view.findViewById(R.id.contactName);
        contactEmail = (TextInputLayout) view.findViewById(R.id.contactEmail);
        contactPhone = (TextInputLayout) view.findViewById(R.id.contactPhone);
        contactSpinnerValue = (TextInputLayout) view.findViewById(R.id.contactSpinnerValue);
        contactSpinnerMessage = (TextInputLayout) view.findViewById(R.id.contactSpinnerMessage);

        selectOptions = (Spinner) view.findViewById(R.id.selectOptions);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,requestType);
        selectOptions.setAdapter(adapter);
        selectOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        result = "-Select a type-";
                        contactSpinnerValue.getEditText().setText(result);
                        break;
                    case 1:
                        result = "Business Purpose";
                        contactSpinnerValue.getEditText().setText(result);
                        break;
                    case 2:
                        result = "Other";
                        contactSpinnerValue.getEditText().setText(result);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        sendAppointRequestButton = (Button) view.findViewById(R.id.sendAppointRequestButton);
        sendAppointRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestForAppointment();
            }
        });

        return view;
    }

    private void RequestForAppointment()
    {
        String name = contactName.getEditText().getText().toString();
        String email = contactEmail.getEditText().getText().toString();
        String phone = contactPhone.getEditText().getText().toString();
        String spinnerValue = contactSpinnerValue.getEditText().getText().toString();
        String message = contactSpinnerMessage.getEditText().getText().toString();

        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        appointmentId = saveCurrentDate + saveCurrentTime;

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(spinnerValue) && TextUtils.isEmpty(message))
        {
            Toast.makeText(getActivity(), "Field's are empty.", Toast.LENGTH_SHORT).show();
            sendAppointRequestButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            loadingBar.setMessage("please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            HashMap<String,Object> appointmentMap = new HashMap<String,Object>();
            appointmentMap.put("fullname", name);
            appointmentMap.put("appointmentType" , spinnerValue);
            appointmentMap.put("appointmentMessage",message);
            appointmentMap.put("uid",currentUserId);
            appointmentMap.put("phone",phone);
            appointmentMap.put("appointmentId",appointmentId);
            appointmentMap.put("time",saveCurrentTime);
            appointmentMap.put("date",saveCurrentDate);
            /*feedbackMap.put("Contact",contact);
            feedbackMap.put("UID",uid);
            feedbackMap.put("Password",password);
            feedbackMap.put("image","default");*/
            appointmentRef.child(spinnerValue).child(currentUserId).child(appointmentId).updateChildren(appointmentMap).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        //SendUserToMainActivity();
                        Toast.makeText(getActivity(), "Request for appointment successfully submitted. We'll let you know if confirmed...", Toast.LENGTH_LONG).show();
                        contactName.getEditText().setText("");
                        contactEmail.getEditText().setText("");
                        contactPhone.getEditText().setText("");
                        contactSpinnerValue.getEditText().setText("");
                        contactSpinnerMessage.getEditText().setText("");
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(getActivity(), "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void contactBnn()
    {
        String number = "9049927365";
        Uri call = Uri.parse("tel:" + number);
        Intent contactIntent = new Intent(Intent.ACTION_CALL,call);
        startActivity(Intent.createChooser(contactIntent,"Choose Calling Client"));
    }

    private void semdMail()
    {
        Uri uri = Uri.parse("mailto:satyamtiwari442@gmail.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(Intent.createChooser(intent,"Choose Mailing Client"));
    }
}