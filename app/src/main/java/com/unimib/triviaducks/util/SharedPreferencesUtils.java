package com.unimib.triviaducks.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPreferencesUtils {
    private final Context context; // Variabile di contesto per accedere alle SharedPreferences.

    public static int getCategory() {
        return category;
    }

    public static void setCategory(int category) {
        SharedPreferencesUtils.category = category;
    }

    private static int category;

    // Costruttore che prende il contesto dell'applicazione per accedere alle SharedPreferences.
    public SharedPreferencesUtils(Context context) {
        this.context = context;
    }

    /**
     * Scrive una stringa nelle SharedPreferences.
     * @param sharedPreferencesFileName Il nome del file delle SharedPreferences.
     * @param key La chiave per identificare il valore da salvare.
     * @param value Il valore da salvare.
     */
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



    /**
     * Scrive un insieme di stringhe nelle SharedPreferences.
     * @param sharedPreferencesFileName Il nome del file delle SharedPreferences.
     * @param key La chiave per identificare il set di valori da salvare.
     * @param value Il set di stringhe da salvare.
     */
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

    /**
     * Legge una stringa dalle SharedPreferences.
     * @param sharedPreferencesFileName Il nome del file delle SharedPreferences.
     * @param key La chiave per identificare il valore da leggere.
     * @return La stringa salvata, o null se non presente.
     */
    public String readStringData(String sharedPreferencesFileName, String key) {
        // Ottiene il riferimento alle SharedPreferences con il nome specificato, in modalità privata.
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        // Restituisce il valore associato alla chiave, o null se non presente.
        return sharedPref.getString(key, null);
    }

    /**
     * Legge un insieme di stringhe dalle SharedPreferences.
     * @param sharedPreferencesFileName Il nome del file delle SharedPreferences.
     * @param key La chiave per identificare il set di valori da leggere.
     * @return Il set di stringhe salvato, o null se non presente.
     */
    public Set<String> readStringSetData(String sharedPreferencesFileName, String key) {
        // Ottiene il riferimento alle SharedPreferences con il nome specificato, in modalità privata.
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        // Restituisce il set di stringhe associato alla chiave, o null se non presente.
        return sharedPref.getStringSet(key, null);
    }
}
