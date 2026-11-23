package use_case.leaderboard;

import entity.LeaderboardEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The result of the leaderboard use case.
 * message is an empty string when everything went well.
 */
public class LeaderboardOutputData {
    private final List<LeaderboardEntry> topEntries;
    private final int ownHarvestCount;
    private final String ownVisibility;
    private final String message;

    public LeaderboardOutputData(List<LeaderboardEntry> topEntries, int ownHarvestCount,
                                 String ownVisibility, String message) {
        this.topEntries = Collections.unmodifiableList(new ArrayList<>(topEntries));
        this.ownHarvestCount = ownHarvestCount;
        this.ownVisibility = ownVisibility;
        this.message = message;
    }

    public List<LeaderboardEntry> getTopEntries() {
        return topEntries;
    }

    public int getOwnHarvestCount() {
        return ownHarvestCount;
    }

    public String getOwnVisibility() {
        return ownVisibility;
    }

    public String getMessage() {
        return message;
    }
}