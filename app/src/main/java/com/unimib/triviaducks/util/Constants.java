package com.unimib.triviaducks.util;

import com.unimib.triviaducks.R;

import java.util.Arrays;
import java.util.List;

public class Constants {

    // COSTANTI PER LA TRIVIA API
    // Difficoltà
    public static final String EASY_DIFFICULTY = "Easy";
    public static final String MEDIUM_DIFFICULTY = "Medium";
    public static final String HARD_DIFFICULTY = "Hard";

    // Categorie
    public static final String GENERAL_KNOWLEDGE = "General Knowledge";
    public static final String BOOKS = "Entertainment: Books";
    public static final String FILM = "Entertainment: Film";
    public static final String MUSIC = "Entertainment: Music";
    public static final String MUSICAlS_THEATRES = "Entertainment: Musical & Theatres";
    public static final String TELEVISION = "Entertainment: Television";
    public static final String VIDEO_GAMES = "Entertainment: Video Games";
    public static final String BOARD_GAMES = "Entertainment: Board Games";
    public static final String SCIENCE_NATURE = "Science & Nature";
    public static final String COMPUTERS = "Science: Computers";
    public static final String MATHEMATICS = "Science: Mathematics";
    public static final String MYTHOLOGY = "Mythology";
    public static final String SPORTS = "Sports";
    public static final String GEOGRAPHY = "Geography";
    public static final String HISTORY = "History";
    public static final String POLITICS = "Politics";
    public static final String ART = "Art";
    public static final String CELEBRITIES = "Celebrities";
    public static final String ANIMALS = "Animals";
    public static final String VEHICLES = "Vehicles";
    public static final String COMICS = "Entertainment: Comics";
    public static final String GADGETS = "Science: Gadgets";
    public static final String ANIME_MANGA = "Entertainment: Japanese Anime & Manga";
    public static final String CARTOON = "Entertainment: Cartoon & Animations";

    public static final String CATEGORY = "category";

    //Codice per le categorie dell'API
    public static final int ANY_CATEGORIES_CODE = 0;
    public static final int HISTORY_CODE = 23;
    public static final int SCIENCE_NATURE_CODE = 17;
    public static final int GEOGRAPHY_CODE = 22;
    public static final int SPORTS_CODE = 21;

    // Liste di difficoltà e categorie
    public static final List<String> DIFFICULTY = Arrays.asList(
            EASY_DIFFICULTY, MEDIUM_DIFFICULTY, HARD_DIFFICULTY
    );

    public static final List<String> CATEGORIES = Arrays.asList(
            GENERAL_KNOWLEDGE, BOOKS, FILM, MUSIC, MUSICAlS_THEATRES, TELEVISION, VIDEO_GAMES,
            BOARD_GAMES, SCIENCE_NATURE, COMPUTERS, MATHEMATICS, MYTHOLOGY, SPORTS, GEOGRAPHY,
            HISTORY, POLITICS, ART, CELEBRITIES, ANIMALS, VEHICLES, COMICS, GADGETS, ANIME_MANGA, CARTOON
    );

    // Liste di nomi delle categorie
    public static final List<Integer> CATEGORIES_NAMES = Arrays.asList(
            R.string.general_knowledge, R.string.books, R.string.film, R.string.music, R.string.musical,
            R.string.television, R.string.video_games, R.string.board_games, R.string.science_nature,
            R.string.computers, R.string.mathematics, R.string.mythology, R.string.sports, R.string.geography,
            R.string.history, R.string.politics, R.string.art, R.string.celebrities, R.string.animals,
            R.string.vehicles, R.string.comics, R.string.gadgets, R.string.anime, R.string.cartoon
    );

    // File JSON di esempio
    public static final String SAMPLE_JSON_FILENAME = "sample_api_response.json";

    // Versione del database
    public static final int DATABASE_VERSION = 1;
    public static final String SAVED_QUESTIONS_DATABASE = "saved_db";

    // Impostazioni timer
    public static final int TIMER_TIME = 30999;
    public static final int COUNTDOWN_INTERVAL = 1000;

    // Timeout per la freschezza dei dati
    public static final int FRESH_TIMEOUT = 1000 * 60;

    // Configurazione API Trivia
    public static final String TRIVIA_API_BASE_URL = "https://opentdb.com/";
    public static final String TRIVIA_ENDPOINT = "api.php";
    public static final String TRIVIA_AMOUNT_PARAMETER = "amount";
    public static final String TRIVIA_TYPE_PARAMETER = "type";
    public static final String TRIVIA_CATEGORY_PARAMETER = "category";
    public static final int TRIVIA_AMOUNT_VALUE = 50;
    public static final String TRIVIA_TYPE_VALUE = "multiple";

    // File di SharedPreferences
    public static final String SHARED_PREFERENCES_FILENAME = "com.unimib.triviaducks.preferences";
    public static final String SHARED_PREFERENCES_LAST_UPDATE = "last_update";

    // Costanti di errore
    public static final String REMOVED_QUESTION = "[Removed]";
    public static final String RETROFIT_ERROR = "retrofit_error";
    public static final String UNEXPECTED_ERROR = "unexpected_error";
}
