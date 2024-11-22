package com.unimib.triviaducks;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button oneshot = findViewById(R.id.oneShot);
        Button trials = findViewById(R.id.trials);
        ImageButton account = findViewById(R.id.account);
        ImageButton settings = findViewById(R.id.settings);
        ImageButton ranking = findViewById(R.id.ranking);

        account.setOnClickListener(view -> {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        });
        settings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
        ranking.setOnClickListener(view -> {
            Intent intent = new Intent(this, RankingActivity.class);
            startActivity(intent);
        });


        oneshot.setOnClickListener(v -> showDialog("oneshot"));
        trials.setOnClickListener(v -> showDialog("trials"));
    }

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

}