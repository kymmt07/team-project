package data_access;
import entity.FlowerPlot;

public interface PlotDataAccessInterface {

    FlowerPlot getPlot(int plotID);
    void savePlot(FlowerPlot plot);
}
