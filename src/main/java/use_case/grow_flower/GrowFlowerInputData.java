package use_case.grow_flower;

public class GrowFlowerInputData {

    private final int plotID;

    public GrowFlowerInputData(int plotID) {
        this.plotID = plotID;
    }

    public int getPlotID() {
        return plotID;
    }
}
