package com.unimib.triviaducks.util;


import com.google.gson.Gson;
import com.unimib.triviaducks.model.QuestionAPIResponse;

import java.io.IOException;

public class JSONParserUtils {
    //public Context context;
    public JSONParserUtils() {

    }

    //E' come la funzione sopra ma anziché prendere da un file lo prende da un JSON direttamente in stringa
    public QuestionAPIResponse parseJSONWithGSon(String json) throws IOException {
        return new Gson().fromJson(json, QuestionAPIResponse.class);
    }
}
