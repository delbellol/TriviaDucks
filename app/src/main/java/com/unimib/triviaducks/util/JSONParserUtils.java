package com.unimib.triviaducks.util;


import android.content.Context;

import com.google.gson.Gson;
import com.unimib.triviaducks.model.QuestionAPIResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// Classe utility per il parsing di file JSON utilizzando Gson
public class JSONParserUtils {

    // Variabile membro per il contesto, necessaria per accedere agli assets
    public Context context;

    // Costruttore che inizializza il contesto
    public JSONParserUtils(Context context) {
        this.context = context;
    }

    // Metodo per il parsing di un file JSON da assets usando Gson
    public QuestionAPIResponse parseJSONFileWithGSon(String filename) throws IOException {
        // Apre il file specificato nella cartella assets
        InputStream inputStream = context.getAssets().open(filename);
        // Crea un BufferedReader per una lettura efficiente del file aperto
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        // Utilizza Gson per convertire il contenuto JSON nel modello QuestionAPIResponse
        return new Gson().fromJson(bufferedReader, QuestionAPIResponse.class);
    }
}