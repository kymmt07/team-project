package interface_adapter.leaderboard;

import entity.HarvestRecord;
import org.junit.jupiter.api.Test;
import use_case.leaderboard.LeaderboardInputBoundary;
import use_case.leaderboard.LeaderboardInputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LeaderboardControllerTest {

    private static class CapturingBoundary implements LeaderboardInputBoundary {
        LeaderboardInputData last;

        @Override
        public void execute(LeaderboardInputData inputData) {
            last = inputData;
        }
    }

    private final CapturingBoundary boundary = new CapturingBoundary();
    private final LeaderboardController controller = new LeaderboardController(boundary);

    @Test
    void viewLeaderboardKeepsTheCurrentChoice() {
        controller.viewLeaderboard("tiffany");

        assertEquals("tiffany", boundary.last.getUsername());
        assertNull(boundary.last.getRequestedVisibility());
    }

    @Test
    void participateRequestsPublic() {
        controller.participate("tiffany");

        assertEquals(HarvestRecord.PUBLIC, boundary.last.getRequestedVisibility());
    }

    @Test
    void viewOnlyRequestsPrivate() {
        controller.viewOnly("tiffany");

        assertEquals(HarvestRecord.PRIVATE, boundary.last.getRequestedVisibility());
    }
}