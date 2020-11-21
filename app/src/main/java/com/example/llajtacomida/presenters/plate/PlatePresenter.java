package com.example.llajtacomida.presenters.plate;

import com.example.llajtacomida.interfaces.PlateInterface;
import com.example.llajtacomida.models.plate.Plate;
import com.example.llajtacomida.models.plate.PlateModel;

import java.util.ArrayList;

public class PlatePresenter implements PlateInterface.PresenterPlate{

    private PlateInterface.ViewPlate viewPlate;
    private PlateInterface.ModelPlate modelPlate;

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
