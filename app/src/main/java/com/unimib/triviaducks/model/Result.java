package com.unimib.triviaducks.model;

import java.util.List;

public abstract class Result {
    private Result() {}

    public boolean isSuccess() {
        return !(this instanceof Error);
    }

    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class QuestionSuccess extends Result {
        private final QuestionAPIResponse questionAPIResponse;
        public QuestionSuccess(QuestionAPIResponse questionAPIResponse) {
            this.questionAPIResponse = questionAPIResponse;
        }
        public QuestionAPIResponse getData() {
            return questionAPIResponse;
        }
    }

    public static final class UserSuccess extends Result {
        private final User user;
        public UserSuccess(User user) {
            this.user = user;
        }
        public User getData() {
            return user;
        }
    }

    public static final class UserDataSuccess extends Result {
        private final UserData userData;
        public UserDataSuccess(UserData userData) {
            this.userData = userData;
        }
        public UserData getData() {
            return userData;
        }
    }

    public static final class LeaderboardSuccess extends Result {
        private final List<User> leaderboard;
        public LeaderboardSuccess(List<User> leaderboard) {
            this.leaderboard = leaderboard;
        }
        public List<User> getData() {
            return leaderboard;
        }
    }

    /**
     * Class that represents an error occurred during the interaction
     * with a Web Service or a local database.
     */
    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}