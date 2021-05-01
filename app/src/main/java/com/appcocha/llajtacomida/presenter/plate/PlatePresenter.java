package com.appcocha.llajtacomida.presenter.plate;

import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.plate.PlateModel;
import java.util.ArrayList;

/**
 * Presentador, plato
 */
public class PlatePresenter implements PlateInterface.PresenterPlate{

    private PlateInterface.ViewPlate viewPlate;
    private PlateInterface.ModelPlate modelPlate;

    /**
     * Contructor, establece viewPlate
     * @param viewPlate
     */
    public PlatePresenter(PlateInterface.ViewPlate viewPlate){
        this.viewPlate = viewPlate;
        modelPlate = new PlateModel(this);
    }

    @Override
    public void showPlate(Plate plate) {
        viewPlate.showPlate(plate);
    }

    @Override
    public void searchPlate(String plateId) {
        modelPlate.searchPlate(plateId);
    }

    @Override
    public void loadPlatesList() {
        modelPlate.loadPlatesList();
    }

    @Override
    public void showPlateList(ArrayList<Plate> plateList) {
        viewPlate.showPlateList(plateList);
    }

    @Override
    public void stopRealtimeDatabase() {
        modelPlate.stopRealtimeDatabase();
    }
}
