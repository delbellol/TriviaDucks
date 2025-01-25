package com.unimib.triviaducks.ui.home.fragment;

import static com.unimib.triviaducks.util.Constants.CATEGORY;
import static com.unimib.triviaducks.util.Constants.DIFFICULTY;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.game.QuestionActivity;
import com.unimib.triviaducks.ui.game.fragment.GameFragment;
import com.unimib.triviaducks.util.Constants;

public class GameModeFragment extends DialogFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    public GameModeFragment() {
        // Required empty public constructor
    }

    public static GameModeFragment newInstance(int selectedCategory) {
        GameModeFragment fragment = new GameModeFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY, selectedCategory);
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
        int selectedCategory = getArguments().getInt(CATEGORY, 0);
        Log.d(TAG, String.valueOf(selectedCategory));

        Button random = view.findViewById(R.id.random);
        random.setOnClickListener(v -> {
            buttonClick(selectedCategory,"random");
        });

        Button easy = view.findViewById(R.id.easy);
        easy.setOnClickListener(v -> {
            buttonClick(selectedCategory,"easy");
        });

        Button medium = view.findViewById(R.id.medium);
        medium.setOnClickListener(v -> {
            buttonClick(selectedCategory,"medium");
        });

        Button hard = view.findViewById(R.id.hard);
        hard.setOnClickListener(v -> {
            buttonClick(selectedCategory,"hard");
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

    private void buttonClick(int selectedCategory, String difficulty) {
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        intent.putExtra(CATEGORY, selectedCategory);
        intent.putExtra("DIFFICULTY", difficulty);
        startActivity(intent);
        dismiss();
    }
}