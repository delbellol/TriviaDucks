package com.unimib.triviaducks.ui.home.fragment;

import static com.unimib.triviaducks.util.Constants.PARAMETER_CAN_PLAY;
import static com.unimib.triviaducks.util.Constants.LIST_DIFFICULTY;
import static com.unimib.triviaducks.util.Constants.DIFFICULTY_RANDOM;
import static com.unimib.triviaducks.util.Constants.TRIVIA_PARAMETER_AMOUNT;
import static com.unimib.triviaducks.util.Constants.TRIVIA_PARAMETER_CATEGORY;
import static com.unimib.triviaducks.util.Constants.TRIVIA_PARAMETER_DIFFICULTY;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.repository.user.IUserRepository;
import com.unimib.triviaducks.adapter.DifficultyAdapter;
import com.unimib.triviaducks.ui.connection.ConnectionErrorActivity;
import com.unimib.triviaducks.ui.game.QuestionActivity;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModel;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.triviaducks.util.NetworkUtil;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

public class GameModeDialog extends DialogFragment {
    private static final String TAG = GameModeDialog.class.getSimpleName();
    private TextView questionPicker;
    private Button plus_button;
    private Button minus_button;
    private ViewPager2 viewPager2;
    private String selectedDifficulty = DIFFICULTY_RANDOM;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private UserViewModel userViewModel;
    private int selectedCategory;

    public GameModeDialog() {}

    public static GameModeDialog newInstance() {
        GameModeDialog fragment = new GameModeDialog();
        Bundle args = new Bundle();
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
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

        View view = getLayoutInflater().inflate(R.layout.dialog_game_mode, null);
        builder.setView(view);
        if(getArguments() != null) {
            selectedCategory = getArguments().getInt(TRIVIA_PARAMETER_CATEGORY, 0);
        }
        else {
            selectedCategory = 0;
        }
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
                selectedDifficulty = LIST_DIFFICULTY.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        questionPicker = view.findViewById(R.id.questionPicker);

        Button play = view.findViewById(R.id.play);
        play.setOnClickListener(v -> {
            if (!NetworkUtil.isInternetAvailable(getContext())) {
                Intent intent = new Intent(getActivity(), ConnectionErrorActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtra(TRIVIA_PARAMETER_CATEGORY, selectedCategory);
                intent.putExtra(TRIVIA_PARAMETER_AMOUNT, Integer.parseInt(questionPicker.getText().toString()));
                intent.putExtra(TRIVIA_PARAMETER_DIFFICULTY, selectedDifficulty);
                intent.putExtra(PARAMETER_CAN_PLAY, true);
                startActivity(intent);
                increaseCategoryGameCounter(selectedCategory);
                dismiss();
            }
        });
        plus_button = view.findViewById(R.id.plus_button);
        plus_button.setOnClickListener(v -> {
            int value = Integer.parseInt(questionPicker.getText().toString());
            if (value < 50) {
                value += 5;
                questionPicker.setText(String.valueOf(value));
            }
        });

        minus_button = view.findViewById(R.id.minus_button);
        minus_button.setOnClickListener(v -> {
            int value = Integer.parseInt(questionPicker.getText().toString());
            if (value > 5) {
                value -= 5;
                questionPicker.setText(String.valueOf(value));
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

        // Carica il nome associato alla categoria su Firebase
        userViewModel.updateCategoryCounter(
                selectedCategoryString,
                userViewModel.getLoggedUser().getIdToken()
        );
    }
}