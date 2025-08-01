package com.unimib.triviaducks.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPreferencesUtils {
    private final Context context;

    public SharedPreferencesUtils(Context context) {
        this.context = context;
    }

    public void writeStringData(String sharedPreferencesFileName, String key, String value) {
        // Ottiene il riferimento alle SharedPreferences con il nome specificato, in modalità privata.
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        // Ottiene l'editor per modificare le SharedPreferences.
        SharedPreferences.Editor editor = sharedPref.edit();
        // Aggiunge una stringa all'editor con la chiave e il valore specificato.
        editor.putString(key, value);
        // Applica le modifiche in modo asincrono (senza bloccare il thread).
        editor.apply();
    }

    public void writeIntData(String sharedPreferencesFileName, String key, int value) {
        // Ottiene il riferimento alle SharedPreferences con il nome specificato, in modalità privata.
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        // Ottiene l'editor per modificare le SharedPreferences.
        SharedPreferences.Editor editor = sharedPref.edit();
        // Aggiunge un intero all'editor con la chiave e il valore specificato.
        editor.putInt(key, value);
        // Applica le modifiche in modo asincrono (senza bloccare il thread).
        editor.apply();
    }

    public void writeStringSetData(String sharedPreferencesFileName, String key, Set<String> value) {
        // Ottiene il riferimento alle SharedPreferences con il nome specificato, in modalità privata.
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        // Ottiene l'editor per modificare le SharedPreferences.
        SharedPreferences.Editor editor = sharedPref.edit();
        // Aggiunge un set di stringhe all'editor con la chiave e il valore specificato.
        editor.putStringSet(key, value);
        // Applica le modifiche in modo asincrono.
        editor.apply();
    }


    public void writeBooleanData(String sharedPreferencesFileName, String key, boolean value) {
        // Ottiene il riferimento alle SharedPreferences con il nome specificato, in modalità privata.
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        // Ottiene l'editor per modificare le SharedPreferences.
        SharedPreferences.Editor editor = sharedPref.edit();
        // Aggiunge una stringa all'editor con la chiave e il valore specificato.
        editor.putBoolean(key, value);
        // Applica le modifiche in modo asincrono (senza bloccare il thread).
        editor.apply();
    }

    public String readStringData(String sharedPreferencesFileName, String key) {
        // Ottiene il riferimento alle SharedPreferences con il nome specificato, in modalità privata.
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        // Restituisce il valore associato alla chiave, o null se non presente.
        return sharedPref.getString(key, null);
    }

    public Set<String> readStringSetData(String sharedPreferencesFileName, String key) {
        // Ottiene il riferimento alle SharedPreferences con il nome specificato, in modalità privata.
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        // Restituisce il set di stringhe associato alla chiave, o null se non presente.
        return sharedPref.getStringSet(key, null);
    }

    public int readIntData(String sharedPreferencesFileName, String key) {
        // Ottiene il riferimento alle SharedPreferences con il nome specificato, in modalità privata.
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        // Restituisce il valore associato alla chiave, o null se non presente.
        return sharedPref.getInt(key, 0);
    }

    public boolean readBooleanData(String sharedPreferencesFileName, String key) {
        // Ottiene il riferimento alle SharedPreferences con il nome specificato, in modalità privata.
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        // Restituisce il valore associato alla chiave, o null se non presente.
        return sharedPref.getBoolean(key, false);
    }

    public void clearPreferences(String fileName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
