package use_case.leaderboard;

public interface LeaderboardOutputBoundary {
    /** The player has not chosen public or private yet, so the view must ask. */
    void prepareChoiceNeededView();

    void prepareSuccessView(LeaderboardOutputData outputData);
}