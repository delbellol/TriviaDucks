package com.unimib.triviaducks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.unimib.triviaducks.R;

import java.util.List;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder> {

    private final List<Integer> lottieFiles;

    public CategoriesRecyclerAdapter(List<Integer> lottieFiles) {
        this.lottieFiles = lottieFiles;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LottieAnimationView lottieAnimationView;



        public ViewHolder(@NonNull View view) {
            super(view);
            this.lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        }

        public LottieAnimationView getLottieAnimationView() {
            return lottieAnimationView;
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
    }

    @Override
    public int getItemCount() {
        return lottieFiles.size();
    }
}
