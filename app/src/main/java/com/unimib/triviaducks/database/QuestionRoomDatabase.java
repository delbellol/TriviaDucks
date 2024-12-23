package com.unimib.triviaducks.database;

import android.content.Context;
import com.unimib.triviaducks.util.Constants;

import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.util.Converter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main access point for the underlying connection to the local database.
 * <a href="https://developer.android.com/reference/kotlin/androidx/room/Database">...</a>
 */
@Database(entities = {Question.class}, version = Constants.DATABASE_VERSION, exportSchema = true)
// Annotazione Room per definire il database, specificando:
// - 'entities': Le entità che compongono le tabelle del database
// - 'version': La versione corrente del database
// - 'exportSchema': Permette di esportare lo schema del database per controlli e backup
@TypeConverters(Converter.class)
// Annotazione per specificare i converter usati per gestire tipi complessi (ad es., List<String>)

public abstract class QuestionRoomDatabase extends RoomDatabase {
    // Metodo astratto per ottenere l'implementazione dell'oggetto DAO
    public abstract QuestionDAO questionDao();

    // Instanza volatile del database per garantire la visibilità delle modifiche tra i thread
    private static volatile QuestionRoomDatabase INSTANCE;

    // ExecutorService per eseguire operazioni del database al di fuori del thread principale
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    // `Executors.newFixedThreadPool()` crea un pool di thread basato sul numero di CPU disponibili

    /**
     * Ritorna l'istanza singleton del database Room.
     * @param context Il contesto dell'applicazione
     * @return L'istanza del database
     */
    public static QuestionRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) { // Controlla se l'istanza del database è già creata
            synchronized (QuestionRoomDatabase.class) { // Blocca il thread per la sincronizzazione
                if (INSTANCE == null) { // Doppio controllo per sicurezza
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    QuestionRoomDatabase.class, Constants.SAVED_QUESTIONS_DATABASE)
                            .allowMainThreadQueries() // Permette query nel thread principale (NON consigliato in produzione)
                            .build(); // Costruisce il database
                }
            }
        }
        return INSTANCE; // Ritorna l'istanza del database
    }
}
