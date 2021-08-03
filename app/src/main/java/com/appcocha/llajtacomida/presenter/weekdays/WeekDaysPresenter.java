package com.appcocha.llajtacomida.presenter.weekdays;

import com.appcocha.llajtacomida.interfaces.SearchPlate;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.weekdays.WeekDaysModel;

import java.util.ArrayList;

public class WeekDaysPresenter implements SearchPlate.PresenterPlateFound {

    private SearchPlate.ViewPlateFound viewPlateFound;
    private SearchPlate.ModelFindPlate modelFindPlate;
    public WeekDaysPresenter(SearchPlate.ViewPlateFound viewPlateFound){
        this.viewPlateFound = viewPlateFound;
        modelFindPlate = new WeekDaysModel(this);
    }

    @Override
    public void showPlateList(ArrayList<Plate> plateList) {
        viewPlateFound.showPlateList(plateList);
    }

    @Override
    public void searchPlateName(String name) {
        modelFindPlate.searchPlateName(name);
    }

    @Override
    public void stopRealtimeDatabase() {
        modelFindPlate.stopRealtimeDatabase();
    }
}
