package interface_adapter.leaderboard;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardViewModel {

    public static final String VIEW_NAME = "leaderboard";
    public static final String TITLE_LABEL = "Leaderboard";
    public static final String PARTICIPATE_BUTTON_LABEL = "Participate";
    public static final String VIEW_ONLY_BUTTON_LABEL = "View only";
    public static final String BACK_BUTTON_LABEL = "Back";
    public static final String NO_ENTRIES_LABEL = "No one has joined yet.";
    public static final String CHOICE_PROMPT =
            "Share your harvest count with other players? Choose Participate to appear on the "
                    + "leaderboard, or View only to just look.";

    private final List<LeaderboardViewModelListener> listeners = new ArrayList<>();
    private LeaderboardState state = new LeaderboardState();

    public LeaderboardState getState() {
        return state;
    }

    public void setState(LeaderboardState state) {
        this.state = state;
    }

    public void addListener(LeaderboardViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (LeaderboardViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}