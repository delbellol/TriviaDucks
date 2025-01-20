package com.unimib.triviaducks.repository.question;

import androidx.lifecycle.MutableLiveData;

import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.source.question.BaseQuestionLocalDataSource;
import com.unimib.triviaducks.source.question.BaseQuestionRemoteDataSource;

import java.util.List;

public class QuestionRepository implements QuestionResponseCallback {

    private static final String TAG = QuestionRepository.class.getSimpleName();

    // LiveData per osservare i risultati delle operazioni
    private final MutableLiveData<Result> allQuestionsMutableLiveData;
    // Data source remoto per le domande
    private final BaseQuestionRemoteDataSource questionRemoteDataSource;
    // Data source locale per le domande
    private final BaseQuestionLocalDataSource questionLocalDataSource;

    // Costruttore della repository che inizializza i data sources e imposta i callback
    public QuestionRepository(BaseQuestionRemoteDataSource questionRemoteDataSource,
                              BaseQuestionLocalDataSource questionLocalDataSource) {
        allQuestionsMutableLiveData = new MutableLiveData<>(); // Inizializza il LiveData
        this.questionRemoteDataSource = questionRemoteDataSource; // Assegna il data source remoto
        this.questionLocalDataSource = questionLocalDataSource; // Assegna il data source locale
        this.questionRemoteDataSource.setQuestionCallback(this); // Imposta il callback remoto
        this.questionLocalDataSource.setQuestionCallback(this); // Imposta il callback locale
    }

    /**
     * Metodo per recuperare le domande
     * @param amount Quantità di domande richieste
     * @param type Tipo di domande
     * @param lastUpdate Timestamp dell'ultimo aggiornamento
     */
    public MutableLiveData<Result> fetchQuestions(int amount, String type, long lastUpdate) {
        long currentTime = System.currentTimeMillis(); // Ottiene il tempo corrente in millisecondi

        // Logica per determinare se prendere le domande dal server o dal locale
        // TODO sistemare if
        // TODO ci sarà da sistemare il passaggio di parametri amount e type sono costanti,
        // TODO l'unica cosa da passare è category
        //Log.d(TAG, String.valueOf((currentTime - lastUpdate > FRESH_TIMEOUT)));

        // Se il tempo trascorso supera un valore di timeout, scarica da remoto, altrimenti locale
        // TODO da sistemare la logica di questo if in modo che sia adeguata al nostro codice, oppoure toglierla direttamente
        //if (currentTime - lastUpdate > FRESH_TIMEOUT) {
        questionRemoteDataSource.getQuestions(); // Recupera le domande dal data source remoto
        //} else {
        //    questionLocalDataSource.getQuestions(); // Recupera le domande dal data source locale
        //}

        return allQuestionsMutableLiveData; // Restituisce il LiveData aggiornato
    }

    /**
     * Metodo per recuperare le domande direttamente dal locale
     */
    public MutableLiveData<Result> getQuestions() {
        questionLocalDataSource.getQuestions(); // Recupera le domande dal data source locale
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
        questionLocalDataSource.insertQuestions(questionAPIResponse.getQuestions());
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

    /**
     * Callback per il successo dal data source locale
     * @param questionList Lista di domande recuperate
     */
    @Override
    public void onSuccessFromLocal(List<Question> questionList) {
        // Crea un risultato di successo e lo invia al LiveData
        Result.QuestionSuccess result = new Result.QuestionSuccess(new QuestionAPIResponse(questionList));
        allQuestionsMutableLiveData.postValue(result);
    }

    /**
     * Callback per errore dal data source locale
     * @param exception Eccezione ricevuta
     */
    @Override
    public void onFailureFromLocal(Exception exception) {
        // Crea un risultato di errore e lo invia al LiveData
        Result.Error resultError = new Result.Error(exception.getMessage());
        allQuestionsMutableLiveData.postValue(resultError);
    }
}
