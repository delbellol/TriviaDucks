package com.unimib.triviaducks.util;

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
    private static final String TAG = ServiceLocator.class.getSimpleName(); // Tag per il logging.

    private static volatile ServiceLocator INSTANCE = null; // Istanza singleton della classe.

    // Costruttore privato per evitare istanziazione esterna.
    private ServiceLocator() {
    }

    /**
     * Restituisce l'istanza singleton della classe ServiceLocator.
     * @return L'istanza di ServiceLocator.
     */
    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized (ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator(); // Crea l'istanza se non esiste.
                }
            }
        }
        return INSTANCE; // Restituisce l'istanza singleton.
    }

    // Configura OkHttpClient con un interceptor per aggiungere l'intestazione "User-Agent" a ogni richiesta.
    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .build(); // Modifica la richiesta per aggiungere l'header.
                return chain.proceed(request); // Procede con la richiesta modificata.
            })
            .build(); // Crea l'istanza di OkHttpClient.

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

    /**
     * Restituisce un'istanza di QuestionRepository, che funge da fonte di dati per le domande.
     * @param application Parametro per accedere allo stato globale dell'applicazione.
     * @param debugMode Parametro per stabilire se l'applicazione è in modalità di debug.
     * @return Un'istanza di QuestionRepository.
     */
    public QuestionRepository getQuestionsRepository(Application application, boolean debugMode) {
        BaseQuestionRemoteDataSource questionRemoteDataSource; // Fonte remota delle domande.
        SharedPreferencesUtils sharedPreferencesUtil = new SharedPreferencesUtils(application); // Utility per SharedPreferences.

        questionRemoteDataSource = new QuestionRemoteDataSource();

        // Restituisce un'istanza di QuestionRepository con le sorgenti remote e locali.
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
