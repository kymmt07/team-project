package entity;

/**
 * One line on the leaderboard: a username and its harvest count.
 */
public class LeaderboardEntry {

    private final String username;
    private final int harvestCount;

    public LeaderboardEntry(String username, int harvestCount) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        this.username = username;
        this.harvestCount = harvestCount;
    }

    public String getUsername() {
        return username;
    }

    public int getHarvestCount() {
        return harvestCount;
    }
}