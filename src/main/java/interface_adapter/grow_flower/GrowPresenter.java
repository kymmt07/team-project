package interface_adapter.grow_flower;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;

import use_case.grow_flower.GrowFlowerOutputBoundary;
import use_case.grow_flower.GrowFlowerOutputData;

public class GrowPresenter implements GrowFlowerOutputBoundary{

    private final LoggedInViewModel loggedInViewModel;

    public GrowPresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(GrowFlowerOutputData growResult) {
        final LoggedInState state = loggedInViewModel.getState();

        state.setPlotState(growResult.getPlotID(), growResult.getState());
        state.setPlotError(null);

        loggedInViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final LoggedInState state = loggedInViewModel.getState();

        state.setPlotError(errorMessage);

        loggedInViewModel.notifyStateChanged();
    }
}
