package com.unimib.triviaducks.ui.home.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.game.QuestionActivity;
import com.unimib.triviaducks.ui.home.AccountActivity;
import com.unimib.triviaducks.ui.home.RankingActivity;
import com.unimib.triviaducks.ui.home.SettingsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button oneshot = view.findViewById(R.id.oneShot);
        Button trials = view.findViewById(R.id.trials);
        ImageButton account = view.findViewById(R.id.account);
        ImageButton settings = view.findViewById(R.id.settings);
        ImageButton leaderboard = view.findViewById(R.id.ranking);

        account.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_accountInformationFragment);
        });
        settings.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_settingsFragment);
        });
        leaderboard.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_leaderboardFragment);
        });

        oneshot.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_gameModeFragment);
        });
        //trials.setOnClickListener(v -> showDialog("trials"));
    }

/*
    private void showDialog(String mode) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_mode);

        TextView title = dialog.findViewById(R.id.dialog_title);
        TextView description = dialog.findViewById(R.id.dialog_description);
        if ("oneshot".equals(mode)) {
            title.setText(getString(R.string.one_shot));
            description.setText(getString(R.string.one_shot_description));
        } else if ("trials".equals(mode)) {
            title.setText(getString(R.string.trials));
            description.setText(getString(R.string.trials_description));
        }

        ImageButton closeButton = dialog.findViewById(R.id.close_dialog);
        closeButton.setOnClickListener(view -> dialog.dismiss());

        Button play = dialog.findViewById(R.id.play);
        play.setOnClickListener(view -> {
            Intent intent = new Intent(this, QuestionActivity.class);
            intent.putExtra("mode", mode);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
    }
*/
}