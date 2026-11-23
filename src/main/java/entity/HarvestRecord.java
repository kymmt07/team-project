package entity;

/**
 * A player's harvest count and whether it is shared on the leaderboard.
 * Visibility is "unset" until the player chooses, then "public" or "private".
 */
public class HarvestRecord {

    public static final String UNSET = "unset";
    public static final String PUBLIC = "public";
    public static final String PRIVATE = "private";

    private final String username;
    private int harvestCount;
    private String visibility;

    public HarvestRecord(String username) {
        this(username, 0, UNSET);
    }

    public HarvestRecord(String username, int harvestCount, String visibility) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (harvestCount < 0) {
            throw new IllegalArgumentException("Harvest count cannot be negative");
        }
        checkVisibility(visibility);
        this.username = username;
        this.harvestCount = harvestCount;
        this.visibility = visibility;
    }

    public String getUsername() {
        return username;
    }

    public int getHarvestCount() {
        return harvestCount;
    }

    public void incrementHarvestCount() {
        harvestCount++;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        checkVisibility(visibility);
        this.visibility = visibility;
    }

    private static void checkVisibility(String visibility) {
        if (!UNSET.equals(visibility) && !PUBLIC.equals(visibility)
                && !PRIVATE.equals(visibility)) {
            throw new IllegalArgumentException("Unknown visibility: " + visibility);
        }
    }
}