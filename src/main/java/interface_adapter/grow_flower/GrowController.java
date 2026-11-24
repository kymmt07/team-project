package interface_adapter.grow_flower;

import use_case.grow_flower.GrowFlowerInputBoundary;
import use_case.grow_flower.GrowFlowerInputData;

public class GrowController {

    private final GrowFlowerInputBoundary growFlowerUseCase;

    public GrowController(GrowFlowerInputBoundary growFlowerUseCase) {
        this.growFlowerUseCase = growFlowerUseCase;
    }

    public void execute (int plotID) {
        GrowFlowerInputData growRequest = new GrowFlowerInputData(plotID);
        growFlowerUseCase.execute(growRequest);
    }
}