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
        // Initialize FirebaseAuth before using it
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already signed in and redirect if necessary
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            // User is signed in, proceed to the next screen
//            Intent intent = new Intent(getApplicationContext(), Addnewrecipe.class);
//            startActivity(intent);
//            finish();  // Close the sign-in activity
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Comment out EdgeToEdge for debugging
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin); // Check if layout file is correct

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Bind UI components to variables
        loginEmail = findViewById(R.id.login_email);  // Email input field
        loginPassword = findViewById(R.id.login_password);  // Password input field
        loginButton = findViewById(R.id.signinbtn);  // Sign-In button
        signupRedirect = findViewById(R.id.signuptext);  // Sign-Up redirect link

        // Sign-In button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                // Validate email input
                if (TextUtils.isEmpty(email)) {
                    loginEmail.setError("Email is required");
                    loginEmail.requestFocus();
                    return;
                }

                // Validate password input
                if (TextUtils.isEmpty(password)) {
                    loginPassword.setError("Password is required");
                    loginPassword.requestFocus();
                    return;
                }

                // Attempt to sign in the user with Firebase Authentication
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign-in successful, redirect to AddRecipe activity
                                    Toast.makeText(signin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(signin.this, Addnewrecipe.class);
                                    startActivity(intent);
                                    finish();  // Close the sign-in activity
                                } else {
                                    // If sign-in fails, show an error message
                                    String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                                    Toast.makeText(signin.this, "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Redirect to sign-up page when clicking on "Sign Up" text
        signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signin.this, signup.class);
                startActivity(intent);
            }
        });
    }
}
