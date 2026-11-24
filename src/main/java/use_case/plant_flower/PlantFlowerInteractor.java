package use_case.plant_flower;

import data_access.PlotDataAccessInterface;
import entity.FlowerPlot;

public class PlantFlowerInteractor implements PlantFlowerInputBoundary {

    private final PlotDataAccessInterface plotDataAccess;
    private final PlantFlowerOutputBoundary plantPresenter;

    public PlantFlowerInteractor(PlotDataAccessInterface plotDataAccess, PlantFlowerOutputBoundary plantPresenter) {
        this.plotDataAccess = plotDataAccess;
        this.plantPresenter = plantPresenter;
    }

    @Override
    public void execute(PlantFlowerInputData plantRequest) {
        int plotID = plantRequest.getPlotID();

        FlowerPlot plot = plotDataAccess.getPlot(plotID);

        if (plot == null) {
            plantPresenter.prepareFailView("The selected plot does not exist.");
            return;
        }

        if (!"EMPTY".equals(plot.getState())) {
            plantPresenter.prepareFailView("The selected plot is already occupied");
            return;
        }

        plot.plant();
        plotDataAccess.savePlot(plot);

        PlantFlowerOutputData plantResult = new PlantFlowerOutputData(plotID, plot.getState());

        plantPresenter.prepareSuccessView(plantResult);
    }
}
