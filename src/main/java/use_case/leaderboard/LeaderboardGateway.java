package use_case.leaderboard;

import entity.LeaderboardEntry;

import java.util.List;

public interface LeaderboardGateway {
    /** Adds the entry, replacing any earlier entry with the same username. */
    void submit(LeaderboardEntry entry) throws LeaderboardUnavailableException;

    void remove(String username) throws LeaderboardUnavailableException;

    List<LeaderboardEntry> getAll() throws LeaderboardUnavailableException;
}