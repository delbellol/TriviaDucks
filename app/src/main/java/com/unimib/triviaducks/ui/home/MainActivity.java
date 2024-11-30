package com.unimib.triviaducks.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.game.QuestionActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Dichiaro i bottoni (R.id.nome_del_bottone dichiarato nel mainfragment layout)
        //Button oneshot = findViewById(R.id.oneShot);
        //Button trials = findViewById(R.id.trials);
        //ImageButton leaderboard = findViewById(R.id.ranking);

        /*collega i bottoni del main ai vari fragment tramite action di home_nav_graph
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

        trials.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_gameModeFragment);
        });*/
    }
    private void showDialog(String mode) {
        Dialog dialog = new Dialog(this);
        //dialog.setContentView(R.layout.dialog_game_mode);

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
}