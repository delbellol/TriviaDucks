package com.unimib.triviaducks.repository;

import android.app.Application;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.database.QuestionDAO;
import com.unimib.triviaducks.database.QuestionRoomDatabase;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.JSONParserUtils;
import com.unimib.triviaducks.util.ResponseCallback;
import com.unimib.triviaducks.service.ServiceLocator;

import java.io.IOException;
import java.util.List;


public class QuestionMockRepository implements IQuestionRepository{
    private final Application application;
    private final ResponseCallback responseCallback;
    private final QuestionDAO questionDao;

    public QuestionMockRepository(Application application, ResponseCallback responseCallback) {
        this.application = application;
        this.responseCallback = responseCallback;
        this.questionDao = ServiceLocator.getInstance().getQuestionsDB(application).questionDao();
    }

    @Override
    public void fetchQuestions(String amount, String type, long lastUpdate) {
        QuestionAPIResponse questionApiResponse = null;

        JSONParserUtils jsonParserUtils = new JSONParserUtils(application.getApplicationContext());

        try {
            questionApiResponse = jsonParserUtils.parseJSONFileWithGSon(Constants.SAMPLE_JSON_FILENAME);
            if (questionApiResponse != null) {
                saveDataInDatabase(questionApiResponse.getResults());
            } else {
                responseCallback.onFailure(application.getString(R.string.error_retrieving_news));
            }
        } catch (IOException e) {
            responseCallback.onFailure(application.getString(R.string.error_retrieving_news));
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateQuestions(Question question) {

    }

    private void saveDataInDatabase(List<Question> questionList) {
        QuestionRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Question> allQuestions = questionDao.getAll();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (Question question : allQuestions) {
                // This check works because News and NewsSource classes have their own
                // implementation of equals(Object) and hashCode() methods

                if (questionList.contains(question)) {
                    // The primary key and the favorite status is contained only in the News objects
                    // retrieved from the database, and not in the News objects downloaded from the
                    // Web Service. If the same news was already downloaded earlier, the following
                    // line of code replaces the the News object in newsList with the corresponding
                    // News object saved in the database, so that it has the primary key and the
                    // favorite status.
                    questionList.set(questionList.indexOf(question), question);
                }
            }

            // Writes the news in the database and gets the associated primary keys
            List<Long> insertedQuestionsIds = questionDao.insertQuestionList(questionList);
            for (int i = 0; i < questionList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                questionList.get(i).setUid(insertedQuestionsIds.get(i));
            }

            responseCallback.onSuccess(questionList, System.currentTimeMillis());
        });
    }

    private void readDataFromDatabase(long lastUpdate) {
        QuestionRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(questionDao.getAll(), lastUpdate);
        });
    }
}
