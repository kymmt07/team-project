package interface_adapter.leaderboard;

import entity.HarvestRecord;
import use_case.leaderboard.LeaderboardInputBoundary;
import use_case.leaderboard.LeaderboardInputData;

/**
 * The controller for the Leaderboard use case.
 */
public class LeaderboardController {

    private final LeaderboardInputBoundary interactor;

    public LeaderboardController(LeaderboardInputBoundary interactor) {
        this.interactor = interactor;
    }

    /** Opens the leaderboard and keeps the player's current choice. */
    public void viewLeaderboard(String username) {
        execute(username, null);
    }

    /** The player chooses to appear on the leaderboard. */
    public void participate(String username) {
        execute(username, HarvestRecord.PUBLIC);
    }

    /** The player chooses to only look at the leaderboard. */
    public void viewOnly(String username) {
        execute(username, HarvestRecord.PRIVATE);
    }

    private void execute(String username, String requestedVisibility) {
        interactor.execute(new LeaderboardInputData(username, requestedVisibility));
    }
}