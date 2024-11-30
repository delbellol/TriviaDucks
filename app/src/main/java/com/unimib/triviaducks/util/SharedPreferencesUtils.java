package com.unimib.triviaducks.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPreferencesUtils {
    private final Context context;

    public SharedPreferencesUtils(Context context) {
        this.context = context;
    }

    //writeStringData prende in input: il file dove vogliamo salvare la stringa, chiave, valore
    public void writeStringData(String sharedPreferencesFileName, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE); // modo per accedere a SharedPreferences, prende in input il filename
        SharedPreferences.Editor editor = sharedPref.edit(); //si estrae l'oggetto editor che ci permette di fare modifiche nel dizionario
        editor.putString(key, value); //aggiungo la stringa all'editor
        editor.apply();
    }

    //come writeStringData ma per un insieme di stringhe (per esempio le categorie)
    public void writeStringSetData(String sharedPreferencesFileName, String key, Set<String> value) {
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    //legge una sringa
    public String readStringData(String sharedPreferencesFileName, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        return sharedPref.getString(key, null); //prende la chiave e ritorna la stringa
    }

    //legge un insieme di stringhe
    public Set<String> readStringSetData(String sharedPreferencesFileName, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        return sharedPref.getStringSet(key, null);
    }
}
