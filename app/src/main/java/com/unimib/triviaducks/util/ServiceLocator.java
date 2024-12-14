package com.unimib.triviaducks.util;

import android.app.Application;
import android.util.Log;

import com.unimib.triviaducks.database.QuestionRoomDatabase;
import com.unimib.triviaducks.repository.QuestionRepository;
import com.unimib.triviaducks.service.QuestionAPIService;
import com.unimib.triviaducks.source.BaseQuestionLocalDataSource;
import com.unimib.triviaducks.source.BaseQuestionRemoteDataSource;
import com.unimib.triviaducks.source.QuestionLocalDataSource;
import com.unimib.triviaducks.source.QuestionMockDataSource;
import com.unimib.triviaducks.source.QuestionRemoteDataSource;
import com.unimib.triviaducks.ui.game.fragment.GameFragment;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {
    private static final String TAG = ServiceLocator.class.getSimpleName();

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {
    }

    /**
     * Returns an instance of ServiceLocator class.
     * @return An instance of ServiceLocator.
     */
    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized (ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .build();
                return chain.proceed(request);
            })
            .build();

    /**
     * Returns an instance of QuestionAPIService class using Retrofit.
     * @return an instance of QuestionAPIService.
     */

    public QuestionAPIService getQuestionAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.TRIVIA_API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(QuestionAPIService.class);
    }

    /**
     * Returns an instance of QuestionRoomDatabase class to manage Room database.
     * @param application Param for accessing the global application state.
     * @return An instance of QuestionRoomDatabase.
     */

    public QuestionRoomDatabase getQuestionsDB(Application application) {
        return QuestionRoomDatabase.getDatabase(application);
    }

    /**
     * Returns an instance of QuestionRepository.
     * @param application Param for accessing the global application state.
     * @param debugMode Param to establish if the application is run in debug mode.
     * @return An instance of QuestionRepository.
     */
    public QuestionRepository getQuestionsRepository(Application application, boolean debugMode) {
        BaseQuestionRemoteDataSource questionRemoteDataSource;
        BaseQuestionLocalDataSource questionLocalDataSource;
        SharedPreferencesUtils sharedPreferencesUtil = new SharedPreferencesUtils(application);

        if (debugMode) {
            JSONParserUtils jsonParserUtil = new JSONParserUtils(application);
            questionRemoteDataSource =
                    new QuestionMockDataSource(jsonParserUtil);
        } else {
            questionRemoteDataSource =
                    new QuestionRemoteDataSource();
        }

        questionLocalDataSource = new QuestionLocalDataSource(getQuestionsDB(application), sharedPreferencesUtil);

        return new QuestionRepository(questionRemoteDataSource, questionLocalDataSource);
    }
}
