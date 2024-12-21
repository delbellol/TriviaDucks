package com.unimib.triviaducks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.unimib.triviaducks.R;

import java.util.List;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder> {

    private final List<Integer> lottieFiles;
    private final List<String> categoryDescriptions; // Lista per i testi
    public CategoriesRecyclerAdapter(List<Integer> lottieFiles, List<String> categoryDescriptions) {
        this.lottieFiles = lottieFiles;
        this.categoryDescriptions = categoryDescriptions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LottieAnimationView lottieAnimationView;
        private final TextView descriptionTextView;



        public ViewHolder(@NonNull View view) {
            super(view);
            this.lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
            this.descriptionTextView = view.findViewById(R.id.categoryDescription);
        }

        public LottieAnimationView getLottieAnimationView() {
            return lottieAnimationView;
        }

        public TextView getDescriptionTextView() {
            return descriptionTextView;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lottie_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getLottieAnimationView().setAnimation(lottieFiles.get(position));
        viewHolder.getLottieAnimationView().playAnimation();
        // Imposta il testo descrittivo
        viewHolder.getDescriptionTextView().setText(categoryDescriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return lottieFiles.size();
    }
}
