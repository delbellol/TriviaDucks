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
@TypeConverters(Converter.class)
public abstract class QuestionRoomDatabase extends RoomDatabase {

    public abstract QuestionDAO questionDao();

    private static volatile QuestionRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static QuestionRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuestionRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    QuestionRoomDatabase.class, Constants.SAVED_ARTICLES_DATABASE)
                            .allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}