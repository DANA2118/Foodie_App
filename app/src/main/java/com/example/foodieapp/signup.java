package com.example.foodieapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signup extends AppCompatActivity {

    EditText signupusername, signupemail, signuppassword;
    TextView signinredirect;
    Button signupbtn;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        signupusername = findViewById(R.id.username);
        signupemail = findViewById(R.id.email);
        signuppassword = findViewById(R.id.password);
        signupbtn = findViewById(R.id.signupbtn);
        signinredirect = findViewById(R.id.signintext);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = signupusername.getText().toString().trim();
                String email = signupemail.getText().toString().trim();
                String password = signuppassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    signupemail.setError("Email is required");
                    signupemail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    signupemail.setError("Please enter a valid email");
                    signupemail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    signupusername.setError("Username is required");
                    signupusername.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    signuppassword.setError("Password is required");
                    signuppassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    signuppassword.setError("Password should be at least 6 characters long");
                    signuppassword.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    if (firebaseUser != null) {
                                        String userId = firebaseUser.getUid();
                                        database = FirebaseDatabase.getInstance();
                                        reference = database.getReference("users");

                                        HashMap<String, String> userMap = new HashMap<>();
                                        userMap.put("username", username);
                                        userMap.put("email", email);
                                        userMap.put("userId", userId);

                                        reference.child(userId).setValue(userMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(signup.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(signup.this, signin.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(signup.this, "Database error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    Toast.makeText(signup.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signinredirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, signin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
