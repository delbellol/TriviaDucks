package com.unimib.triviaducks.util;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.unimib.triviaducks.model.QuestionAPIResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONParserUtils {
    public Context context;
    public JSONParserUtils(Context context) {
        this.context = context;
    }

    public QuestionAPIResponse parseJSONFileWithGSon(String filename) throws IOException {
        InputStream inputStream = context.getAssets().open(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return new Gson().fromJson(bufferedReader, QuestionAPIResponse.class);
    }

    //E' come la funzione sopra ma anziché prendere da un file lo prende da un JSON direttamente in stringa
    public QuestionAPIResponse parseJSONWithGSon(String json) throws IOException {
        return new Gson().fromJson(json, QuestionAPIResponse.class);
    }
}
