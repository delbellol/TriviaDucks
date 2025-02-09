package com.unimib.triviaducks.util;

import com.unimib.triviaducks.R;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final int MINIMUM_LENGTH_PASSWORD = 8;

    // Categorie
    public static final String ALL_CATEGORIES = "All Categories";
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

    public static final String DIFFICULTY = "difficulty";

    //Codice per le categorie dell'API
    public static final int ANY_CATEGORIES_CODE = 0;
    public static final int HISTORY_CODE = 23;
    public static final int SCIENCE_NATURE_CODE = 17;
    public static final int GEOGRAPHY_CODE = 22;
    public static final int SPORTS_CODE = 21;

    // Versione del database
    public static final int DATABASE_VERSION = 1;
    public static final String SAVED_QUESTIONS_DATABASE = "saved_db";

    // Impostazioni timer
    public static final int TIMER_TIME = 30999;
    public static final int COUNTDOWN_INTERVAL = 1000;

    // Timeout per la freschezza dei dati
    public static final int FRESH_TIMEOUT = 1000;

    // Configurazione API Trivia
    public static final String TRIVIA_API_BASE_URL = "https://opentdb.com/";
    public static final String TRIVIA_ENDPOINT = "api.php";
    public static final String TRIVIA_AMOUNT_PARAMETER = "amount";
    public static final String TRIVIA_TYPE_PARAMETER = "type";
    public static final String TRIVIA_CATEGORY_PARAMETER = "category";
    public static final int TRIVIA_AMOUNT_VALUE = 50;
    public static final String TRIVIA_TYPE_VALUE = "multiple";
    public static final String TRIVIA_DIFFICULTY_PARAMETER = "difficulty";

    // File di SharedPreferences
    public static final String SHARED_PREFERENCES_FILENAME = "com.unimib.triviaducks.preferences";
    public static final String SHARED_PREFERENCES_USERNAME = "username";
    public static final String SHARED_PREFERENCES_PROFILE_PICTURE = "profile_picture";
    public static final String SHARED_PREFERENCES_VOLUME = "volume";
    public static final String SHARED_PREFERENCES_IS_MUSIC_OFF = "is_music_off";
    public static final String SHARED_PREFERENCES_IS_NIGHT_MODE = "is_night_mode";
    public static final String SHARED_PREFERENCES_BEST_SCORE = "best_score";
    public static final String SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY = "match_played_by_category";
    public static final String SHARED_PREFERENCES_LEADERBOARD = "leaderboard";

    // Costanti di errore
    public static final String GENERIC_ERROR = "Error: ";
    public static final String WARNING = "Warning: ";
    public static final String IS_NULL = " is null";
    public static final String IS_EMPTY = " is empty";
    public static final String REMOVED_QUESTION = "[Removed]";
    public static final String RETROFIT_ERROR = "Retrofit Error";
    public static final String UNEXPECTED_ERROR = "Unexpected Error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "Invalid credentials";
    public static final String USER_COLLISION_ERROR = "User collision error";
    public static final String WEAK_PASSWORD_ERROR = "Password is weak";
    public static final String CONNECTION_WARNING_TEXT = WARNING+"You are offline, you can end the quiz, then you have to go online.";
    public static final String CONNECTION_ERROR_TEXT = GENERIC_ERROR+"Internet not available. Check connection and then try again";
    public static final String ITEM_IS_NULL = GENERIC_ERROR+"item"+IS_NULL;
    public static final String LEADERBOARD_SET_IS_NULL = GENERIC_ERROR+"leaderboardSet"+IS_NULL;
    public static final String LEADERBOARD_SET_IS_EMPTY = GENERIC_ERROR+"leaderboardSet"+IS_EMPTY;
    public static final String FIREBASE_IMAGE_DATA_NULL = GENERIC_ERROR+"Image data from Firebase"+IS_NULL;
    public static final String NO_DATA_CATEGORY_FOUND = GENERIC_ERROR+"No data found for categories";
    public static final String CORRECT_ANSWER_NULL_OR_EMPTY = WARNING+"CorrectAnswer is null or empty";
    public static final String QUESTION_REPOSITORY_IS_NULL = GENERIC_ERROR+"QuestionRepository"+IS_NULL;
    public static final String ERROR_LOADING_QUESTIONS = "Error loading questions";
    public static final String WARNING_OBTAING_DIFFICULTY = WARNING+"Cannot obtain question difficulty";
    public static final String WARNING_LOADING_PROFILE_DATA = "Error loading profile data.";
    public static final String ERROR_SET_IS_EMPTY = GENERIC_ERROR+"The set"+IS_EMPTY;
    public static final String WARNING_NOT_REGISTERED = "User not registered";
    public static final String WARNING_CHECK_EMAIL = "Check your email address";
    public static final String ERROR_ID_TOKEN_NULL = GENERIC_ERROR+"idToken"+IS_NULL;

    public static final String USER_ALREADY_IN_DB = "User already present in Firebase Realtime Database";
    public static final String USER_NOT_IN_DB = "User not present in Firebase Realtime Database";

    public static final String FIREBASE_REALTIME_DATABASE = "https://triviaducks-d160a-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";

    public static final String SUCCESS = "success";

    public static final int EASY_QUESTION_POINTS = 10;
    public static final int MEDIUM_QUESTION_POINTS = 25;
    public static final int HARD_QUESTION_POINTS = 50;

    public static final String REASON = "reason";
    public static final String SCORE = "score";
    public static final String CORRECT_ANSWER = "correct_answer";
    public static final String END = "end";
    public static final String WRONG_ANSWER = "Wrong answer";
    public static final String TIME_EXPIRED = "Time expired";
    public static final String QUIZ_FINISHED = "Quiz finished";

    public static final String CAN_PLAY = "can_play";

    public static final String DRAWABLE = "drawable";

    public static final String SPLIT_CHARACTER = ";";
    public static final String NULL = "null";

    public static final String TEXT_SCORE="Score: ";
    public static final String QUESTION_NUMBER = "Question N. %d";
    public static final String TEXT_DIFFICULTY = "Difficulty: ";

    public static final String DIFFICULTY_RANDOM = "";
    public static final String DIFFICULY_EASY = "easy";
    public static final String DIFFICULTY_MEDIUM = "medium";
    public static final String DIFFICULTY_HARD = "hard";

    public static final List<String> LIST_DIFFICULTY = Arrays.asList(
            DIFFICULTY_RANDOM,
            DIFFICULY_EASY,
            DIFFICULTY_MEDIUM,
            DIFFICULTY_HARD
    );

    public static final List<Integer> LIST_LOTTIE_ANIMATIONS = Arrays.asList(
            R.raw.category_all,
            R.raw.category_science,
            R.raw.category_geography,
            R.raw.category_history,
            R.raw.category_sport
    );

    public static final List<String> LIST_CATEGORY = Arrays.asList(
            ALL_CATEGORIES,
            SCIENCE_NATURE,
            GEOGRAPHY,
            HISTORY,
            SPORTS
    );

    public static final String USER_AGENT_DESCRIPTION = "User-Agent";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

    public static final String MUSIC_ON = "ON";
    public static final String MUSIC_OFF = "OFF";

    public static final int LEADERBOARD_ACCOUNT_LIMIT = 11;
    public static final int TOP_CATEGORIES_LIMIT = 3;

    public static final String USERNAME_NOT_SELECTED  = "Please enter a username";
    public static final String USERNAME_TOO_LONG  = "Please enter a shorter username";
    public static final String SPLIT_CHAR_NOT_ALLOWED = SPLIT_CHARACTER + " is not an allowed character in username";
}
