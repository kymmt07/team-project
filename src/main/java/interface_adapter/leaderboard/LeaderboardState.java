package interface_adapter.leaderboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Everything the Leaderboard view needs to show, already turned into plain text.
 */
public class LeaderboardState {
    private List<String> rankLines = new ArrayList<>();
    private String ownCountText = "";
    private boolean participating = false;
    private boolean choiceNeeded = false;
    private String message = "";

    public List<String> getRankLines() {
        return new ArrayList<>(rankLines);
    }

    public void setRankLines(List<String> rankLines) {
        this.rankLines = new ArrayList<>(rankLines);
    }

    public String getOwnCountText() {
        return ownCountText;
    }

    public void setOwnCountText(String ownCountText) {
        this.ownCountText = ownCountText;
    }

    public boolean isParticipating() {
        return participating;
    }

    public void setParticipating(boolean participating) {
        this.participating = participating;
    }

    public boolean isChoiceNeeded() {
        return choiceNeeded;
    }

    public void setChoiceNeeded(boolean choiceNeeded) {
        this.choiceNeeded = choiceNeeded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}