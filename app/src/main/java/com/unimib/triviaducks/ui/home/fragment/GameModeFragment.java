package com.unimib.triviaducks.ui.home.fragment;

import static com.unimib.triviaducks.util.Constants.TRIVIA_AMOUNT_PARAMETER;
import static com.unimib.triviaducks.util.Constants.TRIVIA_CATEGORY_PARAMETER;
import static com.unimib.triviaducks.util.Constants.DIFFICULTY;
import static com.unimib.triviaducks.util.Constants.TRIVIA_DIFFICULTY_PARAMETER;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.repository.user.IUserRepository;
import com.unimib.triviaducks.adapter.DifficultyAdapter;
import com.unimib.triviaducks.ui.game.QuestionActivity;
import com.unimib.triviaducks.ui.game.fragment.GameFragment;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModel;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;
import com.unimib.triviaducks.util.Constants;

import java.util.Arrays;
import java.util.List;

public class GameModeFragment extends DialogFragment {
    private static final String TAG = GameModeFragment.class.getSimpleName();
    private TextView questionPicker;
    private Button plus_button;
    private Button minus_button;
    private ViewPager2 viewPager2;
    private String selectedDifficulty = "random";
    private SharedPreferencesUtils sharedPreferencesUtils;
    private UserViewModel userViewModel;

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

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());

        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        userViewModel.setAuthenticationError(false);

        setCancelable(false);
    }

    @NonNull
    @SuppressLint("MissingInflatedId")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

        View view = getLayoutInflater().inflate(R.layout.fragment_game_mode, null);
        builder.setView(view);

        assert getArguments() != null;
        int selectedCategory = getArguments().getInt(TRIVIA_CATEGORY_PARAMETER, 0);

        List<String> difficultyList = Arrays.asList(
                "",
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

        questionPicker = view.findViewById(R.id.questionPicker);

        Button play = view.findViewById(R.id.play);
        play.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuestionActivity.class);
            intent.putExtra(TRIVIA_CATEGORY_PARAMETER, selectedCategory);
            intent.putExtra(TRIVIA_AMOUNT_PARAMETER, Integer.parseInt(questionPicker.getText().toString()));
            intent.putExtra(TRIVIA_DIFFICULTY_PARAMETER, selectedDifficulty);
            intent.putExtra("can_play", true);
            startActivity(intent);
            increaseCategoryGameCounter(selectedCategory);
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

    private void increaseCategoryGameCounter(int selectedCategory) {
        String selectedCategoryString = String.valueOf(selectedCategory);
//        sharedPreferencesUtils.writeIntData(
//                Constants.SHARED_PREFERENCES_FILENAME,
//                selectedCategoryString,
//                sharedPreferencesUtils.readIntData(
//                        Constants.SHARED_PREFERENCES_FILENAME,
//                        Constants.SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY+"/"+selectedCategoryString
//                ) + 1
//        ); // Salva il nome della risorsa

        // Carica il nome su Firebase
        userViewModel.updateCategoryCounter(
                selectedCategoryString,
                userViewModel.getLoggedUser().getIdToken()
        );
    }
}