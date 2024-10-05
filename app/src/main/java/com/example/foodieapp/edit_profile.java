package com.example.foodieapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodieapp.R;

public class edit_profile extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private Button saveProfileButton;
    private ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firstNameEditText = findViewById(R.id.first_name2);
        emailEditText = findViewById(R.id.email2);
        saveProfileButton = findViewById(R.id.btn_edit_profile2);
        backArrow = findViewById(R.id.back_arrow);

        backArrow.setOnClickListener(view -> {
            finish();
        });

        saveProfileButton.setOnClickListener(view -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                Toast.makeText(edit_profile.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(edit_profile.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}