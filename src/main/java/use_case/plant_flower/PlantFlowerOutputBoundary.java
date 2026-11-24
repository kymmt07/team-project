package use_case.plant_flower;

public interface PlantFlowerOutputBoundary {
    void prepareSuccessView(
            PlantFlowerOutputData plantResult);

    void prepareFailView(String errorMessage);
}
