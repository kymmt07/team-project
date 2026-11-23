package entity;

public class FlowerPlot {

    public static final String EMPTY = "EMPTY";
    public static final String SMALL = "SMALL";
    public static final String BIG = "BIG";

    private final int plotID;
    private String state;

    public FlowerPlot(int plotID, String state) {
        if (!EMPTY.equals(state)
                && !SMALL.equals(state)
                && !BIG.equals(state)) {
            throw new IllegalArgumentException("Invalid plot state.");
        }

        this.plotID = plotID;
        this.state = state;
    }

    public int getPlotID() {
        return plotID;
    }

    public String getState() {
        return state;
    }

    public void grow() {
        if (!SMALL.equals(state)) {
            throw new IllegalStateException("Only a small flower can grow.");
        }
        this.state = BIG;
    }
}
