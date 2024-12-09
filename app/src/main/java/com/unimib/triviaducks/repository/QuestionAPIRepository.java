package com.unimib.triviaducks.repository;

import android.app.Application;

import androidx.annotation.NonNull;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.database.QuestionDAO;
import com.unimib.triviaducks.database.QuestionRoomDatabase;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.service.QuestionAPIService;
import com.unimib.triviaducks.service.ServiceLocator;
import com.unimib.triviaducks.util.ResponseCallback;
import static com.unimib.triviaducks.util.Constants.FRESH_TIMEOUT;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAPIRepository implements IQuestionRepository{

    private static final String TAG = QuestionAPIRepository.class.getSimpleName();

    private final Application application;
    private final QuestionAPIService questionAPIService;
    private final QuestionDAO questionDAO;
    private final ResponseCallback responseCallback;

    public QuestionAPIRepository(Application application, ResponseCallback responseCallback) {
        this.application = application;
        this.questionAPIService = ServiceLocator.getInstance().getQuestionAPIService();
        this.questionDAO = ServiceLocator.getInstance().getQuestionsDB(application).questionDao();
        this.responseCallback = responseCallback;
    }

    @Override
    public void fetchQuestions(String amount, String type, long lastUpdate){

        long currentTime = System.currentTimeMillis();

        // It gets the news from the Web Service if the last download
        // of the news has been performed more than FRESH_TIMEOUT value ago
        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            Call<QuestionAPIResponse> questionResponseCall = questionAPIService.getQuestions(
                    amount,
                    type
            );

            questionResponseCall.enqueue(new Callback<QuestionAPIResponse>() {
                @Override
                public void onResponse(@NonNull Call<QuestionAPIResponse> call,
                                       @NonNull Response<QuestionAPIResponse> response) {

                    if (response.body() != null && response.isSuccessful() &&
                            response.body().getResponseCode() == 0) {
                        List<Question> questionList = response.body().getResults();
                        //Question.filterQuestions(questionList);
                        saveDataInDatabase(questionList);
                    } else {
                        responseCallback.onFailure(application.getString(R.string.error_retrieving_news));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<QuestionAPIResponse> call, @NonNull Throwable t) {
                    readDataFromDatabase(lastUpdate);
                }
            });
        } else {
            readDataFromDatabase(lastUpdate);
        }
    }


    @Override
    public void updateQuestions(Question question) {
        QuestionRoomDatabase.databaseWriteExecutor.execute(() -> {
            questionDAO.updateQuestion(question);
            //TODO responseCallback.onNewsFavoriteStatusChanged(news);
        });
    }

    private void saveDataInDatabase(List<Question> questionList) {
        QuestionRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Question> allQuestions = questionDAO.getAll();

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
            List<Long> insertedNewsIds = questionDAO.insertQuestionList(questionList);
            for (int i = 0; i < questionList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                questionList.get(i).setUid(insertedNewsIds.get(i));
            }

            responseCallback.onSuccess(questionList, System.currentTimeMillis());
        });
    }

    private void readDataFromDatabase(long lastUpdate) {
        QuestionRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(questionDAO.getAll(), lastUpdate);
        });
    }
}