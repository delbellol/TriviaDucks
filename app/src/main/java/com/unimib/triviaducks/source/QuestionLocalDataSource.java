package com.unimib.triviaducks.source;

import com.unimib.triviaducks.database.QuestionDAO;
import com.unimib.triviaducks.database.QuestionRoomDatabase;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.util.Constants;
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
        QuestionRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Question> allQuestions = questionDAO.getAll();

            if (questionList != null) {

                // Checks if the news just downloaded has already been downloaded earlier
                // in order to preserve the news status (marked as favorite or not)
                for (Question question : allQuestions) {
                    // This check works because News and NewsSource classes have their own
                    // implementation of equals(Object) and hashCode() methods
                    if (questionList.contains(question)) {
                        // The primary key and the favorite status is contained only in the News objects
                        // retrieved from the database, and not in the News objects downloaded from the
                        // Web Service. If the same news was already downloaded earlier, the following
                        // line of code replaces the News object in newsList with the corresponding
                        // line of code replaces the News object in newsList with the corresponding
                        // News object saved in the database, so that it has the primary key and the
                        // favorite status.
                        questionList.set(questionList.indexOf(question), question);
                    }
                }

                // Writes the news in the database and gets the associated primary keys
                List<Long> insertedQuestionsIds = questionDAO.insertQuestionList(questionList);
                for (int i = 0; i < questionList.size(); i++) {
                    // Adds the primary key to the corresponding object News just downloaded so that
                    // if the user marks the news as favorite (and vice-versa), we can use its id
                    // to know which news in the database must be marked as favorite/not favorite
                    questionList.get(i).setUid(insertedQuestionsIds.get(i));
                }

                sharedPreferencesUtil.writeStringData(Constants.SHARED_PREFERENCES_FILENAME,
                        Constants.SHARED_PREFERNECES_LAST_UPDATE, String.valueOf(System.currentTimeMillis()));

                questionCallback.onSuccessFromLocal(questionList);
            }
        });
    }
}
