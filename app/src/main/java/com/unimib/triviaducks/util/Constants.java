package com.unimib.triviaducks.util;

import com.unimib.triviaducks.R;

import java.util.Arrays;
import java.util.List;

public class Constants {

    // Categories
    public static final String CATEGORY_ALL_CATEGORIES = "All Categories";
    public static final String CATEGORY_GEOGRAPHY = "Geography";
    public static final String CATEGORY_HISTORY = "History";
    public static final String CATEGORY_SCIENCE_NATURE = "Science & Nature";
    public static final String CATEGORY_SPORTS = "Sports";

    // API'S Category codes
    public static final int CODE_ANY_CATEGORIES = 0;
    public static final int CODE_GEOGRAPHY = 22;
    public static final int CODE_HISTORY = 23;
    public static final int CODE_SCIENCE_NATURE = 17;
    public static final int CODE_SPORTS = 21;

    // Is null/empty constants
    public static final String CONSTANT_IS_EMPTY = " is empty";
    public static final String CONSTANT_IS_NULL = " is null";

    // Difficulty constants
    public static final String DIFFICULTY_RANDOM = "";
    public static final String DIFFICULTY_EASY = "easy";
    public static final String DIFFICULTY_MEDIUM = "medium";
    public static final String DIFFICULTY_HARD = "hard";

    public static final String DRAWABLE = "drawable";

    // Costanti di errore
    public static final String ERROR = "Error: ";
    public static final String ERROR_CONNECTION = ERROR +"Internet not available. Check connection and then try again";
    public static final String ERROR_EMPTY_EMAIL = "Email"+CONSTANT_IS_EMPTY;
    public static final String ERROR_EMPTY_PASSWORD = "Password"+CONSTANT_IS_EMPTY;
    public static final String ERROR_FIREBASE_IMAGE_DATA_NULL = ERROR +"Image data from Firebase"+ CONSTANT_IS_NULL;
    public static final String ERROR_ID_TOKEN_NULL = ERROR +"idToken"+ CONSTANT_IS_NULL;
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid credentials";
    public static final String ERROR_INVALID_USER = "invalidUserError";
    public static final String ERROR_ITEM_IS_NULL = ERROR +"item"+ CONSTANT_IS_NULL;
    public static final String ERROR_LEADERBOARD_SET_IS_EMPTY = ERROR +"leaderboardSet"+ CONSTANT_IS_EMPTY;
    public static final String ERROR_LEADERBOARD_SET_IS_NULL = ERROR +"leaderboardSet"+ CONSTANT_IS_NULL;
    public static final String ERROR_CATEGORY_SET_IS_EMPTY = ERROR +"categorySet"+ CONSTANT_IS_EMPTY;
    public static final String ERROR_CATEGORY_SET_IS_NULL = ERROR +"categorySet"+ CONSTANT_IS_NULL;
    public static final String ERROR_LOADING_QUESTIONS = "Error loading questions";
    public static final String ERROR_NO_DATA_CATEGORY_FOUND = ERROR +"No data found for categories";
    public static final String ERROR_QUESTION_REPOSITORY_IS_NULL = ERROR +"QuestionRepository"+ CONSTANT_IS_NULL;
    public static final String ERROR_RETROFIT = "Retrofit Error";
    public static final String ERROR_SET_IS_EMPTY = ERROR +"The set"+ CONSTANT_IS_EMPTY;
    public static final String ERROR_UNEXPECTED = "Unexpected Error";
    public static final String ERROR_USER_COLLISION = "User collision error";
    public static final String ERROR_WEAK_PASSWORD = "Password is weak";

    // Firebase constants
    public static final String FIREBASE_REALTIME_DATABASE = "https://triviaducks-d160a-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";

    // Informations
    public static final String INFO_USER_ALREADY_IN_DB = "User already present in Firebase Realtime Database";
    public static final String INFO_USER_NOT_IN_DB = "User not present in Firebase Realtime Database";
    public static final String INFO_SUCCESS = "success";

    // Limits
    public static final int LIMIT_LEADERBOARD_ACCOUNT = 11;
    public static final int LIMIT_TOP_CATEGORIES = 3;

    //List category
    public static final List<String> LIST_CATEGORY = Arrays.asList(
            CATEGORY_ALL_CATEGORIES,
            CATEGORY_SCIENCE_NATURE,
            CATEGORY_GEOGRAPHY,
            CATEGORY_HISTORY,
            CATEGORY_SPORTS
    );

