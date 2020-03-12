package com.example.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpActivity extends AppCompatActivity {
    EditText txtOptCode;
    Button btnVerify;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        verificationId = getIntent().getStringExtra("verificationId");
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String optCode = txtOptCode.getText().toString().trim();
                if (optCode.isEmpty()){
                    Toast.makeText(OtpActivity.this, "Please enter OTP code to verify!", Toast.LENGTH_SHORT).show();
                }else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, optCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("CUS", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            getIn();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("ERR", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(OtpActivity.this, "The verification code entered was invalid!", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(OtpActivity.this, "An error happen when verification!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getIn(){
        Intent intent = new Intent(OtpActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void addControls() {
        btnVerify = findViewById(R.id.btnVerify);
        txtOptCode = findViewById(R.id.txtOtpCode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null){
            getIn();
        }
    }

}
