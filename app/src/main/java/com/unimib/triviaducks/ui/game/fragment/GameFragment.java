package com.unimib.triviaducks.ui.game.fragment;


import static java.lang.String.*;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.QuestionRepository;
import com.unimib.triviaducks.ui.game.viewmodel.QuestionViewModel;
import com.unimib.triviaducks.ui.game.viewmodel.QuestionViewModelFactory;
import com.unimib.triviaducks.ui.home.fragment.GameModeFragment;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// La classe rappresenta un Fragment per il gioco
public class GameFragment extends Fragment {
    private static final String TAG = GameFragment.class.getSimpleName();

    private int counter = 0; // Contatore per le domande

    private SharedPreferencesUtils sharedPreferencesUtils; // Utilità per la gestione delle preferenze

    private Question currentQuestion; // La domanda corrente

    private QuestionRepository questionRepository; // Repository per ottenere le domande
    private List<Question> questionList; // Lista delle domande
    private QuestionViewModel questionViewModel; // ViewModel per gestire le domande

    private CircularProgressIndicator circularProgressIndicator; // Indicatore di caricamento
    private ConstraintLayout gameLayout; // Layout principale del gioco

    private TextView questionTextView, counterTextView; // TextView per la domanda e il contatore
    private Button answerButton1, answerButton2, answerButton3, answerButton4; // Bottoni per le risposte
    private ImageButton closeImageButton; // Bottone per chiudere il gioco

    public GameFragment() { } // Costruttore del Fragment

    @Override
    public void onCreate(Bundle savedInstanceState) { // Metodo chiamato quando il Fragment viene creato
        super.onCreate(savedInstanceState);

        // Ottiene il QuestionRepository dal ServiceLocator
        QuestionRepository questionRepository =
                ServiceLocator.getInstance().getQuestionsRepository(
                        requireActivity().getApplication(),
                        requireActivity().getApplication().getResources().getBoolean(R.bool.debug_mode)
                );

        // Verifica che il repository non sia nullo
        if (questionRepository == null) {
            Log.e(TAG, "QuestionRepository is null!"); // Log di errore
        } else {
            // Crea il ViewModel utilizzando il repository
            questionViewModel = new ViewModelProvider(
                    requireActivity(),
                    new QuestionViewModelFactory(questionRepository)
            ).get(QuestionViewModel.class);

            if (questionViewModel == null) {
                Log.e(TAG, "questionViewModel is null!"); // Log di errore
            }
        }

        // Inizializza la lista delle domande
        questionList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { // Metodo chiamato per creare la vista del Fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false); // Inflaziona il layout

        // Inizializza gli elementi della vista
        circularProgressIndicator = view.findViewById(R.id.circularProgressIndicator);
        gameLayout = view.findViewById(R.id.gameLayout);

        closeImageButton = view.findViewById(R.id.close_game);

        counterTextView = view.findViewById(R.id.counter);

        questionTextView = view.findViewById(R.id.question);

        //TODO probabilmente per i bottoni delle risposte connviene utilizzare una recycler view/adapter
        answerButton1 = view.findViewById(R.id.answer1);
        answerButton2 = view.findViewById(R.id.answer2);
        answerButton3 = view.findViewById(R.id.answer3);
        answerButton4 = view.findViewById(R.id.answer4);

        // Gestisce l'evento di clic sul bottone per chiudere il gioco
        closeImageButton.setOnClickListener(v -> {
            GameQuitFragment gameQuitDialog = new GameQuitFragment();
            gameQuitDialog.show(getParentFragmentManager(), "GameQuitFragment");
        });

        // Gestisce l'evento di clic sui bottoni di risposta
        View.OnClickListener answerClickListener = v -> {
            Button clickedButton = (Button) v; // Ottiene il bottone cliccato
            checkAnswer(clickedButton.getText().toString(), view); // Verifica la risposta
        };

        // Assegna l'ascoltatore agli altri bottoni di risposta
        answerButton1.setOnClickListener(answerClickListener);
        answerButton2.setOnClickListener(answerClickListener);
        answerButton3.setOnClickListener(answerClickListener);
        answerButton4.setOnClickListener(answerClickListener);

        // Ottiene le domande dal ViewModel e aggiorna la vista
        questionViewModel.getQuestions(10, "multiple", System.currentTimeMillis()).observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccess()) {
                        questionList.clear();
                        questionList.addAll(((Result.Success) result).getData().getQuestions());

                        circularProgressIndicator.setVisibility(View.GONE); // Nasconde l'indicatore di caricamento
                        gameLayout.setVisibility(View.VISIBLE); // Mostra il layout del gioco

                        loadNextQuestion(); // Carica la prossima domanda
                    } else {
                        Snackbar.make(view, "Errore nel caricamento delle domande.", Snackbar.LENGTH_SHORT).show();
                    }
                });

        return view;
    }

    private void checkAnswer(String selectedAnswer, View view) { // Verifica la risposta selezionata
        if (currentQuestion != null && selectedAnswer.equals(Jsoup.parse(currentQuestion.getCorrectAnswer()).text())) {
            Snackbar.make(view, "Risposta corretta!", Snackbar.LENGTH_SHORT).show();

            // Passa alla prossima domanda
            counter++;
            if (counter < questionList.size()) {
                loadNextQuestion();
            } else {
                // Fine delle domande
                Snackbar.make(view, "Hai completato il quiz!", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(view, "Risposta sbagliata!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void loadNextQuestion() { // Carica la prossima domanda
        currentQuestion = questionList.get(counter); // Ottiene la domanda corrente

        // Log della domanda e della risposta corretta
        Log.d(TAG, Jsoup.parse(currentQuestion.getQuestion()).text());
        Log.d(TAG, Jsoup.parse(currentQuestion.getCorrectAnswer()).text());

        // Mescola le risposte
        List<String> allAnswers = new ArrayList<>(currentQuestion.getIncorrectAnswers());
        allAnswers.add(currentQuestion.getCorrectAnswer());
        Collections.shuffle(allAnswers);

        // Imposta il testo per il contatore e la domanda
        counterTextView.setText(format("Domanda N. %d", counter + 1));
        questionTextView.setText(Jsoup.parse(currentQuestion.getQuestion()).text());

        // Imposta il testo per i bottoni delle risposte
        answerButton1.setText(Jsoup.parse(allAnswers.get(0)).text());
        answerButton2.setText(Jsoup.parse(allAnswers.get(1)).text());
        answerButton3.setText(Jsoup.parse(allAnswers.get(2)).text());
        answerButton4.setText(Jsoup.parse(allAnswers.get(3)).text());
    }
}