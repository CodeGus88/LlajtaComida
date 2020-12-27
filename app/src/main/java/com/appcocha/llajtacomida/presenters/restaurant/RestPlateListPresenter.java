package com.appcocha.llajtacomida.presenters.restaurant;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.models.restaurant.RestaurantPlateListModel;
import com.appcocha.llajtacomida.presenters.restaurant.RestaurantPresenter;

import java.util.ArrayList;

public class RestPlateListPresenter implements RestaurantInterface.PresenterPlateList {

    private RestaurantInterface.ModelPlateList modelPlateList;
    private RestaurantInterface.ViewPlateList viewPlateList;

    public RestPlateListPresenter(RestaurantInterface.ViewPlateList viewPlateList){
        this.viewPlateList = viewPlateList;
        modelPlateList = new RestaurantPlateListModel(this);
    }

    @Override
    public void showPlateList(ArrayList<Plate> list) {
        viewPlateList.showPlateList(list);
    }

    @Override
    public void filterPlateListInMenu(String restaurantId) {
        modelPlateList.filterPlateListInMenu(restaurantId);
    }
    @Override
    public void stopRealtimeDatabse(){
        modelPlateList.stopRealtimeDatabse();
    }
}
