package com.unimib.triviaducks.database;

import static com.unimib.triviaducks.util.Constants.DATABASE_VERSION;

import android.content.Context;
import com.unimib.triviaducks.util.Constants;

import com.unimib.triviaducks.model.Question;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main access point for the underlying connection to the local database.
 * <a href="https://developer.android.com/reference/kotlin/androidx/room/Database">...</a>
 */
@Database(entities = {Question.class}, version = DATABASE_VERSION, exportSchema = true)
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
                                    QuestionRoomDatabase.class, Constants.SAVED_QUESTION_DATABASE)
                            .allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}