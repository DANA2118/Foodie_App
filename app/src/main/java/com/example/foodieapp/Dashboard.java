package com.example.foodieapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    RecyclerView recipeRecyclerView;
    RecipeAdapter recipeAdapter;
    ArrayList<Helperclass> recipeList = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference recipeRef;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        recipeRecyclerView = findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeAdapter = new RecipeAdapter(recipeList);
        recipeRecyclerView.setAdapter(recipeAdapter);

        if (currentUser != null) {
            fetchRecipesFromFirebase();
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }

        ImageButton addRecipeButton = findViewById(R.id.imageButton5);
        addRecipeButton.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Addnewrecipe.class);
            startActivity(intent);
        });
    }

    private void fetchRecipesFromFirebase() {
        recipeRef = firebaseDatabase.getReference("recipes").child(currentUser.getUid());

        recipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the list to avoid duplication
                recipeList.clear();
                // Iterate through each recipe node in Firebase
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Helperclass recipe = dataSnapshot.getValue(Helperclass.class);
                    if (recipe != null) {
                        recipeList.add(recipe);
                    }
                }
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Dashboard", "Failed to fetch data", error.toException());
                Toast.makeText(Dashboard.this, "Failed to load recipes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
