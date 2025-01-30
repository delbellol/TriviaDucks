package com.unimib.triviaducks.ui.home.fragment;

import static com.unimib.triviaducks.util.Constants.TRIVIA_AMOUNT_PARAMETER;
import static com.unimib.triviaducks.util.Constants.TRIVIA_CATEGORY_PARAMETER;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.game.QuestionActivity;
import com.unimib.triviaducks.ui.game.fragment.GameFragment;

public class GameModeFragment extends DialogFragment {
    private static final String TAG = GameModeFragment.class.getSimpleName();
    private TextView questionPicker;
    private Button plus_button;
    private Button minus_button;

    public GameModeFragment() {
        // Required empty public constructor
    }

    public static GameModeFragment newInstance(int selectedCategory) {
        GameModeFragment fragment = new GameModeFragment();
        Bundle args = new Bundle();
        args.putInt(TRIVIA_CATEGORY_PARAMETER, selectedCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @NonNull
    @SuppressLint("MissingInflatedId")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getLayoutInflater().inflate(R.layout.fragment_game_mode, null);
        builder.setView(view);

        assert getArguments() != null;
        int selectedCategory = getArguments().getInt(TRIVIA_CATEGORY_PARAMETER, 0);
        Log.d(TAG, String.valueOf(selectedCategory));

        questionPicker = view.findViewById(R.id.questionPicker);

        Button play = view.findViewById(R.id.play);
        play.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuestionActivity.class);
            intent.putExtra(TRIVIA_CATEGORY_PARAMETER, selectedCategory);
            intent.putExtra(TRIVIA_AMOUNT_PARAMETER, Integer.parseInt(questionPicker.getText().toString()));
            startActivity(intent);
            dismiss();
        });

        plus_button = view.findViewById(R.id.plus_button);
        plus_button.setOnClickListener(v -> {
            int value = Integer.parseInt(questionPicker.getText().toString());
            Log.d(TAG,"Old value: "+value);
            if (value < 50) {
                value += 5;
                questionPicker.setText(""+value);
                Log.d(TAG,"New value: "+value);
            }
        });

        minus_button = view.findViewById(R.id.minus_button);
        minus_button.setOnClickListener(v -> {
            int value = Integer.parseInt(questionPicker.getText().toString());
            Log.d(TAG,"Old value: "+value);
            if (value > 5) {
                value -= 5;
                questionPicker.setText(""+value);
                Log.d(TAG,"New value: "+value);
            }
        });

        Button close = view.findViewById(R.id.close);
        close.setOnClickListener(v ->
                dismiss()
        );

        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}