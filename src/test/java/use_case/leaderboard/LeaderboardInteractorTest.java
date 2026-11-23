package use_case.leaderboard;

import entity.HarvestRecord;
import entity.LeaderboardEntry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeaderboardInteractorTest {

    // ---- fakes: they keep everything in memory, so no network is needed ----

    private static class FakeRecordDataAccess implements LeaderboardRecordDataAccessInterface {
        final Map<String, HarvestRecord> records = new LinkedHashMap<>();

        @Override
        public HarvestRecord get(String username) {
            return records.get(username);
        }

        @Override
        public void save(HarvestRecord record) {
            records.put(record.getUsername(), record);
        }
    }

    private static class FakeGateway implements LeaderboardGateway {
        final Map<String, LeaderboardEntry> entries = new LinkedHashMap<>();
        boolean failSubmit = false;
        boolean failRemove = false;
        boolean failGetAll = false;

        @Override
        public void submit(LeaderboardEntry entry) throws LeaderboardUnavailableException {
            if (failSubmit) {
                throw new LeaderboardUnavailableException("offline");
            }
            entries.put(entry.getUsername(), entry);
        }

        @Override
        public void remove(String username) throws LeaderboardUnavailableException {
            if (failRemove) {
                throw new LeaderboardUnavailableException("offline");
            }
            entries.remove(username);
        }

        @Override
        public List<LeaderboardEntry> getAll() throws LeaderboardUnavailableException {
            if (failGetAll) {
                throw new LeaderboardUnavailableException("offline");
            }
            return new ArrayList<>(entries.values());
        }
    }

    private static class FakePresenter implements LeaderboardOutputBoundary {
        boolean choiceNeeded = false;
        LeaderboardOutputData output = null;

        @Override
        public void prepareChoiceNeededView() {
            choiceNeeded = true;
        }

        @Override
        public void prepareSuccessView(LeaderboardOutputData outputData) {
            output = outputData;
        }
    }

    // ---- helpers ----

    private final FakeRecordDataAccess records = new FakeRecordDataAccess();
    private final FakeGateway gateway = new FakeGateway();
    private final FakePresenter presenter = new FakePresenter();
    private final LeaderboardInteractor interactor =
            new LeaderboardInteractor(records, gateway, presenter);

    private void putOnline(String username, int count) {
        gateway.entries.put(username, new LeaderboardEntry(username, count));
    }

    private void givenRecord(String username, int count, String visibility) {
        records.save(new HarvestRecord(username, count, visibility));
    }

    // ---- tests ----

    @Test
    void asksForChoiceWhenPlayerHasNotChosen() {
        interactor.execute(new LeaderboardInputData("tiffany", null));

        assertTrue(presenter.choiceNeeded);
        assertNull(presenter.output);
        assertTrue(gateway.entries.isEmpty());
    }

    @Test
    void firstTimePublicChoiceIsSavedAndUploaded() {
        interactor.execute(new LeaderboardInputData("tiffany", HarvestRecord.PUBLIC));

        assertEquals(HarvestRecord.PUBLIC, records.get("tiffany").getVisibility());
        assertEquals(0, gateway.entries.get("tiffany").getHarvestCount());
        assertNotNull(presenter.output);
        assertEquals("", presenter.output.getMessage());
    }

    @Test
    void firstTimePrivateChoiceIsSavedButNotUploaded() {
        interactor.execute(new LeaderboardInputData("tiffany", HarvestRecord.PRIVATE));

        assertEquals(HarvestRecord.PRIVATE, records.get("tiffany").getVisibility());
        assertTrue(gateway.entries.isEmpty());
        assertNotNull(presenter.output);
    }

    @Test
    void publicPlayerUploadsOwnCountEveryTime() {
        givenRecord("tiffany", 4, HarvestRecord.PUBLIC);

        interactor.execute(new LeaderboardInputData("tiffany", null));

        assertEquals(4, gateway.entries.get("tiffany").getHarvestCount());
        assertEquals(4, presenter.output.getOwnHarvestCount());
    }

    @Test
    void privatePlayerDoesNotUpload() {
        givenRecord("tiffany", 4, HarvestRecord.PRIVATE);

        interactor.execute(new LeaderboardInputData("tiffany", null));

        assertFalse(gateway.entries.containsKey("tiffany"));
        assertEquals(HarvestRecord.PRIVATE, presenter.output.getOwnVisibility());
        assertEquals(4, presenter.output.getOwnHarvestCount());
    }

    @Test
    void showsTopThreeSortedByCountThenUsername() {
        givenRecord("tiffany", 0, HarvestRecord.PRIVATE);
        putOnline("carol", 5);
        putOnline("alice", 5);
        putOnline("bob", 9);
        putOnline("dave", 1);
        putOnline("erin", 3);

        interactor.execute(new LeaderboardInputData("tiffany", null));

        final List<LeaderboardEntry> top = presenter.output.getTopEntries();
        assertEquals(3, top.size());
        assertEquals("bob", top.get(0).getUsername());
        assertEquals("alice", top.get(1).getUsername());
        assertEquals("carol", top.get(2).getUsername());
    }

    @Test
    void showsEveryoneWhenThereAreFewerThanThree() {
        givenRecord("tiffany", 0, HarvestRecord.PRIVATE);
        putOnline("bob", 2);

        interactor.execute(new LeaderboardInputData("tiffany", null));

        assertEquals(1, presenter.output.getTopEntries().size());
    }

    @Test
    void switchingFromPublicToPrivateRemovesTheOnlineEntry() {
        givenRecord("tiffany", 3, HarvestRecord.PUBLIC);
        putOnline("tiffany", 3);

        interactor.execute(new LeaderboardInputData("tiffany", HarvestRecord.PRIVATE));

        assertFalse(gateway.entries.containsKey("tiffany"));
        assertEquals(HarvestRecord.PRIVATE, records.get("tiffany").getVisibility());
        assertTrue(presenter.output.getTopEntries().isEmpty());
    }

    @Test
    void ifRemovingFailsThePlayerStaysPublicAndSeesAMessage() {
        givenRecord("tiffany", 3, HarvestRecord.PUBLIC);
        putOnline("tiffany", 3);
        gateway.failRemove = true;

        interactor.execute(new LeaderboardInputData("tiffany", HarvestRecord.PRIVATE));

        assertEquals(HarvestRecord.PUBLIC, records.get("tiffany").getVisibility());
        assertTrue(presenter.output.getMessage().contains("Could not remove"));
    }

    @Test
    void whenTheServiceIsUnreachableThePlayerStillSeesOwnCountAndAMessage() {
        givenRecord("tiffany", 2, HarvestRecord.PUBLIC);
        gateway.failSubmit = true;
        gateway.failGetAll = true;

        interactor.execute(new LeaderboardInputData("tiffany", null));

        assertTrue(presenter.output.getTopEntries().isEmpty());
        assertEquals(2, presenter.output.getOwnHarvestCount());
        assertTrue(presenter.output.getMessage().contains("Could not"));
    }
}