package data_access;
import entity.FlowerPlot;

import java.util.HashMap;
import java.util.Map;

public class PlotDataAccess implements PlotDataAccessInterface {

    private final Map<Integer, FlowerPlot> plots = new HashMap<>();

    @Override
    public FlowerPlot getPlot(int plotID) {
        FlowerPlot existingPlot = plots.get(plotID);

        if (existingPlot == null) {
            return null;
        }

        return new FlowerPlot(
                existingPlot.getPlotID(),
                existingPlot.getState());
    }

    @Override
    public void savePlot(FlowerPlot plot) {
        FlowerPlot plotCopy = new FlowerPlot(
                plot.getPlotID(),
                plot.getState());

        plots.put(plot.getPlotID(), plotCopy);
    }
}
