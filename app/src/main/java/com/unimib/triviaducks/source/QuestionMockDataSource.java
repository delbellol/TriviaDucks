package com.unimib.triviaducks.source;

import static com.unimib.triviaducks.util.Constants.UNEXPECTED_ERROR;

import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.JSONParserUtils;

import java.io.IOException;

/**
 * Classe per recuperare le domande da un file JSON locale
 */
public class QuestionMockDataSource extends BaseQuestionRemoteDataSource {
    private final JSONParserUtils jsonParserUtil; // Utility per il parsing dei file JSON.

    // Costruttore che inizializza il parser JSON.
    public QuestionMockDataSource(JSONParserUtils jsonParserUtil) {
        this.jsonParserUtil = jsonParserUtil; // Assegna l'utility JSON.
    }

    /**
     * Metodo per simulare il recupero delle domande da un mock (file JSON locale).
     */
    @Override
    public void getQuestions() {
        QuestionAPIResponse questionAPIResponse = null; // Inizializza la risposta.

        try {
            // Prova a fare il parsing del file JSON usando GSon.
            questionAPIResponse = jsonParserUtil.parseJSONFileWithGSon(Constants.SAMPLE_JSON_FILENAME);
        } catch (IOException e) {
            e.printStackTrace(); // Gestisce un'eventuale eccezione di I/O.
        }

        // Verifica se il parsing è andato a buon fine.
        if (questionAPIResponse != null) {
            // Chiama il callback di successo con la risposta e il timestamp corrente.
            questionCallback.onSuccessFromRemote(questionAPIResponse, System.currentTimeMillis());
        } else {
            // Chiama il callback di fallimento in caso di errore.
            questionCallback.onFailureFromRemote(new Exception(UNEXPECTED_ERROR));
        }
    }
}
