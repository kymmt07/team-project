package use_case.leaderboard;

import entity.HarvestRecord;

public interface LeaderboardRecordDataAccessInterface {
    /** Returns the player's record, or null if there is none yet. */
    HarvestRecord get(String username);

    void save(HarvestRecord record);
}