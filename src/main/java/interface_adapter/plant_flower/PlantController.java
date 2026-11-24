package interface_adapter.plant_flower;

import use_case.plant_flower.PlantFlowerInputBoundary;
import use_case.plant_flower.PlantFlowerInputData;

public class PlantController {

    private final PlantFlowerInputBoundary plantFlowerUseCase;

    public PlantController(PlantFlowerInputBoundary plantFlowerUseCase) {
        this.plantFlowerUseCase = plantFlowerUseCase;
    }

    public void execute(int plotID) {
        PlantFlowerInputData plantRequest = new PlantFlowerInputData(plotID);
        plantFlowerUseCase.execute(plantRequest);
    }
}
