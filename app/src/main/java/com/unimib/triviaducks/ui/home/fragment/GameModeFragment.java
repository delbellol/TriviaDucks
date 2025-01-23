package com.unimib.triviaducks.ui.home.fragment;

import static com.unimib.triviaducks.util.Constants.CATEGORY;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.repository.user.IUserRepository;
import com.unimib.triviaducks.ui.game.QuestionActivity;
import com.unimib.triviaducks.ui.game.fragment.GameFragment;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModel;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

public class GameModeFragment extends DialogFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private SharedPreferencesUtils sharedPreferencesUtils;
    private UserViewModel userViewModel;

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

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());

        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        userViewModel.setAuthenticationError(false);
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
        int selectedCategory = getArguments().getInt(CATEGORY, 0);
        Log.d(TAG, String.valueOf(selectedCategory));

        Button play = view.findViewById(R.id.play);
        play.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuestionActivity.class);
            intent.putExtra(CATEGORY, selectedCategory);
            startActivity(intent);
            increaseCategoryGameCounter(selectedCategory);
            dismiss();
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