package com.unimib.triviaducks.ui.game.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.repository.user.IUserRepository;
import com.unimib.triviaducks.ui.home.HomeActivity;
import com.unimib.triviaducks.ui.home.fragment.HomeFragment;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModel;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

public class GameOverFragment extends DialogFragment {
    private static final String TAG = GameOverFragment.class.getSimpleName();

    private SharedPreferencesUtils sharedPreferencesUtils;
    private UserViewModel userViewModel;

    Button home;
    TextView dialog_title;
    TextView scoreView;
    String reason;
    int score;
    boolean end=false;
    String correctAnswer;

    public GameOverFragment() {
        reason = getString(R.string.wrong_answer);
    }
    public GameOverFragment(String reason, int score) {
        this.reason = reason;
        this.score = score;
    }

    public GameOverFragment(String reason, int score, String correctAnswer, boolean end) {
        this.reason = reason;
        this.score = score;
        this.correctAnswer = correctAnswer;
        this.end=end;
    }


    public static GameOverFragment newInstance() {
        return new GameOverFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());

        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        userViewModel.setAuthenticationError(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_game_over, null);
        builder.setView(view);

        int currentBestScore = sharedPreferencesUtils.readIntData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_BEST_SCORE
        );

        if (score > currentBestScore) {
            sharedPreferencesUtils.writeIntData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_BEST_SCORE,
                    score
                    );

            userViewModel.saveBestScore(
                    score,
                    userViewModel.getLoggedUser().getIdToken()
            );
        }

        dialog_title = view.findViewById(R.id.dialog_title);
        dialog_title.setText(reason);

        scoreView = view.findViewById(R.id.score);
        scoreView.setText("Score: "+score);

        home = view.findViewById(R.id.home);

        home.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.main_content);
            //Per spostarsi tra tra activity diverse a quanto pare bisogna usare questi intent maledetti
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            dismiss(); //chiude la finestra di dialogo
        });

        dialog_title = view.findViewById(R.id.dialog_title);
        dialog_title.setText(reason);

        scoreView = view.findViewById(R.id.score);
        scoreView.setText("Score: "+score);

        if (end) {
            TextView dialogQuestion = view.findViewById(R.id.dialog_question);
            dialogQuestion.setVisibility(View.GONE);
        }

        TextView correctAnswerView = view.findViewById(R.id.correct_answer);
        // Mostra la risposta corretta, se disponibile
        if (correctAnswer != null && !correctAnswer.isEmpty()) {
            correctAnswerView.setText(correctAnswer);
            correctAnswerView.setVisibility(View.VISIBLE); // Rendi visibile la TextView
        }
        return builder.create();
    }
}