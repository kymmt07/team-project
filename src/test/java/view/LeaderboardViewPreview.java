package view;

import entity.HarvestRecord;
import entity.LeaderboardEntry;
import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard.LeaderboardController;
import interface_adapter.leaderboard.LeaderboardPresenter;
import interface_adapter.leaderboard.LeaderboardViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.leaderboard.LeaderboardGateway;
import use_case.leaderboard.LeaderboardInteractor;
import use_case.leaderboard.LeaderboardRecordDataAccessInterface;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Run this main to look at the leaderboard window without logging in.
 * Everything is kept in memory: no file is written and no network is used.
 */
public class LeaderboardViewPreview {

    public static void main(String[] args) {
        final Map<String, HarvestRecord> records = new HashMap<>();
        final Map<String, LeaderboardEntry> online = new LinkedHashMap<>();
        online.put("alice", new LeaderboardEntry("alice", 5));
        online.put("bob", new LeaderboardEntry("bob", 3));
        online.put("carol", new LeaderboardEntry("carol", 8));
        online.put("dave", new LeaderboardEntry("dave", 1));

        final LeaderboardRecordDataAccessInterface recordAccess =
                new LeaderboardRecordDataAccessInterface() {
                    @Override
                    public HarvestRecord get(String username) {
                        return records.get(username);
                    }

                    @Override
                    public void save(HarvestRecord record) {
                        records.put(record.getUsername(), record);
                    }
                };

        final LeaderboardGateway gateway = new LeaderboardGateway() {
            @Override
            public void submit(LeaderboardEntry entry) {
                online.put(entry.getUsername(), entry);
            }

            @Override
            public void remove(String username) {
                online.remove(username);
            }

            @Override
            public List<LeaderboardEntry> getAll() {
                return new ArrayList<>(online.values());
            }
        };

        final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        loggedInViewModel.getState().setUsername("tiffany");
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final LeaderboardViewModel leaderboardViewModel = new LeaderboardViewModel();

        final LeaderboardController controller = new LeaderboardController(
                new LeaderboardInteractor(recordAccess, gateway,
                        new LeaderboardPresenter(leaderboardViewModel, viewManagerModel)));

        final LeaderboardView leaderboardView =
                new LeaderboardView(leaderboardViewModel, loggedInViewModel, viewManagerModel);
        leaderboardView.setLeaderboardController(controller);

        final JPanel home = new JPanel();
        home.add(new JLabel("(pretend this is the farm)"));
        final JButton open = new JButton("Leaderboard");
        open.addActionListener(evt -> controller.viewLeaderboard("tiffany"));
        home.add(open);

        final CardLayout cardLayout = new CardLayout();
        final JPanel cards = new JPanel(cardLayout);
        cards.add(home, LoggedInViewModel.VIEW_NAME);
        cards.add(leaderboardView, LeaderboardViewModel.VIEW_NAME);
        new ViewManager(cardLayout, cards, viewManagerModel);

        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new JFrame("Leaderboard preview");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.add(cards);
            viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}