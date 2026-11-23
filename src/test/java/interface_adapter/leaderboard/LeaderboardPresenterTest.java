package interface_adapter.leaderboard;

import entity.HarvestRecord;
import entity.LeaderboardEntry;
import interface_adapter.ViewManagerModel;
import org.junit.jupiter.api.Test;
import use_case.leaderboard.LeaderboardOutputData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeaderboardPresenterTest {

    private final LeaderboardViewModel viewModel = new LeaderboardViewModel();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final LeaderboardPresenter presenter =
            new LeaderboardPresenter(viewModel, viewManagerModel);

    @Test
    void successViewFormatsTheEntriesNotifiesAndShowsTheLeaderboard() {
        final int[] notified = {0};
        viewModel.addListener(() -> notified[0]++);
        final List<LeaderboardEntry> top = Arrays.asList(
                new LeaderboardEntry("bob", 9), new LeaderboardEntry("alice", 5));

        presenter.prepareSuccessView(new LeaderboardOutputData(top, 3, HarvestRecord.PUBLIC, ""));

        final LeaderboardState state = viewModel.getState();
        assertEquals(Arrays.asList("1. bob - 9", "2. alice - 5"), state.getRankLines());
        assertEquals("Your harvest count: 3", state.getOwnCountText());
        assertTrue(state.isParticipating());
        assertFalse(state.isChoiceNeeded());
        assertEquals("", state.getMessage());
        assertEquals(1, notified[0]);
        assertEquals(LeaderboardViewModel.VIEW_NAME, viewManagerModel.getState());
    }

    @Test
    void aPrivatePlayerIsNotMarkedAsParticipating() {
        presenter.prepareSuccessView(new LeaderboardOutputData(
                Collections.emptyList(), 0, HarvestRecord.PRIVATE, ""));

        assertFalse(viewModel.getState().isParticipating());
    }

    @Test
    void theInteractorMessageIsPassedOn() {
        presenter.prepareSuccessView(new LeaderboardOutputData(
                Collections.emptyList(), 0, HarvestRecord.PUBLIC,
                "Could not reach the online leaderboard."));

        assertEquals("Could not reach the online leaderboard.", viewModel.getState().getMessage());
    }

    @Test
    void choiceNeededSetsTheFlagAndShowsTheLeaderboard() {
        presenter.prepareChoiceNeededView();

        assertTrue(viewModel.getState().isChoiceNeeded());
        assertEquals(LeaderboardViewModel.VIEW_NAME, viewManagerModel.getState());
    }
}