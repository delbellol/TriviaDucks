package com.unimib.triviaducks.database;

import android.content.Context;
import com.unimib.triviaducks.util.Constants;

import com.unimib.triviaducks.model.Question;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Main access point for the underlying connection to the local database.
 * <a href="https://developer.android.com/reference/kotlin/androidx/room/Database">...</a>
 */
@Database(entities = {Question.class}, version = Constants.DATABASE_VERSION, exportSchema = true)
public abstract class QuestionRoomDatabase extends RoomDatabase {

    public abstract QuestionDAO newsDao();

    private static volatile QuestionRoomDatabase INSTANCE;

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