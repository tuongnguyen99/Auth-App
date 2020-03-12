package com.example.authapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    Button btnSend;
    EditText txtCountryCode, txtPhoneNumber;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryCode = '+' + txtCountryCode.getText().toString().trim();
                String phoneNumber = txtPhoneNumber.getText().toString().trim();
                if (countryCode.isEmpty() || phoneNumber.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your country code and phone number to verify!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String completePhone = countryCode + phoneNumber;
                PhoneAuthProvider.getInstance().verifyPhoneNumber(completePhone, 60, TimeUnit.SECONDS, LoginActivity.this, mCallbacks);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(LoginActivity.this, "Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(LoginActivity.this, "OnCodeSent", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                intent.putExtra("verificationId", s);
                startActivity(intent);
            }
        };
    }

    private void addControls() {
        btnSend = findViewById(R.id.btnSend);
        txtCountryCode = findViewById(R.id.txtCountryCode);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
