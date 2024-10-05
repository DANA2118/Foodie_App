package com.example.foodieapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.viewholder> {
    ArrayList<Helperclass> Items;
    Context context;

    public RecipeAdapter(ArrayList<Helperclass> items) {
        this.Items = items;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_recipies, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Helperclass recipe = Items.get(position);

        holder.titleTxt.setText(recipe.getRecipename());

        Glide.with(context)
                .load(recipe.getDataImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, detail.class);
            intent.putExtra("Name", recipe.getRecipename());
            intent.putExtra("Kcal", recipe.getKcal());
            intent.putExtra("Instructions", recipe.getInstructions());
            intent.putExtra("Ingredients", recipe.getIngrediands());
            intent.putExtra("Video", recipe.getDataVideo());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        ImageView imageView;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.textView7);
            imageView = itemView.findViewById(R.id.imageView11);
        }
    }
}
