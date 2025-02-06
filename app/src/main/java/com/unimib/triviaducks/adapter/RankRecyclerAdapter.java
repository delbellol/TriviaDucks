package com.unimib.triviaducks.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.Rank;
import com.unimib.triviaducks.repository.question.QuestionRepository;

import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RankRecyclerAdapter extends RecyclerView.Adapter<RankRecyclerAdapter.ViewHolder> {
    private static final String TAG = RankRecyclerAdapter.class.getSimpleName();

    private List<Rank> rankList;
    private Context context;

    public RankRecyclerAdapter(Context context, Set<String> leaderboardSet) {
        this.context = context;
        this.rankList = convertAndSortLeaderboard(leaderboardSet);
    }

    // Classe ViewHolder
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

    // Costruttore che accetta un Set<String>
    public RankRecyclerAdapter(Set<String> leaderboardSet) {
        this.rankList = convertAndSortLeaderboard(leaderboardSet);
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

        viewHolder.getPositionText().setText(String.valueOf(position + 1));

        viewHolder.getProfileImage().setImageResource(
                context.getResources().getIdentifier(
                        currentRankItem.getProfileImage(),
                        "drawable",
                        context.getPackageName()
                )
        );

        viewHolder.getNameText().setText(currentRankItem.getName());

        viewHolder.getScoreText().setText(String.valueOf(currentRankItem.getScore()));
    }

    @Override
    public int getItemCount() {
        if (rankList != null)
            return rankList.size();
        else
            return 0;
    }

    // Metodo per convertire e ordinare il Set<String>
    private List<Rank> convertAndSortLeaderboard(Set<String> leaderboardSet) {
        if (leaderboardSet != null && !leaderboardSet.isEmpty()) {
            Log.d(TAG, "ok");
            return leaderboardSet.stream()
                    .map(item -> {
                        // Divide la stringa "bestScore;username;image"
                        if (item != null) {
                            Log.d(TAG, item);
                            String[] parts = item.split(";");
                            int bestScore = 0;
                            if (!Objects.equals(parts[0], "null"))
                                bestScore = Integer.parseInt(parts[0]);
                            String username = parts[1];
                            String image = parts[2];

                            // Crea un oggetto Rank
                            return new Rank(image, username, bestScore);
                        } else {
                            Log.d(TAG, "ERRORE: item == null");
                            return null;
                        }
                    })
                    .sorted((rank1, rank2) -> Integer.compare(rank2.getScore(), rank1.getScore())) // Ordina per bestScore decrescente
                    .collect(Collectors.toList());
        } else {
            if (leaderboardSet == null)
                Log.d(TAG, "ERRORE: leaderboardSet == null");
            else if (leaderboardSet.isEmpty())
                Log.d(TAG, "ERRORE: leaderboardSet.isEmpty()");
            return null;
        }

    }
}