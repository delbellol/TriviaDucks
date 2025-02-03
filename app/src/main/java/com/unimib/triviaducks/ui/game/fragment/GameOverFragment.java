package com.unimib.triviaducks.ui.game.fragment;

import static com.unimib.triviaducks.util.Constants.CORRECT_ANSWER;
import static com.unimib.triviaducks.util.Constants.END;
import static com.unimib.triviaducks.util.Constants.REASON;
import static com.unimib.triviaducks.util.Constants.SCORE;
import static com.unimib.triviaducks.util.Constants.TRIVIA_CATEGORY_PARAMETER;
import static com.unimib.triviaducks.util.Constants.WRONG_ANSWER;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
        //NON ELIMINARE: Serve a impedire che la gente schiacci indietro
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        reason = getArguments().getString(REASON, WRONG_ANSWER);
        score = getArguments().getInt(SCORE,0);
        correctAnswer = getArguments().getString(CORRECT_ANSWER,"");
        end = getArguments().getBoolean(END,false);

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
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            dismiss(); //chiude la finestra di dialogo
        });

        dialog_title = view.findViewById(R.id.dialog_title);
        dialog_title.setText(reason);

        scoreView = view.findViewById(R.id.score);
        scoreView.setText("Score: "+score);

        TextView correctAnswerView = view.findViewById(R.id.correct_answer);
        TextView dialogQuestion = view.findViewById(R.id.dialog_question);
        ImageView image = view.findViewById(R.id.image);
        if (end) {
            dialogQuestion.setVisibility(View.GONE);
            correctAnswerView.setVisibility(View.GONE);
            image.setImageResource(R.drawable.you_won);
        }
        else if (correctAnswer != null && !correctAnswer.isEmpty()) {
            // Mostra la risposta corretta, se disponibile
            correctAnswerView.setText(correctAnswer);
            correctAnswerView.setVisibility(View.VISIBLE); // Rendi visibile la TextView
        }
        else {
            Log.d(TAG,"CorrectAnswer is null or empty");
        }

        return builder.create();
    }
}