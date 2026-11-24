package use_case.plant_flower;

public class PlantFlowerOutputData {

    private final int plotID;
    private final String state;

    public PlantFlowerOutputData(int plotID, String state) {
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
