package com.unimib.triviaducks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
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
        /*
        oneshot.setOnClickListener(view -> {
            Intent intent = new Intent(this, QuestionActivity.class);
            startActivity(intent);
        });
        trials.setOnClickListener(view -> {
            Intent intent = new Intent(this, QuestionActivity.class);
            startActivity(intent);
        });
         */

        oneshot.setOnClickListener(v -> showPopup(v, "oneshot"));
        trials.setOnClickListener(v -> showPopup(v, "trials"));
    }

    private void showPopup(View anchorView, String mode) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.showAtLocation(anchorView.getRootView(), Gravity.CENTER, 0, 0);

        ImageButton closeButton = popupView.findViewById(R.id.close_popup);
        closeButton.setOnClickListener(view -> popupWindow.dismiss());

        TextView title = popupView.findViewById(R.id.popup_title);
        TextView description = popupView.findViewById(R.id.popup_description);
        if ("oneshot".equals(mode)) {
            title.setText(getString(R.string.one_shot));
            description.setText(getString(R.string.one_shot_description));
        } else if ("trials".equals(mode)) {
            title.setText(getString(R.string.trials));
            description.setText(getString(R.string.trials_description));
        }

        Button play = popupView.findViewById(R.id.play);
        play.setOnClickListener(view -> {
            Intent intent = new Intent(this, QuestionActivity.class);
            intent.putExtra("mode", mode);
            startActivity(intent);
        });
    }

}