    //List difficulty
    public static final List<String> LIST_DIFFICULTY = Arrays.asList(
            DIFFICULTY_RANDOM,
            DIFFICULTY_EASY,
            DIFFICULTY_MEDIUM,
            DIFFICULTY_HARD
    );

    //List lotties_animations
    public static final List<Integer> LIST_LOTTIE_ANIMATIONS = Arrays.asList(
            R.raw.category_all,
            R.raw.category_science,
            R.raw.category_geography,
            R.raw.category_history,
            R.raw.category_sport
    );

    public static final String NULL = "null";

    // Game parameters
    public static final String PARAMETER_CAN_PLAY = "can_play";
    public static final String PARAMETER_CORRECT_ANSWER = "correct_answer";
    public static final String PARAMETER_END = "end";
    public static final String PARAMETER_QUIZ_FINISHED = "Quiz finished";
    public static final String PARAMETER_REASON = "reason";
    public static final String PARAMETER_SCORE = "score";
    public static final String PARAMETER_TIME_EXPIRED = "Time expired";
    public static final String PARAMETER_WRONG_ANSWER = "Wrong answer";

    public static final int PASSWORD_MINIMUM_LENGTH = 8;

    // Question points
    public static final int QUESTION_POINTS_EASY = 10;
    public static final int QUESTION_POINTS_MEDIUM = 25;
    public static final int QUESTION_POINTS_HARD = 50;

    // File di SharedPreferences
    public static final String SHARED_PREFERENCES_BEST_SCORE = "best_score";
    public static final String SHARED_PREFERENCES_FILENAME = "com.unimib.triviaducks.preferences";
    public static final String SHARED_PREFERENCES_IS_MUSIC_OFF = "is_music_off";
    public static final String SHARED_PREFERENCES_IS_NIGHT_MODE = "is_night_mode";
    public static final String SHARED_PREFERENCES_LEADERBOARD = "leaderboard";
    public static final String SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY = "match_played_by_category";
    public static final String SHARED_PREFERENCES_PROFILE_PICTURE = "profile_picture";
    public static final String SHARED_PREFERENCES_USERNAME = "username";
    public static final String SHARED_PREFERENCES_VOLUME = "volume";

    public static final String SPLIT_CHARACTER = ";";

    // Game TextViews default value
    public static final String TEXT_DIFFICULTY = "Difficulty: ";
    public static final String TEXT_QUESTION_NUMBER = "Question N. %d";
    public static final String TEXT_SCORE="Score: ";

    // Timer settings
    public static final int TIMER_COUNTDOWN_INTERVAL = 1000;
    public static final int TIMER_TIME = 30999;

    // Trivia API setup
    public static final String TRIVIA_API_BASE_URL = "https://opentdb.com/";
    public static final String TRIVIA_ENDPOINT = "api.php";
    public static final String TRIVIA_PARAMETER_AMOUNT = "amount";
    public static final String TRIVIA_PARAMETER_CATEGORY = "category";
    public static final String TRIVIA_PARAMETER_DIFFICULTY = "difficulty";
    public static final String TRIVIA_PARAMETER_TYPE = "type";
    public static final String TRIVIA_VALUE_TYPE = "multiple";

    //User Agent
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
    public static final String USER_AGENT_DESCRIPTION = "User-Agent";

    //Values ON and OFF
    public static final String VALUE_OFF = "OFF";
    public static final String VALUE_ON = "ON";

    // Warnings
    public static final String WARNING = "Warning: ";
    public static final String WARNING_CHECK_EMAIL = "Check your email address";
    public static final String WARNING_CONNECTION = WARNING+"You are offline, you can end the quiz, then you have to go online.";
    public static final String WARNING_CORRECT_ANSWER_NULL_OR_EMPTY = WARNING+"CorrectAnswer is null or empty";
    public static final String WARNING_LOADING_PROFILE_DATA = "Error loading profile data.";
    public static final String WARNING_NOT_REGISTERED = "User not registered";
    public static final String WARNING_OBTAINING_DIFFICULTY = WARNING+"Cannot obtain question difficulty";
    public static final String WARNING_SPLIT_CHAR_NOT_ALLOWED = SPLIT_CHARACTER + " is not an allowed character in username";
    public static final String WARNING_USERNAME_NOT_SELECTED = "Please enter a username";
    public static final String WARNING_USERNAME_TOO_LONG = "Please enter a shorter username";
    public static final String WARNING_SIGN_IN_WITH_CREDENTIAL_FAILURE = "signInWithCredential:failure";


    public static final String EMPTY_STRING = "";
}
