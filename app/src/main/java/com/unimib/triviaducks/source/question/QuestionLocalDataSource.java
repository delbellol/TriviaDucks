package com.unimib.triviaducks.source.question;

import com.unimib.triviaducks.database.QuestionDAO;
import com.unimib.triviaducks.database.QuestionRoomDatabase;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

import java.util.List;

/**
 * Class to get questions from local database using Room.
 */
public class QuestionLocalDataSource extends BaseQuestionLocalDataSource {
    private final QuestionDAO questionDAO; // DAO per interagire con il database locale.
    private final SharedPreferencesUtils sharedPreferencesUtil; // Utility per gestire SharedPreferences.

    // Costruttore che inizializza il DAO e le SharedPreferences.
    public QuestionLocalDataSource(QuestionRoomDatabase questionRoomDatabase,
                                   SharedPreferencesUtils sharedPreferencesUtil) {
        this.questionDAO = questionRoomDatabase.questionDao(); // Ottiene l'istanza del DAO.
        this.sharedPreferencesUtil = sharedPreferencesUtil; // Inizializza SharedPreferences.
    }

    /**
     * Recupera le domande dal database locale.
     * Eseguito su un thread separato tramite ExecutorService per evitare errori sul thread principale.
     */
    @Override
    public void getQuestions() {
        QuestionRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Chiama il callback di successo con le domande dal database.
            questionCallback.onSuccessFromLocal(questionDAO.getAll());
        });
    }

    // TODO: Implementare altri metodi.

    /**
     * Metodo per aggiornare una domanda nel database.
     */
    @Override
    public void updateQuestion(Question question) {
        // TODO: implementare logica di aggiornamento.
    }

    /**
     * Metodo per eliminare tutte le domande dal database.
     */
    @Override
    public void deleteQuestions() {
        // TODO: implementare logica di eliminazione.
    }

    /**
     * Inserisce una lista di domande nel database.
     * Eseguito su un thread separato per evitare operazioni sul main thread.
     * @param questionList Lista di domande da inserire.
     */
    @Override
    public void insertQuestions(List<Question> questionList) {
        QuestionRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Recupera tutte le domande esistenti nel database.
            List<Question> allQuestions = questionDAO.getAll();

            if (questionList != null) {
                // Confronta le domande esistenti con quelle da inserire per preservare lo stato (es. preferiti).
                for (Question question : allQuestions) {
                    if (questionList.contains(question)) {
                        // Sostituisce l'elemento nella lista con quello del database per mantenere la chiave primaria
                        // e lo stato del preferito.
                        questionList.set(questionList.indexOf(question), question);
                    }
                }

                // Inserisce la lista aggiornata delle domande nel database e ottiene gli ID delle righe inserite.
                List<Long> insertedQuestionsIds = questionDAO.insertQuestionList(questionList);
                for (int i = 0; i < questionList.size(); i++) {
                    // Assegna la chiave primaria (UID) alle domande appena inserite per tracciarle.
                    questionList.get(i).setUid(insertedQuestionsIds.get(i));
                }

                // Aggiorna il timestamp dell'ultimo aggiornamento nel file SharedPreferences.
                sharedPreferencesUtil.writeStringData(Constants.SHARED_PREFERENCES_FILENAME,
                        Constants.SHARED_PREFERENCES_LAST_UPDATE, String.valueOf(System.currentTimeMillis()));

                // Chiama il callback di successo con la lista aggiornata.
                questionCallback.onSuccessFromLocal(questionList);
            }
        });
    }
}
