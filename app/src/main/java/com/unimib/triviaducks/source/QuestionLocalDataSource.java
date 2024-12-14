package com.unimib.triviaducks.source;

import com.unimib.triviaducks.database.QuestionDAO;
import com.unimib.triviaducks.database.QuestionRoomDatabase;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

import java.util.List;

/**
 * Class to get questions from local database using Room.
 */
public class QuestionLocalDataSource extends BaseQuestionLocalDataSource{
    private final QuestionDAO questionDAO;
    private final SharedPreferencesUtils sharedPreferencesUtil;

    public QuestionLocalDataSource(QuestionRoomDatabase questionRoomDatabase,
                                  SharedPreferencesUtils sharedPreferencesUtil) {
        this.questionDAO = questionRoomDatabase.questionDao();
        this.sharedPreferencesUtil = sharedPreferencesUtil;
    }

    /**
     * Gets the questions from the local database.
     * The method is executed with an ExecutorService defined in QuestionRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    @Override
    public void getQuestions() {
        QuestionRoomDatabase.databaseWriteExecutor.execute(() -> {
            questionCallback.onSuccessFromLocal(questionDAO.getAll());
        });
    }

    //TODO sistemare gli altri metodi

    @Override
    public void updateQuestion(Question question) {

    }

    @Override
    public void deleteQuestions() {

    }

    @Override
    public void insertQuestions(List<Question> questionList) {

    }
}
