package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard.LeaderboardController;
import interface_adapter.leaderboard.LeaderboardState;
import interface_adapter.leaderboard.LeaderboardViewModel;
import interface_adapter.leaderboard.LeaderboardViewModelListener;
import interface_adapter.logged_in.LoggedInViewModel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

/**
 * The view for the Leaderboard use case.
 */
public class LeaderboardView extends JPanel implements LeaderboardViewModelListener {

    private final LeaderboardViewModel leaderboardViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private LeaderboardController leaderboardController;

    private final JLabel choicePromptLabel = new JLabel(
            "<html><div style='width:260px; text-align:center'>"
                    + LeaderboardViewModel.CHOICE_PROMPT + "</div></html>",
            SwingConstants.CENTER);
    private final JPanel rankPanel = new JPanel();
    private final JLabel ownCountLabel = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel messageLabel = new JLabel(" ", SwingConstants.CENTER);
    private final JButton participateButton =
            new JButton(LeaderboardViewModel.PARTICIPATE_BUTTON_LABEL);
    private final JButton viewOnlyButton =
            new JButton(LeaderboardViewModel.VIEW_ONLY_BUTTON_LABEL);
    private final JButton backButton = new JButton(LeaderboardViewModel.BACK_BUTTON_LABEL);

    public LeaderboardView(LeaderboardViewModel leaderboardViewModel,
                           LoggedInViewModel loggedInViewModel,
                           ViewManagerModel viewManagerModel) {
        this.leaderboardViewModel = leaderboardViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
        this.leaderboardViewModel.addListener(this);

        final JLabel title = new JLabel(LeaderboardViewModel.TITLE_LABEL, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));

        rankPanel.setLayout(new BoxLayout(rankPanel, BoxLayout.Y_AXIS));
        choicePromptLabel.setVisible(false);

        final JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));
        center.add(centre(choicePromptLabel));
        center.add(centre(rankPanel));
        center.add(centre(ownCountLabel));
        center.add(centre(statusLabel));
        center.add(centre(messageLabel));

        final JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(participateButton);
        buttons.add(viewOnlyButton);
        buttons.add(backButton);

        participateButton.addActionListener(
                evt -> leaderboardController.participate(currentUsername()));
        viewOnlyButton.addActionListener(
                evt -> leaderboardController.viewOnly(currentUsername()));
        backButton.addActionListener(evt -> {
            viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        });

        setPreferredSize(new Dimension(380, 320));
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    public void setLeaderboardController(LeaderboardController leaderboardController) {
        this.leaderboardController = leaderboardController;
    }

    @Override
    public void stateChanged() {
        final LeaderboardState state = leaderboardViewModel.getState();
        final boolean choiceNeeded = state.isChoiceNeeded();

        choicePromptLabel.setVisible(choiceNeeded);

        rankPanel.removeAll();
        if (!choiceNeeded) {
            final List<String> lines = state.getRankLines();
            if (lines.isEmpty()) {
                rankPanel.add(centre(new JLabel(LeaderboardViewModel.NO_ENTRIES_LABEL)));
            }
            for (String line : lines) {
                rankPanel.add(centre(new JLabel(line)));
            }
        }

        ownCountLabel.setText(choiceNeeded ? " " : state.getOwnCountText());
        if (choiceNeeded) {
            statusLabel.setText(" ");
        }
        else if (state.isParticipating()) {
            statusLabel.setText("You are on the leaderboard.");
        }
        else {
            statusLabel.setText("View only: your count is not shared.");
        }
        messageLabel.setText(state.getMessage().isEmpty() ? " " : state.getMessage());

        participateButton.setEnabled(choiceNeeded || !state.isParticipating());
        viewOnlyButton.setEnabled(choiceNeeded || state.isParticipating());

        revalidate();
        repaint();
    }

    private String currentUsername() {
        return loggedInViewModel.getState().getUsername();
    }

    private static JComponent centre(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        return component;
    }
}