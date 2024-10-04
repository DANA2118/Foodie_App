package com.example.foodieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Addnewrecipe extends AppCompatActivity {

    Button submitButton, chooseVideoButton;
    EditText recipeNameEditText, kcalEditText, instructionsEditText, ingredientsEditText;
    ImageView imageView12;
    Uri imageUri, videoUri;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference recipeRef;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewrecipe);

        // Initialize Firebase components
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        // Link views from XML layout
        recipeNameEditText = findViewById(R.id.edit_recipe_name);
        kcalEditText = findViewById(R.id.edit_kcal);
        instructionsEditText = findViewById(R.id.edit_instructions);
        ingredientsEditText = findViewById(R.id.edit_ingredients);
        submitButton = findViewById(R.id.submit_button);
        chooseVideoButton = findViewById(R.id.choose_video_button);
        imageView12 = findViewById(R.id.imageView12);

        // Handling image selection by tapping ImageView
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            imageUri = data.getData();
                            imageView12.setImageURI(imageUri); // Preview selected image
                        }
                    }
                });

        imageView12.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        // Handling video selection
        ActivityResultLauncher<Intent> videoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            videoUri = data.getData();
                            Toast.makeText(this, "Video selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        chooseVideoButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*");
            videoPickerLauncher.launch(intent);
        });

        // Submit button action to store data
        submitButton.setOnClickListener(v -> {
            String recipeName = recipeNameEditText.getText().toString().trim();
            String kcalValue = kcalEditText.getText().toString().trim();
            String instructions = instructionsEditText.getText().toString().trim();
            String ingredients = ingredientsEditText.getText().toString().trim();

            if (recipeName.isEmpty() || kcalValue.isEmpty() || instructions.isEmpty() || ingredients.isEmpty() || imageUri == null || videoUri == null) {
                Toast.makeText(Addnewrecipe.this, "Please fill in all fields and add both an image and a video", Toast.LENGTH_SHORT).show();
                return;
            }

            // Upload Image to Firebase Storage
            StorageReference imageRef = storageReference.child("recipes/" + currentUser.getUid() + "/images/" + System.currentTimeMillis() + ".jpg");
            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(imageDownloadUrl -> {
                    // After Image upload, upload Video
                    StorageReference videoRef = storageReference.child("recipes/" + currentUser.getUid() + "/videos/" + System.currentTimeMillis() + ".mp4");
                    videoRef.putFile(videoUri).addOnSuccessListener(taskSnapshot1 -> {
                        videoRef.getDownloadUrl().addOnSuccessListener(videoDownloadUrl -> {
                            // Store Recipe Data in Realtime Database
                            recipeRef = firebaseDatabase.getReference("recipes").child(currentUser.getUid()).push();

                            Map<String, Object> recipeData = new HashMap<>();
                            recipeData.put("recipename", recipeName);
                            recipeData.put("kcal", kcalValue);
                            recipeData.put("instructions", instructions);
                            recipeData.put("ingredients", ingredients);
                            recipeData.put("imageUri", imageDownloadUrl.toString());
                            recipeData.put("videoUri", videoDownloadUrl.toString());

                            recipeRef.setValue(recipeData).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Addnewrecipe.this, "Recipe submitted successfully", Toast.LENGTH_SHORT).show();
                                    clearForm();
                                } else {
                                    Toast.makeText(Addnewrecipe.this, "Failed to submit recipe", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    });
                });
            }).addOnFailureListener(e -> Toast.makeText(Addnewrecipe.this, "Image upload failed", Toast.LENGTH_SHORT).show());
        });
    }

    // Helper method to clear the form after submission
    private void clearForm() {
        recipeNameEditText.setText("");
        kcalEditText.setText("");
        instructionsEditText.setText("");
        ingredientsEditText.setText("");
        imageView12.setImageResource(R.drawable.ic_launcher_foreground);
        imageUri = null;
        videoUri = null;
    }
}
