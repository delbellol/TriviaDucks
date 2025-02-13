package com.unimib.triviaducks.util;

import static com.unimib.triviaducks.util.Constants.USER_AGENT;
import static com.unimib.triviaducks.util.Constants.USER_AGENT_DESCRIPTION;

import android.app.Application;

import com.unimib.triviaducks.repository.question.QuestionRepository;
import com.unimib.triviaducks.repository.user.IUserRepository;
import com.unimib.triviaducks.repository.user.UserRepository;
import com.unimib.triviaducks.service.QuestionAPIService;
import com.unimib.triviaducks.source.question.BaseQuestionRemoteDataSource;
import com.unimib.triviaducks.source.question.QuestionRemoteDataSource;
import com.unimib.triviaducks.source.user.BaseUserAuthenticationRemoteDataSource;
import com.unimib.triviaducks.source.user.BaseUserDataRemoteDataSource;
import com.unimib.triviaducks.source.user.UserAuthenticationFirebaseDataSource;
import com.unimib.triviaducks.source.user.UserFirebaseDataSource;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {
    private static final String TAG = ServiceLocator.class.getSimpleName();

    private static volatile ServiceLocator INSTANCE = null;
    private ServiceLocator() {}

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
                        .header(USER_AGENT_DESCRIPTION, USER_AGENT)
                        .build();
                return chain.proceed(request);
            })
            .build();

    /**
     * Restituisce un'istanza del servizio Retrofit per le domande.
     * @return Un'istanza di QuestionAPIService.
     */
    public QuestionAPIService getQuestionAPIService() {
        // Configura Retrofit con il client OkHttp e il converter Gson.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.TRIVIA_API_BASE_URL) // Imposta l'URL di base dell'API.
                .client(client) // Usa il client OkHttp configurato.
                .addConverterFactory(GsonConverterFactory.create()).build(); // Usa il converter Gson per la serializzazione/deserializzazione.
        return retrofit.create(QuestionAPIService.class); // Crea e restituisce l'istanza del servizio API.
    }

    public QuestionRepository getQuestionsRepository(Application application) {
        BaseQuestionRemoteDataSource questionRemoteDataSource;
        SharedPreferencesUtils sharedPreferencesUtil = new SharedPreferencesUtils(application);

        questionRemoteDataSource = new QuestionRemoteDataSource();

        return new QuestionRepository(questionRemoteDataSource);
    }

    public IUserRepository getUserRepository(Application application) {
        SharedPreferencesUtils sharedPreferencesUtil = new SharedPreferencesUtils(application);

        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource =
                new UserAuthenticationFirebaseDataSource();

        BaseUserDataRemoteDataSource userDataRemoteDataSource =
                new UserFirebaseDataSource(sharedPreferencesUtil);

        return new UserRepository(userRemoteAuthenticationDataSource,
                userDataRemoteDataSource);
    }
}
