package use_case.leaderboard;

import entity.HarvestRecord;

/**
 * The input of the leaderboard use case.
 * requestedVisibility is null when the player has not chosen or changed anything.
 */
public class LeaderboardInputData {
    private final String username;
    private final String requestedVisibility;

    public LeaderboardInputData(String username, String requestedVisibility) {
        if (requestedVisibility != null
                && !HarvestRecord.PUBLIC.equals(requestedVisibility)
                && !HarvestRecord.PRIVATE.equals(requestedVisibility)) {
            throw new IllegalArgumentException("Requested visibility must be public or private");
        }
        this.username = username;
        this.requestedVisibility = requestedVisibility;
    }

    public String getUsername() {
        return username;
    }

    public String getRequestedVisibility() {
        return requestedVisibility;
    }
}