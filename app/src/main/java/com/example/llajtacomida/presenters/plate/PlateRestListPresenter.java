package com.example.llajtacomida.presenters.plate;

import com.example.llajtacomida.interfaces.PlateInterface;
import com.example.llajtacomida.models.plate.PlateModelRestList;
import com.example.llajtacomida.models.restaurant.Restaurant;

import java.util.ArrayList;

public class PlateRestListPresenter implements PlateInterface.presenterRestList {

    private PlateInterface.ViewRestlist viewRestlist;
    private PlateInterface.ModelRestList modelRestList;

    public PlateRestListPresenter(PlateInterface.ViewRestlist viewRestlist){
        this.viewRestlist = viewRestlist;
        modelRestList = new PlateModelRestList(this);
    }

    @Override
    public void showRestList(ArrayList<Restaurant> list) {
        viewRestlist.showRestList(list);
    }

    @Override
    public void filterRestaurantListWithPlate(String plateId) {
        modelRestList.filterRestaurantListWithPlate(plateId);
    }

    @Override
    public void stopRealtimeDatabase() {
        modelRestList.stopRealtimeDatabase();
    }
}
