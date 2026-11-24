package use_case.grow_flower;

import data_access.PlotDataAccess;
import entity.FlowerPlot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GrowFlowerInteractorTest {

    @Test
    void emptyPlotCannotGrow() {
        // Prepare an empty plot.
        PlotDataAccess plotDataAccess = new PlotDataAccess();
        plotDataAccess.savePlot(new FlowerPlot(2, FlowerPlot.EMPTY));

        TestPresenter presenter = new TestPresenter();
        GrowFlowerInteractor interactor = new GrowFlowerInteractor(plotDataAccess, presenter);

        // Attempt to grow a flower in the empty plot.
        interactor.execute(new GrowFlowerInputData(2));

        // Check that the plot remains empty.
        FlowerPlot savedPlot = plotDataAccess.getPlot(2);
        assertNotNull(savedPlot);
        assertEquals(FlowerPlot.EMPTY, savedPlot.getState());

        // Check that failure was reported.
        assertNotNull(presenter.errorMessage);
        assertNull(presenter.growResult);
    }

    @Test
    void smallFlowerGrowsToBig(){
        // Prepare a small flower.
        PlotDataAccess plotDataAccess = new PlotDataAccess();
        plotDataAccess.savePlot(new FlowerPlot(3, FlowerPlot.SMALL));

        TestPresenter presenter = new TestPresenter();
        GrowFlowerInteractor interactor = new GrowFlowerInteractor(plotDataAccess, presenter);

        // Ask the use case to grow the flower in plot 3.
        interactor.execute(new GrowFlowerInputData(3));

        // Check the saved state.
        FlowerPlot savedPlot = plotDataAccess.getPlot(3);
        assertNotNull(savedPlot);
        assertEquals(FlowerPlot.BIG, savedPlot.getState());

        // Check the result sent to the presenter.
        assertNotNull(presenter.growResult);
        assertEquals(3, presenter.growResult.getPlotID());
        assertEquals(FlowerPlot.BIG, presenter.growResult.getState());
        assertNull(presenter.errorMessage);
    }

    @Test
    void bigFlowerCannotGrow() {
        // Prepare a big flower.
        PlotDataAccess plotDataAccess = new PlotDataAccess();
        plotDataAccess.savePlot(new FlowerPlot(4, FlowerPlot.BIG));

        TestPresenter presenter = new TestPresenter();
        GrowFlowerInteractor interactor = new GrowFlowerInteractor(plotDataAccess, presenter);

        // Attempt to grow the big flower.
        interactor.execute(new GrowFlowerInputData(4));

        // Check that the saved state remains BIG.
        FlowerPlot savedPlot = plotDataAccess.getPlot(4);
        assertNotNull(savedPlot);
        assertEquals(FlowerPlot.BIG, savedPlot.getState());

        // Check that failure was reported.
        assertNotNull(presenter.errorMessage);
        assertNull(presenter.growResult);
    }

    @Test
    void nonexistentPlotCannotGrow() {
        // Create storage without adding any plots.
        PlotDataAccess plotDataAccess = new PlotDataAccess();

        TestPresenter presenter = new TestPresenter();
        GrowFlowerInteractor interactor = new GrowFlowerInteractor(plotDataAccess, presenter);

        // Attempt to grow a flower in the nonexistent plot.
        interactor.execute(new GrowFlowerInputData(3));

        // Check that no plot was created.
        assertNull(plotDataAccess.getPlot(3));

        // Check that failure was reported.
        assertNotNull(presenter.errorMessage);
        assertNull(presenter.growResult);
    }

    private static class TestPresenter implements GrowFlowerOutputBoundary {
        private GrowFlowerOutputData growResult;
        private String errorMessage;

        @Override
        public void prepareSuccessView(GrowFlowerOutputData growResult) {
            this.growResult = growResult;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
