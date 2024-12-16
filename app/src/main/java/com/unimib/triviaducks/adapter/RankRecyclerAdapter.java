package com.unimib.triviaducks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.Rank;

import java.util.List;

public class RankRecyclerAdapter extends RecyclerView.Adapter<RankRecyclerAdapter.ViewHolder> {
    private List<Rank> rankList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView positionText;
        private final ImageView profileImage;
        private final TextView nameText;
        private final TextView scoreText;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.positionText = view.findViewById(R.id.rank_position);
            this.profileImage = view.findViewById(R.id.rank_image);
            this.nameText = view.findViewById(R.id.rank_name);
            this.scoreText = view.findViewById(R.id.rank_score);
        }

        public TextView getPositionText() {
            return positionText;
        }

        public ImageView getProfileImage() {
            return profileImage;
        }

        public TextView getNameText() {
            return nameText;
        }

        public TextView getScoreText() {
            return scoreText;
        }
    }

    public RankRecyclerAdapter(List<Rank> rankList) {
        this.rankList = rankList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rank_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Rank currentRankItem = rankList.get(position);

        viewHolder.getPositionText().setText(String.valueOf(currentRankItem.getPosition()));
        viewHolder.getProfileImage().setImageResource(currentRankItem.getProfileImage());
        viewHolder.getNameText().setText(currentRankItem.getName());
        viewHolder.getScoreText().setText(String.valueOf(currentRankItem.getScore()));
    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }
}
