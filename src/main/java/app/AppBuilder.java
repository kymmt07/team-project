package app;

import data_access.FileUserDataAccessObject;
import data_access.PlotDataAccess;

import entity.UserFactory;
import entity.FlowerPlot;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login_signup.LoginSignupViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.grow_flower.GrowController;
import interface_adapter.grow_flower.GrowPresenter;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.grow_flower.GrowFlowerInputBoundary;
import use_case.grow_flower.GrowFlowerInteractor;
import use_case.grow_flower.GrowFlowerOutputBoundary;

import view.LoggedInView;
import view.LoginSignupView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    final UserFactory userFactory = new UserFactory();
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardLayout, cardPanel, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("users.csv", userFactory);

    private LoginSignupView loginSignupView;
    private LoginSignupViewModel loginSignupViewModel;
    private LoggedInView loggedInView;
    private LoggedInViewModel loggedInViewModel;

    private final PlotDataAccess plotDataAccess = new PlotDataAccess();

    public AppBuilder(){
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addLoginSignupView() {
        loginSignupViewModel = new LoginSignupViewModel();
        loginSignupView = new LoginSignupView(loginSignupViewModel);
        cardPanel.add(loginSignupView, LoginSignupViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, LoggedInViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(loginSignupViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        final SignupController controller = new SignupController(userSignupInteractor);
        loginSignupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase(){
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(loginSignupViewModel,
                loggedInViewModel, viewManagerModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(userDataAccessObject, loginOutputBoundary);
        final LoginController loginController = new LoginController(loginInteractor);
        loginSignupView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addGrowUseCase() {
        final GrowFlowerOutputBoundary growFlowerOutputBoundary = new GrowPresenter(loggedInViewModel);
        final GrowFlowerInputBoundary growInteractor = new GrowFlowerInteractor(plotDataAccess, growFlowerOutputBoundary);
        final GrowController growController = new GrowController(growInteractor);
        loggedInView.setGrowController(growController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Login / Sign Up");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(LoginSignupViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }

    public AppBuilder initializePlots() {
        for (int plotID = 1; plotID <= 9; plotID ++) {
            final FlowerPlot plot = new FlowerPlot(plotID, FlowerPlot.EMPTY);

            plotDataAccess.savePlot(plot);
            loggedInViewModel.getState().setPlotState(plotID, plot.getState());
        }
        return this;
    }
}