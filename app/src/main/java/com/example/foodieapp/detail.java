package com.example.foodieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.media.MediaPlayer;
import android.widget.MediaController;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class detail extends AppCompatActivity {

    TextView ReceipeTitle, Kcal, Instructions, Ingredients;
    RatingBar recipeRatingBar;
    DatabaseReference ratingReference;
    ImageView deleteDataImage, editDataImage, shareDataImage, backIcon;
    VideoView video;
    String key = "";
    String videoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ReceipeTitle = findViewById(R.id.receipeName);
        deleteDataImage = findViewById(R.id.deleteData);
        shareDataImage = findViewById(R.id.shareData);
        editDataImage = findViewById(R.id.editData);
        backIcon = findViewById(R.id.back);
        video = findViewById(R.id.detailVideo);
        Kcal = findViewById(R.id.Kcal);
        Instructions = findViewById(R.id.Instructions);
        Ingredients = findViewById(R.id.Ingredients);
        recipeRatingBar = findViewById(R.id.recipeRatingBar);

        Bundle bundle = getIntent().getExtras();
        ratingReference = FirebaseDatabase.getInstance().getReference("Ratings");

        if (bundle != null) {
            ReceipeTitle.setText(bundle.getString("Name"));
            Kcal.setText(bundle.getString("Kcal"));
            Instructions.setText(bundle.getString("Instructions"));
            Ingredients.setText(bundle.getString("Ingredients"));
            key = bundle.getString("Email");
            videoUrl = bundle.getString("Video");

            initializeVideoPlayer(videoUrl);
        }

        backIcon.setOnClickListener(view -> onBackPressed());

        shareDataImage.setOnClickListener(view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            String shareBody = "Check out this recipe!\n\n" +
                    "Recipe Name: " + ReceipeTitle.getText().toString() + "\n" +
                    "Kcal: " + Kcal.getText().toString() + "\n" +
                    "Instructions: " + Instructions.getText().toString() + "\n" +
                    "Ingredients: " + Ingredients.getText().toString();

            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "share via"));
        });

        deleteDataImage.setOnClickListener(view -> {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageReference = storage.getReferenceFromUrl(videoUrl);
            storageReference.delete().addOnSuccessListener(unused -> {
                reference.child(key).removeValue();
                Toast.makeText(detail.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }).addOnFailureListener(e -> Toast.makeText(detail.this, "Error Occurred!", Toast.LENGTH_SHORT).show());
        });
    }

    private void initializeVideoPlayer(String videoUrl) {
        Uri videoUri = Uri.parse(videoUrl);
        video.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video);
        video.setMediaController(mediaController);

        video.setOnPreparedListener(mediaPlayer -> video.start());

        video.setOnCompletionListener(mediaPlayer -> Toast.makeText(detail.this, "Video Finished", Toast.LENGTH_SHORT).show());
    }
}
