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
import androidx.viewpager2.widget.ViewPager2;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.adapter.DifficultyAdapter;
import com.unimib.triviaducks.ui.game.QuestionActivity;
import com.unimib.triviaducks.ui.game.fragment.GameFragment;
import com.unimib.triviaducks.util.Constants;

import java.util.Arrays;
import java.util.List;

public class GameModeFragment extends DialogFragment {
    private static final String TAG = GameModeFragment.class.getSimpleName();
    private ViewPager2 viewPager2;
    private String selectedDifficulty = "random";

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

        List<String> difficultyList = Arrays.asList(
                "random",
                "easy",
                "medium",
                "hard"
        );

        viewPager2 = view.findViewById(R.id.viewpager);
        DifficultyAdapter difficultyAdapter = new DifficultyAdapter(this.getContext());

        viewPager2.setAdapter(difficultyAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            // This method is triggered when there is any scrolling activity for the current page
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            // triggered when you select a new page
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                selectedDifficulty = difficultyList.get(position);
            }

            // triggered when there is
            // scroll state will be changed
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        Button play = view.findViewById(R.id.play);
        play.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuestionActivity.class);
            intent.putExtra(CATEGORY, selectedCategory);
            intent.putExtra(DIFFICULTY, selectedDifficulty);
            startActivity(intent);
            dismiss();
        });
        Button exit = view.findViewById(R.id.close);
        exit.setOnClickListener(v -> {
            dismiss();
        });

        return builder.create();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}