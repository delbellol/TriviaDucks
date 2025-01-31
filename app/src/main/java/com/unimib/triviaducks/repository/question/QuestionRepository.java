package com.unimib.triviaducks.repository.question;

import androidx.lifecycle.MutableLiveData;

import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.source.question.BaseQuestionRemoteDataSource;

import java.util.List;

public class QuestionRepository implements QuestionResponseCallback {

    private static final String TAG = QuestionRepository.class.getSimpleName();

    // LiveData per osservare i risultati delle operazioni
    private MutableLiveData<Result> allQuestionsMutableLiveData;
    // Data source remoto per le domande
    private final BaseQuestionRemoteDataSource questionRemoteDataSource;
    // Data source locale per le domande

    // Costruttore della repository che inizializza i data sources e imposta i callback
    public QuestionRepository(BaseQuestionRemoteDataSource questionRemoteDataSource) {
        allQuestionsMutableLiveData = new MutableLiveData<>(); // Inizializza il LiveData
        this.questionRemoteDataSource = questionRemoteDataSource; // Assegna il data source remoto
        this.questionRemoteDataSource.setQuestionCallback(this); // Imposta il callback remoto
    }

    /**
     * Metodo per recuperare le domande
     */
    public MutableLiveData<Result> fetchQuestions(int category, int questionAmount, String difficulty) {

        questionRemoteDataSource.fetchQuestions(category,questionAmount, difficulty);
        allQuestionsMutableLiveData = questionRemoteDataSource.getQuestions();
        return allQuestionsMutableLiveData; // Restituisce il LiveData aggiornato
    }

    /**
     * Metodo per recuperare le domande direttamente dal locale
     */
    public MutableLiveData<Result> getQuestions() {
        questionRemoteDataSource.getQuestions();
        //questionLocalDataSource.getQuestions(); // Recupera le domande dal data source locale
        return allQuestionsMutableLiveData; // Restituisce il LiveData con i risultati
    }

    /**
     * Callback per il successo dal data source remoto
     * @param questionAPIResponse Risposta API con le domande
     * @param lastUpdate Tempo di aggiornamento
     */
    @Override
    public void onSuccessFromRemote(QuestionAPIResponse questionAPIResponse, long lastUpdate) {
        // Salva le domande recuperate nel data source locale.
        //questionLocalDataSource.insertQuestions(questionAPIResponse.getQuestions());

    }

    /**
     * Callback per errore dal data source remoto
     * @param exception Eccezione ricevuta
     */
    @Override
    public void onFailureFromRemote(Exception exception) {
        // Crea un risultato di errore e lo invia al LiveData
        Result.Error result = new Result.Error(exception.getMessage());
        allQuestionsMutableLiveData.postValue(result);
    }
}
