package com.unimib.triviaducks.ui.game.fragment;

import static com.unimib.triviaducks.util.Constants.SHARED_PREFERENCES_FILENAME;
import static com.unimib.triviaducks.util.Constants.SHARED_PREFERNECES_LAST_UPDATE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    private List<Question> questionList;
    private IQuestionRepository questionRepository;
    private SharedPreferencesUtils sharedPreferencesUtils;

    private ImageButton closeImageButton;

    private TextView questionTextView;
    private Button answer1Button, answer2Button, answer3Button, answer4Button;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if (requireActivity().getResources().getBoolean(R.bool.debug_mode)) {
            questionRepository = new QuestionMockRepository(requireActivity().getApplication(), this);
        //} else {
        //    questionRepository = new QuestionAPIRepository(requireActivity().getApplication(), this);
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game, container, false);
        /*
        // Find the views
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
        displayQuestion(questionList.get(0));

        String lastUpdate = "0";
        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());
        if (sharedPreferencesUtils.readStringData(
                Constants.SHARED_PREFERENCES_FILENAME, Constants.SHARED_PREFERNECES_LAST_UPDATE) != null) {
            lastUpdate = sharedPreferencesUtils.readStringData(
                    Constants.SHARED_PREFERENCES_FILENAME, Constants.SHARED_PREFERNECES_LAST_UPDATE);
        }

        // Fetch questions from repository
        questionRepository.fetchQuestion(Constants.TRIVIA_AMOUNT_VALUE, Constants.TRIVIA_TYPE_VALUE, Long.parseLong(lastUpdate));
*/
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        closeImageButton = view.findViewById(R.id.close_game);
        closeImageButton.setOnClickListener(v -> {
                    GameQuitFragment gameQuitDialog = new GameQuitFragment();
                    gameQuitDialog.show(getParentFragmentManager(), "GameQuitFragment");
                }
                //Navigation.findNavController(v).navigate(R.id.action_gameQuestionFragment_to_gameQuitFragment)
        );
    }

    private void displayQuestion(Question question) {
        questionTextView.setText(question.getQuestion());
        List<String> answers = new ArrayList<>(question.getIncorrectAnswers());
        answers.add(question.getCorrectAnswer());
        Collections.shuffle(answers);  // Shuffle answers to randomize the order

        // Set answers to buttons
        answer1Button.setText(answers.get(0));
        answer2Button.setText(answers.get(1));
        answer3Button.setText(answers.get(2));
        answer4Button.setText(answers.get(3));

        // Add click listeners to check the selected answer
        answer1Button.setOnClickListener(v -> checkAnswer(answers.get(0), question.getCorrectAnswer()));
        answer2Button.setOnClickListener(v -> checkAnswer(answers.get(1), question.getCorrectAnswer()));
        answer3Button.setOnClickListener(v -> checkAnswer(answers.get(2), question.getCorrectAnswer()));
        answer4Button.setOnClickListener(v -> checkAnswer(answers.get(3), question.getCorrectAnswer()));
    }

    private void checkAnswer(String selectedAnswer, String correctAnswer) {
        if (selectedAnswer.equals(correctAnswer)) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Correct!", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Incorrect!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(List<Question> questionList, long lastUpdate) {
        sharedPreferencesUtils.writeStringData(Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERNECES_LAST_UPDATE,
                String.valueOf(lastUpdate));
        this.questionList.clear();
        this.questionList.addAll(questionList);

        // Display the first question after fetching
        displayQuestion(questionList.get(0));
    }

    @Override
    public void onFailure(String errorMessage) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_LONG).show();
    }


    /*
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameViewModel = new GameController(); //Aggiunto getContext perché serve per JSONParserUtils


        questionTextView = view.findViewById(R.id.question);
        counterTextView = view.findViewById(R.id.counter);
        countdownTextView = view.findViewById(R.id.countdown);

        answer1Button = view.findViewById(R.id.answer1);
        answer2Button = view.findViewById(R.id.answer2);
        answer3Button = view.findViewById(R.id.answer3);
        answer4Button = view.findViewById(R.id.answer4);

        //chiudere la partita quando premi bottone
        ImageButton closeImageButton = view.findViewById(R.id.close_game);
        closeImageButton.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_gameQuestionFragment_to_gameQuitFragment)
        );

        //Richiamo metodi per ottenere messaggi da visualizzare
        gameViewModel.questionCounter.observe(getViewLifecycleOwner(), counter -> {
            counterTextView.setText(counter);
        });

        gameViewModel.currentQuestion.observe(getViewLifecycleOwner(), question -> {
            //implementando jsoup decodifico i caratteri speciali, come ad esempio ", nel testo
            //della domanda
            questionTextView.setText(Jsoup.parse(question).text());
        });

        gameViewModel.currentAnswers.observe(getViewLifecycleOwner(), answers -> {
            List<Button> buttons = Arrays.asList(answer1Button, answer2Button, answer3Button, answer4Button);
            Collections.shuffle(buttons);

            for (int i = 0; i < buttons.size(); i++) {
                Button button = buttons.get(i);
                String answer = answers.get(i);

                //implementando jsoup decodifico i caratteri speciali nel testo delle risposte
                button.setText(Jsoup.parse(answer).text());
                button.setOnClickListener(v -> gameViewModel.isCorrectAnswer(answer));
            }
        });

        gameViewModel.secondsRemaining.observe(getViewLifecycleOwner(), seconds -> {
            countdownTextView.setText(String.valueOf(seconds));
        });

        gameViewModel.gameOver.observe(getViewLifecycleOwner(), isGameOver -> {
            if (isGameOver) {
                // Mostra dialog di game over
                //lo mostra come fragmennt
                //Navigation.findNavController(getActivity(), R.id.main_content).navigate(R.id.action_gameQuestionFragment_to_gameOverFragment);
                //lo mostra come dialog
                GameOverFragment gameOverDialog = new GameOverFragment();
                gameOverDialog.show(getParentFragmentManager(), "gameOverDialog"); //changed getFragmentManager (deprecated) with getParentFragmentManager
            }
        });

        gameViewModel.quizDataTest();


    }

     */
}