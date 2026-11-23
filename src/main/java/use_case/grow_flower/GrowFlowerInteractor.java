package use_case.grow_flower;
import data_access.PlotDataAccessInterface;
import entity.FlowerPlot;

public class GrowFlowerInteractor implements GrowFlowerInputBoundary {

    private final PlotDataAccessInterface plotDataAccess;
    private final GrowFlowerOutputBoundary growPresenter;

    public GrowFlowerInteractor(
            PlotDataAccessInterface plotDataAccess,
            GrowFlowerOutputBoundary growPresenter) {
        this.plotDataAccess = plotDataAccess;
        this.growPresenter = growPresenter;
    }

    @Override
    public void execute(GrowFlowerInputData growRequest) {
        int plotID = growRequest.getPlotID();
        FlowerPlot plot = plotDataAccess.getPlot(plotID);

        if (plot == null) {
            growPresenter.prepareFailView("The Selected plot does not exist.");
            return;
        }

        if (!"SMALL".equals(plot.getState())) {
            growPresenter.prepareFailView("Only a small flower can grow.");
            return;
        }

        plot.grow();
        plotDataAccess.savePlot(plot);

        GrowFlowerOutputData growResult =
                new GrowFlowerOutputData(plotID, plot.getState());

        growPresenter.prepareSuccessView((growResult));
    }
}