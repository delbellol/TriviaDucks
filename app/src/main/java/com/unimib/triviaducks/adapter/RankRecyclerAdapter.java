package com.unimib.triviaducks.adapter;

import static com.unimib.triviaducks.util.Constants.DRAWABLE;
import static com.unimib.triviaducks.util.Constants.ERROR;
import static com.unimib.triviaducks.util.Constants.ERROR_ITEM_IS_NULL;
import static com.unimib.triviaducks.util.Constants.ERROR_LEADERBOARD_SET_IS_EMPTY;
import static com.unimib.triviaducks.util.Constants.ERROR_LEADERBOARD_SET_IS_NULL;
import static com.unimib.triviaducks.util.Constants.NULL;
import static com.unimib.triviaducks.util.Constants.SPLIT_CHARACTER;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rank, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Rank currentRankItem = rankList.get(position);

        viewHolder.getPositionText().setText(String.valueOf(position + 1));

        viewHolder.getProfileImage().setImageResource(
                context.getResources().getIdentifier(
                        currentRankItem.getProfileImage(),
                        DRAWABLE,
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
            try {
                return leaderboardSet.stream()
                        .map(item -> {
                            // Divide la stringa "bestScore;username;image"
                            if (item != null) {
                                Log.d(TAG, item);
                                String[] parts = item.split(SPLIT_CHARACTER);
                                int bestScore = 0;
                                if (!Objects.equals(parts[0], NULL))
                                    bestScore = Integer.parseInt(parts[0]);
                                String username = parts[1];
                                String image = parts[2];

                                // Crea un oggetto Rank
                                return new Rank(image, username, bestScore);
                            } else {
                                Log.e(TAG, ERROR_ITEM_IS_NULL);
                                return null;
                            }
                        })
                        .sorted((rank1, rank2) -> Integer.compare(rank2.getScore(), rank1.getScore())) // Ordina per bestScore decrescente
                        .collect(Collectors.toList());
            }catch (Exception ex) {
                if (ex.getMessage() != null) Log.e(TAG, ERROR + ex.getMessage());
                else ex.printStackTrace();
                return null;
            }
        } else {
            if (leaderboardSet == null)
                Log.e(TAG, ERROR_LEADERBOARD_SET_IS_NULL);
            else if (leaderboardSet.isEmpty())
                Log.e(TAG, ERROR_LEADERBOARD_SET_IS_EMPTY);
            return null;
        }

    }
}