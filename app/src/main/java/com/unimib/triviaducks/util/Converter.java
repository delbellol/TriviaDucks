package com.unimib.triviaducks.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.List;

public class Converter {

    /**
     * Converte una lista di stringhe in una stringa JSON.
     * Questa funzione è annotata con @TypeConverter per permettere a Room di usare questa logica
     * durante la lettura e scrittura dei dati nel database.
     */
    @TypeConverter
    public static String fromListToString(List<String> list) {
        if (list == null) {
            return null; // Se la lista è nulla, restituisce null.
        }
        Gson gson = new Gson(); // Crea un'istanza di Gson per il parsing JSON.
        return gson.toJson(list); // Converte la lista in una stringa JSON.
    }

    /**
     * Converte una stringa JSON in una lista di stringhe.
     * Questa funzione è annotata con @TypeConverter per permettere a Room di usare questa logica
     * durante la lettura e scrittura dei dati nel database.
     */
    @TypeConverter
    public static List<String> fromStringToList(String data) {
        if (data == null) {
            return null; // Se la stringa è nulla, restituisce null.
        }
        Gson gson = new Gson(); // Crea un'istanza di Gson per il parsing JSON.
        Type listType = new TypeToken<List<String>>(){}.getType(); // Definisce il tipo della lista.
        return gson.fromJson(data, listType); // Converte la stringa JSON in una lista di stringhe.
    }

    public static String fromBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap fromBase64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
