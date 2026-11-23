package interface_adapter.leaderboard;

import entity.HarvestRecord;
import entity.LeaderboardEntry;
import interface_adapter.ViewManagerModel;
import use_case.leaderboard.LeaderboardOutputBoundary;
import use_case.leaderboard.LeaderboardOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * The presenter for the Leaderboard use case.
 */
public class LeaderboardPresenter implements LeaderboardOutputBoundary {

    private final LeaderboardViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public LeaderboardPresenter(LeaderboardViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareChoiceNeededView() {
        final LeaderboardState state = viewModel.getState();
        state.setChoiceNeeded(true);
        state.setMessage("");
        viewModel.notifyStateChanged();
        showLeaderboardView();
    }

    @Override
    public void prepareSuccessView(LeaderboardOutputData outputData) {
        final List<String> lines = new ArrayList<>();
        int rank = 1;
        for (LeaderboardEntry entry : outputData.getTopEntries()) {
            lines.add(rank + ". " + entry.getUsername() + " - " + entry.getHarvestCount());
            rank++;
        }

        final LeaderboardState state = viewModel.getState();
        state.setRankLines(lines);
        state.setOwnCountText("Your harvest count: " + outputData.getOwnHarvestCount());
        state.setParticipating(HarvestRecord.PUBLIC.equals(outputData.getOwnVisibility()));
        state.setChoiceNeeded(false);
        state.setMessage(outputData.getMessage());
        viewModel.notifyStateChanged();
        showLeaderboardView();
    }

    private void showLeaderboardView() {
        viewManagerModel.setState(LeaderboardViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}