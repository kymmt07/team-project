package use_case.grow_flower;

public class GrowFlowerOutputData {

    private final int plotID;
    private final String state;

    public GrowFlowerOutputData(int plotID, String state) {
        this.plotID = plotID;
        this.state = state;
    }

    public int getPlotID() {
        return plotID;
    }

    public String getState() {
        return state;
    }
}
