package use_case.grow_flower;

public interface GrowFlowerOutputBoundary {
    void prepareSuccessView(GrowFlowerOutputData growResult);
    void prepareFailView(String errorMessage);
}
