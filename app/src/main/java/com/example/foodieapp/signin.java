package com.example.foodieapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class signin extends AppCompatActivity {

    // UI Components
    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signupRedirect;

    // Firebase Authentication
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.signinbtn);
        signupRedirect = findViewById(R.id.signuptext);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    loginEmail.setError("Email is required");
                    loginEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    loginPassword.setError("Password is required");
                    loginPassword.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(signin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(signin.this, Dashboard.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                                    Toast.makeText(signin.this, "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signin.this, signup.class);
                startActivity(intent);
            }
        });
    }
}
