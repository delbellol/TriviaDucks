package com.unimib.triviaducks.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unimib.triviaducks.R;

// classe per la recycler view del viewpager in GameModeDialog
public class DifficultyAdapter extends RecyclerView.Adapter<DifficultyAdapter.ViewHolder> {
    private int[] images = {R.drawable.random, R.drawable.easy, R.drawable.medium,
            R.drawable.hard,};
    private Context ctx;

    public DifficultyAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_difficulty_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.images.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.difficulty_images);
        }
    }
}
