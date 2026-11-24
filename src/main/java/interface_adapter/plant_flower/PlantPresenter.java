package interface_adapter.plant_flower;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.plant_flower.PlantFlowerOutputBoundary;
import use_case.plant_flower.PlantFlowerOutputData;

import javax.swing.*;

public class PlantPresenter implements PlantFlowerOutputBoundary {

    private final LoggedInViewModel loggedInViewModel;

    public PlantPresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(PlantFlowerOutputData result) {
        LoggedInState state = loggedInViewModel.getState();
        state.setPlotState(result.getPlotID(), result.getState());
        loggedInViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage);
    }
}
