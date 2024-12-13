package com.unimib.triviaducks.ui.game.fragment;

import static com.unimib.triviaducks.util.Constants.SHARED_PREFERENCES_FILENAME;
import static com.unimib.triviaducks.util.Constants.SHARED_PREFERENCES_LAST_UPDATE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.repository.IQuestionRepository;
import com.unimib.triviaducks.repository.QuestionAPIRepository;
import com.unimib.triviaducks.repository.QuestionMockRepository;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.ResponseCallback;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameFragment extends Fragment implements ResponseCallback {

    public static final String TAG = GameFragment.class.getName();

    private static final int viewedElements = 5;
    private int counter = 0;

    private List<Question> questionList;
    private IQuestionRepository questionRepository;
    private SharedPreferencesUtils sharedPreferencesUtils;

    private ImageButton closeImageButton;

    private TextView questionTextView, counterTextView;
    private Button answer1Button, answer2Button, answer3Button, answer4Button;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (requireActivity().getResources().getBoolean(R.bool.debug_mode)) {
            Log.e(TAG, "MOCK");
            questionRepository = new QuestionMockRepository(requireActivity().getApplication(), this);
        } else {
            Log.e(TAG, "API");
            questionRepository = new QuestionAPIRepository(requireActivity().getApplication(), this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game, container, false);

        counterTextView = view.findViewById(R.id.counter);

        questionTextView = view.findViewById(R.id.question);

        answer1Button = view.findViewById(R.id.answer1);
        answer2Button = view.findViewById(R.id.answer2);
        answer3Button = view.findViewById(R.id.answer3);
        answer4Button = view.findViewById(R.id.answer4);

        questionList = new ArrayList<>();
        for (int i = 0; i < viewedElements; i++) {
            questionList.add(Question.getSampleQuestion());
        }

        // Display the first question
        loadNewQuestion();

        String lastUpdate = "0";

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());
        if (sharedPreferencesUtils.readStringData(
                Constants.SHARED_PREFERENCES_FILENAME, Constants.SHARED_PREFERENCES_LAST_UPDATE) != null) {
            lastUpdate = sharedPreferencesUtils.readStringData(
                    Constants.SHARED_PREFERENCES_FILENAME, Constants.SHARED_PREFERENCES_LAST_UPDATE);
        }

        // Fetch questions from repository
        questionRepository.fetchQuestion(Constants.TRIVIA_AMOUNT_VALUE, Constants.TRIVIA_TYPE_VALUE, Long.parseLong(lastUpdate));

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        closeImageButton = view.findViewById(R.id.close_game);
        closeImageButton.setOnClickListener(v -> {
                    GameQuitFragment gameQuitDialog = new GameQuitFragment();
                    gameQuitDialog.show(getParentFragmentManager(), "GameQuitFragment");
                }
        );
    }

    private void checkAnswer(String selectedAnswer, String correctAnswer) {
        if (selectedAnswer.equals(correctAnswer)) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Correct!", Snackbar.LENGTH_SHORT).show();

            loadNewQuestion();
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Incorrect!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(List<Question> questionList, long lastUpdate) {
        sharedPreferencesUtils.writeStringData(Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_LAST_UPDATE,
                String.valueOf(lastUpdate));
        this.questionList.clear();
        this.questionList.addAll(questionList);
    }

    @Override
    public void onFailure(String errorMessage) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_LONG).show();
    }

    private void loadNewQuestion() {
        if (counter >= questionList.size()) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "No more questions!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Ottieni la domanda corrente
        Question currentResult = questionList.get(counter);

        // Prepara le risposte
        List<String> answers = new ArrayList<>();
        answers.add(currentResult.getCorrectAnswer());
        answers.addAll(currentResult.getIncorrectAnswers());
        Collections.shuffle(answers); // Mischia le risposte

        // Aggiorna il testo della domanda e delle risposte
        counterTextView.setText(" "+(counter+1));

        questionTextView.setText(currentResult.getQuestion());

        answer1Button.setText(answers.get(0));
        answer2Button.setText(answers.get(1));
        answer3Button.setText(answers.get(2));
        answer4Button.setText(answers.get(3));

        // Assegna i listener ai pulsanti
        answer1Button.setOnClickListener(v -> checkAnswer(answers.get(0), currentResult.getCorrectAnswer()));
        answer2Button.setOnClickListener(v -> checkAnswer(answers.get(1), currentResult.getCorrectAnswer()));
        answer3Button.setOnClickListener(v -> checkAnswer(answers.get(2), currentResult.getCorrectAnswer()));
        answer4Button.setOnClickListener(v -> checkAnswer(answers.get(3), currentResult.getCorrectAnswer()));

        Log.d("GameFragment", "Question: " + currentResult.getQuestion());
        Log.d("GameFragment", "Correct Answer: " + currentResult.getCorrectAnswer());

        // Incrementa il contatore per la prossima domanda
        counter++;
    }
}