package use_case.leaderboard;

import entity.HarvestRecord;
import entity.LeaderboardEntry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardInteractor implements LeaderboardInputBoundary {
    private static final int TOP_COUNT = 3;

    private final LeaderboardRecordDataAccessInterface recordDataAccess;
    private final LeaderboardGateway gateway;
    private final LeaderboardOutputBoundary presenter;

    public LeaderboardInteractor(LeaderboardRecordDataAccessInterface recordDataAccess,
                                 LeaderboardGateway gateway,
                                 LeaderboardOutputBoundary presenter) {
        this.recordDataAccess = recordDataAccess;
        this.gateway = gateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(LeaderboardInputData inputData) {
        final String username = inputData.getUsername();
        final String requested = inputData.getRequestedVisibility();
        final List<String> messages = new ArrayList<>();

        HarvestRecord record = recordDataAccess.get(username);
        if (record == null) {
            record = new HarvestRecord(username);
        }

        if (requested != null && !requested.equals(record.getVisibility())) {
            changeVisibility(record, requested, messages);
        }

        if (HarvestRecord.UNSET.equals(record.getVisibility())) {
            presenter.prepareChoiceNeededView();
            return;
        }

        if (HarvestRecord.PUBLIC.equals(record.getVisibility())) {
            try {
                gateway.submit(new LeaderboardEntry(username, record.getHarvestCount()));
            }
            catch (LeaderboardUnavailableException ex) {
                messages.add("Could not upload your count right now.");
            }
        }

        List<LeaderboardEntry> top = new ArrayList<>();
        try {
            top = topEntries(gateway.getAll());
        }
        catch (LeaderboardUnavailableException ex) {
            messages.add("Could not reach the online leaderboard.");
        }

        presenter.prepareSuccessView(new LeaderboardOutputData(
                top, record.getHarvestCount(), record.getVisibility(),
                String.join(" ", messages)));
    }

    private void changeVisibility(HarvestRecord record, String requested, List<String> messages) {
        if (HarvestRecord.PRIVATE.equals(requested)
                && HarvestRecord.PUBLIC.equals(record.getVisibility())) {
            try {
                gateway.remove(record.getUsername());
            }
            catch (LeaderboardUnavailableException ex) {
                messages.add("Could not remove your entry, so your count stays public.");
                return;
            }
        }
        record.setVisibility(requested);
        recordDataAccess.save(record);
    }

    private List<LeaderboardEntry> topEntries(List<LeaderboardEntry> all) {
        final List<LeaderboardEntry> sorted = new ArrayList<>(all);
        sorted.sort(Comparator.comparingInt(LeaderboardEntry::getHarvestCount).reversed()
                .thenComparing(LeaderboardEntry::getUsername));
        return new ArrayList<>(sorted.subList(0, Math.min(TOP_COUNT, sorted.size())));
    }
}