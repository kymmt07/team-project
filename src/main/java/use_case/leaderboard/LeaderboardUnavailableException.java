package use_case.leaderboard;

/**
 * Thrown when the online leaderboard cannot be reached or gives an unusable answer.
 */
public class LeaderboardUnavailableException extends Exception {
    public LeaderboardUnavailableException(String message) {
        super(message);
    }

    public LeaderboardUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}